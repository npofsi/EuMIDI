package pro.npofsi.wrap.eumidi.comps.jsonx;

import java.io.*;
import org.json.*;
import pro.npofsi.wrap.eumidi.*;
import pro.npofsi.wrap.eumidi.comps.*;


public class ArrayX extends JSONArray
{
	FileX mfile;
	int isOrigin=3;//0 yes ;1 array ;2 object;
	ArrayX originArray;
	int indexx;
	ObjectX originObject;
	String namee;
	public ArrayX(FileX file) throws JSONException
	{
		super(FileParsers.parserText(file));
		mfile = file;
		isOrigin = 0;
	}
	public ArrayX(String str) throws JSONException
	{
		super(str);
	}
	public ArrayX setOrigin(int index, ArrayX array)
	{
		isOrigin = 1;
		originArray = array;
		indexx = index;
		return this;
	}
	public ArrayX setOrigin(String name, ObjectX object)
	{
		isOrigin = 2;
		originObject = object;
		namee = name;
		return this;
	}
	public ArrayX optJSONArrayX(int index) throws JSONException
	{
		return new ArrayX(this.optJSONArray(index).toString()).setOrigin(index, this);
	}
	public ObjectX optJSONObjectX(int index) throws JSONException
	{
		return new ObjectX(this.optJSONObject(index).toString()).setOrigin(index, this);
	}
	public boolean save() throws JSONException, IOException
	{
		boolean flag=true;
		switch (isOrigin)
		{
			case 0:
				mfile.write(this.toString().getBytes());
				break;
			case 1:
				originArray.put(indexx, this);
				flag = originArray.save();
				break;
			case 2:
				originObject.put(namee, this);
				flag = originObject.save();
				break;
			case 3:
				flag = false;
				break;
		};
		return flag;
	}
	public void cache() throws JSONException{
		switch (isOrigin)
		{
			case 0:
				//mfile.write(this.toString().getBytes());
				break;
			case 1:
				originArray.put(indexx, this);
				originArray.cache();
				break;
			case 2:
				originObject.put(namee, this);
				originObject.cache();
				break;
			case 3:
				break;
		};
	}
	public FileX getFileX()
	{
		return mfile;
	}

}
