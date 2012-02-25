package utilities;

import java.io.*;
import java.io.File;
import java.util.*;

import com.ximpleware.*;
//import java.nio.*;

public class XMLFileManager {
	
	Vector<String> 	splitFileName = new Vector<String>();
	Vector<String>  nonSplitFileName = new Vector<String>();
	String srcFileName= null;
	int file_count = 0, file_count_nonSplitFiles=0;
	Iterator<String> itr;
	Iterator<String> itr2;
	boolean alreadyRead = false;
	String fullFilePath= null;
	public XMLFileManager(String fileName)
	{
		
		//srcFileName = fileName;
		srcFileName = fileName;
		//"/home/maverick/workspace/milestonedump/";
		
		//srcFileName = "/home/maverick/irproject/wikidump/enwiki.xml";
		try{
			System.out.println("Coming to constructor ");
			// Check for the Filesize
			File file = new File(srcFileName);
			String files[] = file.list();
			
			
			// loop through each file
			for(int i=0; i<files.length; i++)
			{
				//System.out.println("File Size" + files[i].length());
				//TODO Only if the file size if more than the threshold value is it made to split.	Need to decide
				// on optimum size
				fullFilePath = srcFileName + files[i];
				File temp = new File(fullFilePath);
				System.out.println("File Size" + temp.length());
				if(temp.length() >= 20480000)
				{
					try
					{
						splitFiles();
					}
					catch(Exception e)
					{
						System.out.println(e.toString());
					}
					itr = splitFileName.iterator();
				}
				else
				{
					nonSplitFileName.add(files[i].toString());
					itr2 = nonSplitFileName.iterator();
				}
			}
			//System.out.println("i am here");
		}catch(Exception e){
		}
	}
					
     public void splitFiles() throws XPathEvalException, NavException, IOException, XPathParseException
     {
        VTDGen vg = new VTDGen();
        if (vg.parseFile(fullFilePath, true)){
				System.out.println("Coming to splitFIles ");
                VTDNav vn = vg.getNav();
                AutoPilot ap = new AutoPilot(vn);

				ap.selectXPath("/mediawiki/page");
                int i=0,k=0;
                             
                int array[] = new int[1000];
                byte[] ba = vn.getXML().getBytes();
                int count =0;
                
                FileOutputStream fos = new FileOutputStream("temp/"+ "out" +count+".xml"); 
                // FileName
                String fileName;
                splitFileName.add("out0.xml".toString());
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
                    	fos = new FileOutputStream("temp/"+"out"+count+".xml");
                    	fileName = "out"+ count +".xml";
                    	splitFileName.add(fileName.toString());
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
		
		boolean result = false;
		if(splitFileName.size()>0)
		{
			System.out.println("File count = " +file_count + " SplitFileName.size : " +  splitFileName.size()  );

			if(file_count < splitFileName.size() )
				result = true;
		}
		else if(nonSplitFileName.size()>0 && file_count_nonSplitFiles < nonSplitFileName.size())// file is not split, so, just returning true as we already know that the file exists
		{
			result = true;
			//alreadyRead = true; //this done, so that the file is read only once
		}
		return result;
	}
     
     public String returnFileName()
     {
    	 String result = null;
    	 if(itr2!=null && itr2.hasNext())
    	 {
    		 result = itr2.next().toString();
    		 file_count_nonSplitFiles++;
    		 result =srcFileName +result;

    	 }
    	 else
    	 {
	       	 if(itr!=null && itr.hasNext())
	       	 {
	       		result =  itr.next().toString();
				file_count++;
				result = "temp/" +result;
	       	 }
    	 }
	       	 
		return result;
     }
}
