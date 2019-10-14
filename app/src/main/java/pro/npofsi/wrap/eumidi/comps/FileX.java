package pro.npofsi.wrap.eumidi.comps;
import java.io.*;
import android.util.*;

public class FileX extends File
{
	public FileX(String path){
		super(path);
	}
	
	public static FileX toFileX(File file){
		return new FileX(file.getAbsolutePath());
	}
	
	public FileX[] listFileXFiles(){
		File[] files=this.listFiles()==null?new File[]{}:this.listFiles()==null?new File[]{}:this.listFiles();
		FileX[] fileXs= new FileX[this.countFiles()];
		int i=0;
		for(File file:files){
			if(file.isFile()){
				fileXs[i]=FileX.toFileX(file);
				i++;
			}
		}
		files = null;
		return fileXs;
	}
	
	public FileX[] listFileXDirectorys(){
		File[] files=this.listFiles()==null?new File[]{}:this.listFiles();
		FileX[] fileXs= new FileX[this.countDirectory()];
		int i=0;
		for(File file:files){
			if(file.isDirectory()){
				fileXs[i]=FileX.toFileX(file);
				i++;
			}
		}
		files = null;
		return fileXs;
	}
	public FileX[] listFileXs(FilenameFilter fnf){
		File[] files=this.listFiles(fnf);
		FileX[] fileXs= new FileX[files.length];
		int i=0;
		for(File file:files){
			fileXs[i]=FileX.toFileX(file);
			i++;
		}
		files = null;
		return fileXs;
	}
	
	public FileX[] listFileXs(FileFilter ff){
		File[] files=this.listFiles(ff);
		FileX[] fileXs= new FileX[files.length];
		int i=0;
		for(File file:files){
			fileXs[i]=FileX.toFileX(file);
			i++;
		}
		files = null;
		return fileXs;
	}
	
	public FileX[] listFileXs(){
		File[] files=this.listFiles()==null?new File[]{}:this.listFiles();
		FileX[] fileXs= new FileX[files.length];
		int i=0;
		for(File file:files){
			fileXs[i]=FileX.toFileX(file);
			i++;
		}
		files = null;
		return fileXs;
	}
	
	public FileX forward(String childName){
		String now=this.getAbsolutePath();
		if(childName.startsWith(separator)){
			childName=childName.replaceFirst(separator,"");
		}
		if(childName.endsWith(pathSeparator)){
			childName=childName.substring(0,childName.length()-separator.length()-1);
		}
		return new FileX(now+separator+childName);
	}
	
	public FileX forwardACNF(String childName){
		FileX f=this.forward(childName);
		try
		{
			if(!f.exists())this.createNewFile();
		}
		catch (IOException e)
		{ 
			Log.e("CreateNewFile","i",e);
		}
		return f;
	}
	
	public FileX forwardAMKD(String childName){
		FileX f=this.forward(childName);
		if(!f.exists())this.mkdirs();
		return f;
	}
	
	public FileX backward(){
		return FileX.toFileX(this.getParentFile());
	}
	
	public FileX[] mkdirss(String[] childNames){
		FileX[] list=new FileX[childNames.length];
		int i=0;
		for(String childName:childNames){
			list[i]=this.forwardAMKD(childName);
			i++;
		}
		return list;
	}
	
	public FileX[] createNewFiles(String[] childNames){
		FileX[] list=new FileX[childNames.length];
		int i=0;
		for(String childName:childNames){
			list[i]=this.forwardACNF(childName);
			i++;
		}
		return list;
	}

	public void deleteX(){		
		if(this.isFile()&&this.exists()){
			this.delete();
		}
		if(this.isDirectory()&&this.exists()){
			FileX[] filexs = this.listFileXs();
			for(FileX filex:filexs){
				filex.deleteX();
			}
			this.delete();
			filexs = null;	// lets gc do its works
		}
	}
	
	public void deleteXU(final Runnable runnable){
		final FileX that=this;
		new Thread(new Runnable(){

				@Override
				public void run()
				{
					that.deleteX();
					runnable.run();
					// TODO: Implement this method
				}
			}).start();
	}
	
	public void clean(){
		FileX[] filexs = this.listFileXs();
		for(FileX filex:filexs){
			filex.deleteX();
		}
		filexs = null;	// lets gc do its works
	}
	
	public int countFiles(){
		if(this.isDirectory()){
			int i=0;
			for(File f:this.listFiles())if(f.isFile())i++;
			return i;
		}
		return 0;
	}
	public int countDirectory(){
		if(this.isDirectory()){
			int i=0;
			for(File f:this.listFiles())if(f.isDirectory())i++;
			return i;
		}
		return 0;
	}
	public int count(){
		if(this.isDirectory())return this.listFiles().length;
		return 0;
	}
	
	public boolean check(){
		if(!this.exists())return false;
		if(this.isFile()){
			return this.length()>0?true:false;
		}
		return true;
	}
	public boolean[] checkList(String[] childNames){
		boolean[] list= new boolean[childNames.length];
		int i=0;
		for(String childName: childNames){
			list[i]=this.forward(childName).check();
			i++;
		}
		return list;
	}
	public boolean checkWhole(String[] childNames){
		boolean[] list=checkList(childNames);
		int i=0;
		for(boolean result: list){
			if(!result)i++;
		}
		return i==0?true:false;
	}
	
	public FileInputStream getFileInputStream() throws FileNotFoundException{
		return new FileInputStream(this);
	}
	
	public FileOutputStream getFileOutputStream() throws FileNotFoundException{
		return new FileOutputStream(this);
	}
	
	public byte[] read() throws IOException{
		byte[] buffer = new byte[(int)this.length()];
		this.getFileInputStream().read(buffer);
		return buffer;
	}
	
	public void write(byte[] buffer) throws IOException{
		this.getFileOutputStream().write(buffer);
	}
}
