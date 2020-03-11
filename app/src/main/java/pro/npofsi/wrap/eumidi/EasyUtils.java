package pro.npofsi.wrap.eumidi;

import android.app.*;
import android.database.*;
import android.net.*;
import android.provider.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

import android.util.*;

public class EasyUtils
{
	public static boolean[] createBooleanArray(boolean v,int l){
		boolean[] b=new boolean[l];
		for(int i=0;i<b.length;i++){
			b[i]=v;
		}
		return b;
	}
	

	//assuming "this" is an Activity):

	public static String getFileName(Uri uri,Activity activity) {
		String result = null;
		if (uri.getScheme().equals("content")) {
			Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
				}
			} finally {
				cursor.close();
			}
		}
		if (result == null) {
			result = uri.getPath();
			int cut = result.lastIndexOf('/');
			if (cut != -1) {
				result = result.substring(cut + 1);
			}
		}
		return result;
	}
	
	public static String idGen(){
		String str="";
		for(int i=0;i<8;i++)str+=Integer.toHexString((int)(Math.random()*0xf));
		return str;
	};
	public static String idGen(int n){
		String str="";
		for(int i=0;i<n;i++)str+=Integer.toHexString((int)(Math.random()*0xf));
		return str;
	};
	
	public static String parserText(File f){
		String text="";
		try
		{
			byte[] buffer = new byte[(int)f.length()];
			(new FileInputStream(f)).read(buffer);
			text=new String(buffer);

			//list[i]=(new Gson()).fromJson(configs,config.class);
		}
		catch (IOException e)
		{}
		return text;
	}
	
	
}
