import parser.*;
import utilities.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;


public class WikiIndexer{

	private IndexWriter writer;
	String index_dir="Folder_Index" , doc_dir;
	public WikiUtils utils_Obj;
	private XMLFileManager xml_files;
	private XMLJDomParser xml_parser;
	final int NUM_OF_PAGES = 10;
	public Page page;

	private void initializeIndexWriter() throws IOException {
		Directory dir ;
		dir = FSDirectory.open(new File(getIndexDirectory()) );
	    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
	    IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_31, analyzer);
		writer = new IndexWriter( dir, iwc);
	}

	public void setIndexDirectory(String path){
		this.index_dir = path;
	}

	public String getIndexDirectory(){
		return this.index_dir;
	}

	public void setDocumentDirectory(String path){
		this.doc_dir = path;
	}

	public String getDocumentDirectory(){
		return this.doc_dir;
	}

	WikiIndexer(String path) throws IOException{
		//Intitialize all the objects

		//Initializing WikiUtils obj
		utils_Obj = new WikiUtils();
		if(utils_Obj.validateFolder(path) !=0)
		{
			System.out.println("Invalid folder . Exiting");
			System.exit(1);
		}
		setDocumentDirectory(path);

		//Initializing XMLFileManager 
		//xml_files = new XMLFileManager( path );
		xml_files = new XMLFileManager();
		
		//Initializing Index Writer.
	 	initializeIndexWriter();

		page = new Page();
        page.setFlag(1);

		String file_name = "data/enwiki-20120104-pages-meta-current1.xml-p000000010p000010000";
		xml_parser= new XMLJDomParser(file_name) ;
	
	}

	public int indexFiles() throws Exception{
		
       
/*
        while(newPage.getFlag()==1)
        {
            //System.out.println(newPage.getTitle());
            System.out.println(newPage.getContent());
           
            try {
                newPage=xml_parser.getPageData();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

		}
*/
            
		//for(int i=0; page.getFlag() == 1 ;i++){
        while(page.getFlag()==1){
			//page = xml_parser.getPageObj(page);
            page=xml_parser.getPageData();

			if( page.getFlag() == 0)
				break;
			
			Document indexDoc = new Document();
			//System.out.println( page.getContent().toString());
			Field field1 = new Field("contents", page.getContent().toString() , Field.Store.YES, Field.Index.ANALYZED);
			indexDoc.add(field1);

			Field field2 = new Field("title" ,  page.getTitle().toString() ,Field.Store.YES, Field.Index.ANALYZED);
			indexDoc.add(field2);

			writer.addDocument(indexDoc);
		//}
		}

		return writer.numDocs();
	}

	public static void main(String args[]) throws Exception{
    	String usage = "java WikiIndexer [-docs DOCS_PATH]\n\n";

		if(args.length!=2 ){
			System.out.println("Usage : "+usage);
      		System.exit(1);
		}
		else{
			if(!args[0].equals("-docs") || args[1] == ""){
				System.out.println("Usage : "+usage);
      			System.exit(1);
			}
		}
    Date start = new Date();

		String index_path = args[1];
		WikiIndexer indexer_obj = new WikiIndexer(index_path);

		int numOfDocs = indexer_obj.indexFiles();
		System.out.println("Number of files indexed : " +numOfDocs);
		indexer_obj.close();
      Date end = new Date();
      System.out.println(end.getTime() - start.getTime() + " total milliseconds");

	}

	public void close() throws Exception{
		writer.close();
	}
}
