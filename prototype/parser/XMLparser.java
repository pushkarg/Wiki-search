package parser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XMLparser {

	public String[] getFrameWorkDetails() throws Exception, IOException
	{
	 
		SAXBuilder builder = new SAXBuilder();
		String filePath = "xmls/Anthropology.xml";
	
		Document document = (Document) builder.build(new File(filePath));
		Element root = document.getRootElement();
		List totalRow = root.getChildren("page" , root.getNamespace() );
		System.out.println("total row : "+ totalRow.size() );
		System.out.println("title:" + "id");

		String data[]  = new String[10];
		for (int i = 0; i < totalRow.size(); i++)
		{
			Element frameWork = (Element) totalRow.get(i);
			//System.out.println(frameWork.getChildText("title" ,  root.getNamespace() ) + "-" + frameWork.getChildText("id",root.getNamespace()));
			//System.out.println("More details : ");
			List revision = frameWork.getChildren("revision",  root.getNamespace() );
			Element child = (Element)revision.get(i);
			//System.out.println(" comments : " +child.getChildText("comment",  root.getNamespace() ) );
			//System.out.println(child.getChildText("text",  root.getNamespace() ) );
			data[0] =  child.getChildText("text",  root.getNamespace() ) ;
			data[1] = child.getChildText("comment",  root.getNamespace() );
			data[2] = frameWork.getChildText("title",  root.getNamespace() );
		}
		return data;
	}
	
	/*
	public static void main(String argS[])
	{
			XMLparser readXML = new XMLparser();
			readXML.getFrameWorkDetails();
	}
	*/
	
}
