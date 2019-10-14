package pro.npofsi.wrap.eumidi;
import java.io.*;
//import java.util.*;
import com.google.gson.*;
import java.nio.*;
import pro.npofsi.wrap.eumidi.comps.*;
import pro.npofsi.wrap.eumidi.comps.jsonx.*;
import org.json.*;
import android.content.*;
import android.util.*;

public class TracksUtils
{
	FileX index;
	ArrayX array;
	public TracksUtils(Context ctx) throws JSONException{
		index=new FileX(ctx.getDataDir().getAbsolutePath() + "/euphony/tracks");
		array=new ArrayX(index.forward("index.json"));
	}
	String[] getTracks(){
		
		String[] list=new String[array.length()];
		for(int i=0;i<list.length;i++){
			list[i]=array.optString(i,"Broken File");
		}
		
		return list;//(new File(path).list());
	}
	
	void addTrackFlag(String name) throws JSONException, IOException{
		array=new ArrayX(index.forward("index.json"));
		Log.e("eumidi",array.toString(1));
		if(getTrackIndexByName(name)==-1){
			array.put(name);
			array.save();
		}
		
		
	}
	
	void removeTrackFlag(int indexx) throws JSONException, IOException{
		array.remove(indexx);
		array.save();
	}
	
	void removeAllTracks() throws JSONException, IOException{
		for(int i=array.length()-1;i>=0;i--){
			index.forward(getTrackNameByIndex(i)).deleteX();
			
		}
		index.deleteX();
		index.mkdirs();
		index.forwardACNF("index.json").getFileOutputStream().write(("[]").getBytes());
		array=new ArrayX(index.forward("index.json"));
		//array.save();
		
	}
	
	int getTrackIndexByName(String name){
		
		for(int i=0;i<array.length();i++){
			if(name==array.optString(i)){
				return i;
			}
		}
		return -1;
	}
	
	String getTrackNameByIndex(int indexx){
		return array.optString(indexx,"Broken File");
	}
	
	void removeTrackFlag(String name) throws JSONException, IOException{	
		removeTrackFlag(getTrackIndexByName(name));
	}
	
	void removeTrack(int indexx) throws JSONException, IOException{
		if(indexx!=-1&&indexx<array.length()){
			index.forward(getTrackNameByIndex(indexx)).deleteX();
			removeTrackFlag(indexx);
			
		}
	}
	
	String toBase64Data(byte[] b){
		return "data:audio/midi;base64,"+java.util.Base64.getEncoder().encodeToString(b);
	}
	
	
	void addTrack(String name,byte[] base){
		
		
		if(index.forward(name).check())return;
		FileX f=index.forwardACNF(name);
		
		try
		{
			addTrackFlag(name);
			f.getFileOutputStream().write(toBase64Data(base).getBytes());
			//f.createNewFile();
			//new FileOutputStream(f).write(("data:audio/midi;base64,"+bases).getBytes());
		}
		catch (Exception e)
		{}
	}
	
	
	
	
	
}
