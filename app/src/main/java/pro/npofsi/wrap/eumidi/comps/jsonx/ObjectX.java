package pro.npofsi.wrap.eumidi.comps.jsonx;

import java.io.*;
import org.json.*;
import pro.npofsi.wrap.eumidi.comps.*;



public class ObjectX extends JSONObject
{
	FileX mfile;
	int isOrigin=3;
	ArrayX originArray;
	int indexx;
	ObjectX originObject;
	String namee;
	public ObjectX(FileX file) throws JSONException
	{
		
		super(FileParsers.parserText(file));
		mfile = file;
		isOrigin = 0;
	}
	public ObjectX(String str) throws JSONException
	{
		super(str);
	}
	public ObjectX setOrigin(int index, ArrayX array)
	{
		isOrigin = 1;
		originArray = array;
		indexx = index;
		return this;
	}
	public ObjectX setOrigin(String name, ObjectX object)
	{
		isOrigin = 2;
		originObject = object;
		namee = name;
		return this;
	}
	public ArrayX optJSONArrayX(String name) throws JSONException
	{
		return new ArrayX(this.optJSONArray(name).toString()).setOrigin(name, this);
	}
	public ObjectX optJSONObjectX(String name) throws JSONException
	{
		return new ObjectX(this.optJSONObject(name).toString()).setOrigin(name, this);
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
