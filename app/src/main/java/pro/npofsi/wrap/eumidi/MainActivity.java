package pro.npofsi.wrap.eumidi;

import android.app.*;
import android.os.*;
import android.webkit.*;
import android.widget.*;
import com.yanzhenjie.andserver.*;
import android.content.res.*;
import com.yanzhenjie.andserver.website.*;
import java.util.concurrent.*;
import android.content.pm.*;
import android.view.*;
import pro.npofsi.wrap.eumidi.comps.*;
import android.content.*;
import android.net.*;
import org.json.*;
import java.io.*;
import android.*;


public class MainActivity extends Activity 
{
		//读写权限
		private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
		//请求状态码
		private static int REQUEST_PERMISSION_CODE = 1;

		
		@Override
		public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			if (requestCode == REQUEST_PERMISSION_CODE) {
				for (int i = 0; i < permissions.length; i++) {
					//Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
				}
			}
		}
	
	final int REQUEST_CHOOSEFILE=10;
	WebView webview;
	TextView textview;
	FrameLayout framelayout;
	Server.Builder aserver;
	Server server;
	FileX rootfx;
	TracksUtils tu=null;
	String path;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        setContentView(R.layout.main);
		
		
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
			if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				this.requestPermissions(PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
			}
		}
		
		framelayout=findViewById(R.id.mainFrameLayout);
		textview = findViewById(R.id.debugTextView);
		webview = findViewById(R.id.mainWebView);
		webSettings(webview);

		path=getDataDir().getAbsolutePath();
		startServer(path);
		rootfx=new FileX(path);
		try
		{
			tu = new TracksUtils(this);
		}
		catch (JSONException e)
		{}
		try
		{
			if(!rootfx.forward("euphony").forward("index.html").check())ZipUtils.UnZipAssetsFolder(this, "euphony/euphony.zip", rootfx.forward("euphony").getAbsolutePath());
			if(!rootfx.forward("euphony").forward("tracks").forward("index.json").check())ZipUtils.UnZipAssetsFolder(this,"tracks/tracks.zip",rootfx.forward("euphony").forward("tracks").getAbsolutePath());
			if(!rootfx.forward("euphony").forward("about").forward("about.html").check())ZipUtils.UnZipAssetsFolder(this,"about/about.zip",rootfx.forward("euphony").forward("about").getAbsolutePath());
			webview.loadUrl("http://localhost:30100/index.html");
		}
		catch (Exception e)
		{
			textview.setText(e.toString());
			framelayout.removeView(webview);
		}
    }

	@Override
	protected void onStop()
	{
		// TODO: Implement this method
		server.shutdown();
		super.onStop();
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO: Implement this method
		if(keyCode == event.KEYCODE_BACK){
			if(webview!=null)new Paused(this,this,webview);
			
			return true;
		}
		//return true;
		return super.onKeyDown(keyCode, event);
	}
	
	public void startServer(String path){

		//AssetManager不能被关闭。
		//AssetManager mAssetManager = getAssets();

		//WebSite website = new AssetsWebsite(mAssetManager, "euphony");
		WebSite website = new StorageWebsite(path+"/euphony");

		aserver=AndServer.serverBuilder();
		server=aserver.port(30100)
			.timeout(10,TimeUnit.SECONDS)
			.website(website)
			.build();

		server.startup();
		
	}
	
	
	void refresh(){
		try
		{
			tu=new TracksUtils(this);
		}
		catch (JSONException e)
		{}
		
		webview.loadUrl("http://localhost:30100/index.html#1");
		
	}
	
	void chooseFile(){
		
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType(“image/*”);//选择图片
        //intent.setType(“audio/*”); //选择音频
        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
        intent.setType("audio/midi");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CHOOSEFILE);
		
	}
	
	
@Override
protected void onActivityResult(int requestCode,int resultCode,Intent data){//选择文件返回
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
        switch(requestCode){
            case REQUEST_CHOOSEFILE:
            Uri uri=data.getData();
            server.startup();
			String chooseFilePath=FileChooseUtil.getInstance(this).getChooseFileResultPath(uri);
		//chooseFilePath.split(FileX.separator)
				try
				{
					refresh();
					tu.addTrack(new FileX(chooseFilePath).getName(), new FileX(chooseFilePath).read());
				}
				catch (Exception e)
				{
					new AlertDialog.Builder(this).setMessage("Error:"+e.toString()).setPositiveButton("confirm",null).create().show();
				}
				finally{
					new AlertDialog.Builder(this).setMessage("Added:"+new FileX(chooseFilePath).getName()).setPositiveButton("confirm",null).create().show();
				}
			//refresh();
			//server.startup();
			//webview.loadUrl("http://localhost:30100/index.html#1");
			startServer(path);
			webview.reload();
				//Log.d(TAG,"选择文件返回："+chooseFilePath);
			//sendFileMessage(chooseFilePath);
            break;
        }
    }
}
	
	public void webSettings(WebView webview){
		WebSettings webSettings = webview.getSettings();
		//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
		webSettings.setJavaScriptEnabled(true);  
// 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
// 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
		webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
		webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小 
//缩放操作
		webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
		webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
		webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

//其他细节操作
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存 
		webSettings.setAllowFileAccess(true); //设置可以访问文件 
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口 
		webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
		webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
		webSettings.setBuiltInZoomControls(false);
		webSettings.setSupportZoom(false);

		webview.setAnimationCacheEnabled(true);
		webview.setWebChromeClient(new WebChromeClient());
		webview.setWebViewClient(new WebViewClient());
		webview.setScrollBarDefaultDelayBeforeFade(0);
		webview.setScrollBarFadeDuration(0);
		webview.setScrollBarSize(0);

		webview.setFocusable(true);
		webview.setFocusedByDefault(true);
		webview.setFocusableInTouchMode(true);
		
	}
	
}
