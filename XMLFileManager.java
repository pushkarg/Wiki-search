import java.io.*;
import java.util.*;

import com.ximpleware.*;

public class XMLFileManager {
	
	Vector<String> 	spitFileName = new Vector<String>();				
     public void splitFiles() throws XPathEvalException, NavException, IOException, XPathParseException
     {
        VTDGen vg = new VTDGen();
        if (vg.parseFile("/home/maverick/irproject/wikidump/enwiki.xml", true)){
                VTDNav vn = vg.getNav();
                AutoPilot ap = new AutoPilot(vn);

				ap.selectXPath("/mediawiki/page");
                int i=0,k=0;
                byte[] ba = vn.getXML().getBytes();
                int count =0;
                
                FileOutputStream fos = new FileOutputStream("/home/maverick/irproject/splitfiles/out"+count+".xml"); 
                // FileName
                String fileName;
                spitFileName.add("out0.xml");
                while((i=ap.evalXPath())!=-1)
                {	   
                	//fos.write("".getBytes());
                    long l = vn.getElementFragment();
                    fos.write(ba, (int)l, (int)(l>>32));
                    //fos.write("".getBytes());
                    k++;
                    if(k == 3000)
                    {
                    	fos.close();
                    	count= count +1;
                    	k = 0;
                    	// open a new one for write 
                    	fos = new FileOutputStream("/home/maverick/irproject/splitfiles/out"+count+".xml");
                    	fileName = "out"+count +".xml";
                    	spitFileName.add(fileName);
                    }
                }
                fos.close();
        }               
    }
     
     public static void main(String[] argv) throws XPathParseException, XPathEvalException, NavException, IOException
     {
    	 XMLFileManager mangerObj = new XMLFileManager();
    	 mangerObj.splitFiles();
    	 Iterator<String> itr = mangerObj.spitFileName.iterator();
    	 while(itr.hasNext())
    	 {
    		 System.out.println(itr.next());
    	 }
     }
}
