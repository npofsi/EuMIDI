package pro.npofsi.wrap.eumidi.comps;

import android.util.Xml;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class FileParsers
{
	public static Document parserXmlToDom(InputStream is) throws ParserConfigurationException, IOException, SAXException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		//获得Document对象
		return builder.parse(is);
	}
	
	//public static  parserJson()
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
