package utilities;

import java.io.*;
import java.util.*;

import com.ximpleware.*;

public class XMLFileManager {
	
	Vector<String> 	splitFileName = new Vector<String>();
	String srcFileName= null;
	int file_count = 0;
	Iterator<String> itr;
	public XMLFileManager(String fileName)
	{
		srcFileName = "/home/maverick/irproject/wikidump/enwiki.xml";
		try{
			System.out.println("Coming to constructor ");
			// Check for the Filesize
			File file = new File(srcFileName);
			
			System.out.println("File Size" + file.length());
			//TODO Only if the file size if more than the threshold value is it made to split.	Need to decide
			// on optimum size
			if(file.length() == 204800)
			{
				splitFiles();
				itr = splitFileName.iterator();
			}
		}catch(Exception e){
		}
	}
					
     public void splitFiles() throws XPathEvalException, NavException, IOException, XPathParseException
     {
        VTDGen vg = new VTDGen();
        if (vg.parseFile(srcFileName, true)){
				System.out.println("Coming to splitFIles ");
                VTDNav vn = vg.getNav();
                AutoPilot ap = new AutoPilot(vn);

				ap.selectXPath("/mediawiki/page");
                int i=0,k=0;
                             
                int array[] = new int[10];
                byte[] ba = vn.getXML().getBytes();
                int count =0;
                
                FileOutputStream fos = new FileOutputStream("/home/maverick/irproject/temp/out"+count+".xml"); 
                // FileName
                String fileName;
                splitFileName.add("out0.xml");
                while((i=ap.evalXPath())!=-1)
                {	   
                	array[count] =-1;
                	if(k == 0)
                	{
                		fos.write("<mediawiki>".getBytes());
                	}
            		long l = vn.getElementFragment();
            		fos.write(ba, (int)l, (int)(l>>32));
                    k++;
                    if(k == 3000)
                    {
                    	fos.write("</mediawiki>".getBytes());
                    	fos.close();
                    	array[count] = 0; //the file perfectly formulated
                    	count= count +1;
                    	k = 0;
                    	// open a new one for write 
                    	fos = new FileOutputStream("/home/maverick/irproject/temp/out"+count+".xml");
                    	fileName = "out"+count +".xml";
                    	splitFileName.add(fileName);
                    }
                }
                for (int j=0; j<=count; j++)
                {
                	if(array[j]== -1)
                	{
                		fos.write("</mediawiki>".getBytes());
                	}
                }
                fos.close();
        } 
		//file_count = count + 1;

    }

	public boolean filesExist(){
		System.out.println("File count = " +file_count + " SplitFileName.size : " +  splitFileName.size()  );
		if(file_count < splitFileName.size() )
			return true;
		else 
			return false;
	}
     
     public String returnFileName()
     {
	       	 
	       	 String result = null;
	       	 if(itr.hasNext())
	       	 {
	       		result =  itr.next().toString();
				file_count++;
	       	 }
			return result;
     }
     
	 /*
     public static void main(String[] argv) throws XPathParseException, XPathEvalException, NavException, IOException
     {
    	 XMLFileManager mangerObj = new XMLFileManager("/home/maverick/irproject/wikidump/enwiki.xml");
    	 mangerObj.splitFiles();
    	 Iterator<String> itr = mangerObj.splitFileName.iterator();
    	 while(itr.hasNext())
    	 {
    		 System.out.println(itr.next());
    	 }
     }
	 */
}
