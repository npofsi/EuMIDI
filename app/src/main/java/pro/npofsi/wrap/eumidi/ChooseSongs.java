package pro.npofsi.wrap.eumidi;
import android.content.*;
import android.webkit.*;
import android.app.*;
import pro.npofsi.wrap.eumidi.comps.*;
import org.json.*;
import java.io.*;

public class ChooseSongs
{
	boolean[] b ;
	WebView webv;
	Activity activ;
	Context convv;
	AlertDialog.Builder builder;
	FileX fx;
	public ChooseSongs(Context context,Activity activity,WebView web){
		webv=web;
		activ=activity;
		convv=context;
		fx=new FileX(context.getDataDir().getAbsolutePath()+"/euphony/tracks");
		builder=new AlertDialog.Builder(context);
		String[] s = null;
		try
		{
			s=new TracksUtils(convv).getTracks();
		}
		catch (JSONException e)
		{}
		if(s!=null){
			b = new boolean[s.length];
			for(int i=0;i<s.length;i++)b[i]=false;
			builder.setMultiChoiceItems(s,b , new DialogInterface.OnMultiChoiceClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2, boolean p3)
				{
					// TODO: Implement this method
					b[p2]=p3;
				}
			});
		}
		
		builder.setTitle("Remove Songs");
		builder.setNegativeButton("Cancel",new onNegativeButton());
		//builder.setNeutralButton("Remove All",new onNeutralButton());
		builder.setPositiveButton("Confirm",new onPositiveButton());
		builder.create().show();
	}


	class onNeutralButton implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface p1, int p2)
		{

			int c=0;
			try
			{
				new TracksUtils(convv).removeAllTracks();
				c++;
			}
			catch (Exception e)
			{new AlertDialog.Builder(convv).setMessage("Error:"+e.toString()).setPositiveButton("comfirm",null).create().show();}
			
			new AlertDialog.Builder(convv).setMessage("The page will reload until you add a new song.").setPositiveButton("comfirm",null).create().show();

			webv.reload();
			//webv.loadUrl("http://localhost:30100/index.html#1");
			// TODO: Implement this method
		}
	}

	class onNegativeButton implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface p1, int p2)
		{
			p1.dismiss();
			// TODO: Implement this method

		}
	}

	class onPositiveButton implements DialogInterface.OnClickListener
	{
		@Override
		public void onClick(DialogInterface p1, int p2)
		{
			int c=0;
			int t=0;
			for(int i=0;i<b.length;i++){
				if(b[i])t++;
			}
			if(t==b.length){
				new AlertDialog.Builder(convv).setMessage("You must keep 1 song at least.").setPositiveButton("comfirm",null).create().show();
				return;
			}
			for(int i=0;i<b.length;i++){
				if(b[i])try
				{
					new TracksUtils(convv).removeTrack(i);
					c++;
				}
				catch (JSONException e)
				{}
				catch (IOException e)
				{}
			}
			new AlertDialog.Builder(convv).setMessage(c+" songs removed.").setPositiveButton("comfirm",null).create().show();
			
			
			webv.reload();
			// TODO: Implement this method
		}
	}
}
