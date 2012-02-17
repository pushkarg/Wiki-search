package parser;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import utilities.Page;



public class XMLJDomParser {
	
		String file_path;
		Document document;
		Vector <StringBuffer> title;
		Vector <StringBuffer> text;
		Vector <StringBuffer> timestamp;
		Vector <Boolean> redirect;
		
		int tot_page_count, curr_page_count;
		static int cummulativePageCount=0;
		
		
		
        public XMLJDomParser(String src_file) {
			super();
            SAXBuilder builder = new SAXBuilder();
        //    String filePath = "xmls/Anthropology.xml";
            file_path = src_file; 
            File source_xml = new File(file_path);
            
            System.out.println("The file: \""+src_file+"\" is getting parsed...\n");
            
            try {
				document = (Document) builder.build(source_xml);
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            Element root = document.getRootElement();
            
            List totalRow = root.getChildren("page" , root.getNamespace() );
            System.out.println("total Pages : "+ totalRow.size() );
            
            tot_page_count=totalRow.size();
            curr_page_count=tot_page_count;
            // keeps track of the total number of pages
            cummulativePageCount+= tot_page_count;
            
            title = new Vector<StringBuffer>(tot_page_count);
            text = new Vector<StringBuffer>(tot_page_count);
            timestamp = new Vector<StringBuffer>(tot_page_count);
            redirect = new Vector<Boolean>(tot_page_count);
            
            int j=0;
            for (int i = 0; i < tot_page_count; i++)
            {
                    Element frameWork = (Element) totalRow.get(i);
                    StringBuffer temp1 = new StringBuffer(frameWork.getChildText("title", root.getNamespace()));
                    
                    if (frameWork.getChild("redirect",root.getNamespace()) != null)
                           	//System.out.println(frameWork.getChild("redirect",root.getNamespace()).getName()+j++);
                    	redirect.addElement(true);
                    else
                    	redirect.addElement(false);
                    	
                    	
                    
                    //System.out.println("Gautham"+temp1+"Gautham");
                    Element revision = frameWork.getChild("revision",root.getNamespace());
                    StringBuffer temp2 = new StringBuffer(revision.getChildText("text", root.getNamespace()));
                    StringBuffer temp3 = new StringBuffer(revision.getChildText("timestamp", root.getNamespace()));
                    title.addElement(temp1 );
                    text.addElement(temp2 );
                    timestamp.addElement(temp2 );
                    
            }
			
		}



		public int getPageData(Page newPage) throws Exception, IOException
        {
								
				newPage.setFlag(0);
				
				if (curr_page_count>0)
				{
					newPage.setContent(text.get(curr_page_count-1));
					newPage.setTitle(title.get(curr_page_count-1));
					newPage.setRedirect(redirect.get(curr_page_count - 1));
					newPage.setTimestamp(timestamp.get(curr_page_count - 1));
					newPage.setFlag(1);
					curr_page_count--;
					return 1;
				}

				return 0;//There are no pages remaining
				                
        }
		
		public int returnArticlecount()
		{
		
			return cummulativePageCount;
		}
		

}

