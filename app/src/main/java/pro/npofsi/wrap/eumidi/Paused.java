package pro.npofsi.wrap.eumidi;
import android.content.*;
import android.app.*;
import android.webkit.*;
import android.view.View.*;

public class Paused
{
	final int REQUEST_CHOOSEFILE=10;
	WebView webv;
	Activity activ;
	Context convv;
	AlertDialog.Builder builder;
	public Paused(Context context,Activity activity,WebView web){
		webv=web;
		activ=activity;
		convv=context;
		builder=new AlertDialog.Builder(context);
		builder.setItems(new CharSequence[]{
			"Remove Song",
			"Add Song"
		}, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					// TODO: Implement this method
					switch(p2){
						case 0:
							new ChooseSongs(convv,activ,webv);
							break;
						case 1:
							chooseFile();
					}
				}
			});
		builder.setTitle("Manage Songs");
		builder.setNegativeButton("About",new onNegativeButton());
		builder.setNeutralButton("Exit",new onNeutralButton());
		builder.setPositiveButton("Refresh",new onPositiveButton());
		builder.create().show();
	}
	
	
	class onNegativeButton implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface p1, int p2)
		{
			// TODO: Implement this method
			WebView wb=new WebView(convv);
			wb.loadUrl("http://localhost:30100/about/about.html");
			new AlertDialog.Builder(convv).setView(wb).create().show();
		}
	}
	
	class onNeutralButton implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface p1, int p2)
		{
			activ.finish();
			// TODO: Implement this method
			
		}
	}
	
	class onPositiveButton implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface p1, int p2)
		{
			webv.reload();
			// TODO: Implement this method
		}
	}
	
	void chooseFile(){

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType(“image/*”);//选择图片
        //intent.setType(“audio/*”); //选择音频
        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
        intent.setType("audio/midi");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activ.startActivityForResult(intent, REQUEST_CHOOSEFILE);

	}
}
