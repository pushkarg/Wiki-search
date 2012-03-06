import parser.*;
import utilities.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DefaultSimilarity;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
//import org.apache.lucene.analysis.snowball.*;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Date;




public class WikiIndexer{

	private IndexWriter writer;
	String index_dir="Folder_Index" , doc_dir;
	public WikiUtils utils_Obj;
	private XMLFileManager xml_files;
	private static XMLJDomParser xml_parser;
	final int NUM_OF_PAGES = 10;
	public Page page;


	public String stemmer(Analyzer analyzer, String data){
		
		StringReader query_reader = new StringReader(data);
    	
    	TokenStream tstream = analyzer.tokenStream("contents", query_reader);
    	TermAttribute term = tstream.addAttribute(TermAttribute.class);

    	StringBuffer result = new StringBuffer();
        try {
            while (tstream.incrementToken()){
                result.append(term.term());
                result.append(" ");
            }
        } catch (IOException ioe){
            System.out.println("Error: "+ioe.getMessage());
        }
    
        if (result.length()==0)
        	result.append(data);	
        
        return result.toString();
	}
	
	private void initializeIndexWriter() throws IOException {
		Directory dir ;
		dir = FSDirectory.open(new File(getIndexDirectory()) );
	    //Analyzer analyzer = new SnowballAnalyzer(Version.LUCENE_35,"English");
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
	    IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35, analyzer);
	   // Similarity sim = new MySim();
	   // writer.setSimilarity(sim);
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
		xml_files = new XMLFileManager(path);
		
		//Initializing Index Writer.
	 	initializeIndexWriter();

		page = new Page();
        page.setFlag(1);
	
	}

	public Document addFields(Document indexDoc , Page page){
		//Analyzer analyzer= new SnowballAnalyzer(Version.LUCENE_35, "English");
		//Analyzer analyzer= new StandardAnalyzer(Version.LUCENE_35);
		
		
		Field field1 = new Field("contents", /*stemmer(analyzer, */page.getContent().toString() , Field.Store.NO, Field.Index.ANALYZED);
		indexDoc.add(field1);
		//field1.setBoost(0.87F);

		Field field2 = new Field("title" , /*stemmer(analyzer,*/ page.getTitle().toString()  ,Field.Store.NO, Field.Index.ANALYZED);
		indexDoc.add(field2);
		field2.setBoost(1.5F); // The title needs to have a higher weight than other fields.

		Field field3 = new Field("Exacttitle" , page.getTitle().toString() ,Field.Store.YES, Field.Index.NOT_ANALYZED);
		indexDoc.add(field3);
		//field3.setBoost(6F);
		
		//System.out.println(stemmer(analyzer, page.getTitle().toString())+"Gautham\n");
		//Field field4 = new Field("Summary" ,  page.getSummary_text().toString() ,Field.Store.YES, Field.Index.ANALYZED);
		//field4.setBoost(1.3F);
		//indexDoc.add(field4);
		//System.out.println("\nTitle : " + page.getTitle().toString() + "\nSummary : " + page.getSummary_text().toString() );
		
	/*	
		Field field5;
		for(int i=0;i<page.getBold_text().size();i++){
			field5 = new Field("Bold" ,  page.getBold_text().elementAt(i).getPhrase() ,Field.Store.NO, Field.Index.ANALYZED);
			indexDoc.add(field5);
		//	field5.setBoost(1.2F);
		}
		
		Field field6;
		for(int i=0;i<page.getBold_and_italic_text().size();i++){
			field6 = new Field("BoldAndItalic" ,  page.getBold_and_italic_text().elementAt(i).getPhrase() ,Field.Store.NO, Field.Index.ANALYZED);
			indexDoc.add(field6);
		//	field6.setBoost(1.5F);
		}
		
		/*
		Field field7;
		for(int i=0;i<page.getItalic_text().size();i++){
			field7 = new Field("Italic" ,  page.getItalic_text().elementAt(i).getPhrase() ,Field.Store.NO, Field.Index.ANALYZED);
			indexDoc.add(field7);
		//	field7.setBoost(1.2F);
		}
		*/

		// CITATIONS ANALYSIS
		
		
		if(page.getNumRefUrls()>200)
			indexDoc.setBoost(1.8F);
		else if(page.getNumRefUrls()>150)
			indexDoc.setBoost(1.6F);
		else if(page.getNumRefUrls()>125)
			indexDoc.setBoost(1.5F);
		else if(page.getNumRefUrls()>100)
			indexDoc.setBoost(1.3F);
		else if(page.getNumRefUrls()>75)
			indexDoc.setBoost(1.2F);
		else if(page.getNumRefUrls()>50)
			indexDoc.setBoost(1.1F);
			/*
		else if (page.getNumRefUrls()>25)
			indexDoc.setBoost(1.3F);
		else if (page.getNumRefUrls()>5)
			indexDoc.setBoost(1.1F);
			*/

		// OUT LINK ANALYSIS
		
		int out_link_count = page.getWikiLinkvctr().getOutPutLink().size();

		if(out_link_count>180)
			indexDoc.setBoost(1.6F);
		else if(out_link_count>150)
			indexDoc.setBoost(1.3F);
		else if(out_link_count>100)
			indexDoc.setBoost(1.2F);
		else if(out_link_count>70)
			indexDoc.setBoost(1.1F);
		/*else if(out_link_count>50)
			indexDoc.setBoost(1.2F);
		else if(out_link_count>30)
			indexDoc.setBoost(1.6F);
		else if(out_link_count>10)
			indexDoc.setBoost(1.2F); */
		
		// CATEGORY LINKS
		
		/*Field field8;
		for(int i=0;i<page.getWikiLinkvctr().getCategoryLink().size();i++){
			//System.out.println(page.getWikiLinkvctr().getCategoryLink().elementAt(i));
			field8 = new Field("Category_Links" ,  page.getWikiLinkvctr().getCategoryLink().elementAt(i) ,Field.Store.NO, Field.Index.ANALYZED);
			indexDoc.add(field8);
			//field8.setBoost(1.5F);
		}	*/
		
		//System.out.println("\n\nTitle : "+page.getTitle().toString() + "\nContent Length : "+page.getContent().toString().length() + "\nNum of Ref urls : " + page.getNumRefUrls() + "\nOutlink count : "+out_link_count );		

		// CATEGORY DEPRECIATION
		//System.out.println(page.getNumRefUrls()+"Gautham");
		if( page.getTitle().toString().contains("Talk:"))
			indexDoc.setBoost(0.01F);
		else if( page.getTitle().toString().contains("Category:") )
			indexDoc.setBoost(0.78F);
		else if( page.getTitle().toString().contains("User:") )
			indexDoc.setBoost(0.82F);
		else if(page.getTitle().toString().contains("File:") && page.getTitle().toString().contains(".svg")  )
			indexDoc.setBoost(1.42F);
		else if(page.getTitle().toString().contains("File:") )
			indexDoc.setBoost(0.82F);
		else if(page.getTitle().toString().contains("Wikipedia:") )
			indexDoc.setBoost(0.82F);
		else if(page.getTitle().toString().contains("Template:") )
			indexDoc.setBoost(0.01F);
		return indexDoc;
	}
 
	public int indexFiles(Date start) throws Exception{
       	
		int i=0;
		while(xml_files.filesExist()){
			
       		String file_name = xml_files.returnFileName();
			xml_parser= new XMLJDomParser(file_name) ;
			while( xml_parser.getPageData(page) == 1 ){
			
				int type = page.getPageType();
				Document indexDoc = new Document();
				addFields(indexDoc, page);
				//TODO need to have some sort of check here, same document should be indexed only once
				// In our case since the documents are static, the counter should always remain the same.
				writer.addDocument(indexDoc);

				//System.out.println("Content : " +page.getContent() );
				page.resetPage();
			}
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

		String documentPath = args[1];
		//System.out.println(documentPath);
		WikiIndexer indexer_obj = new WikiIndexer(documentPath);

		int numOfDocs = 0;
		numOfDocs = indexer_obj.indexFiles(start);
		
		
		System.out.println("Total Number of articles Parsed : " + xml_parser.returnArticlecount());
		// The article indexed in not correct, we need to figure out something for the one below,
		System.out.println("Total Number of articles indexed : " +numOfDocs);
		indexer_obj.close();
      	Date end = new Date();
      	System.out.println(end.getTime() - start.getTime() + " total milliseconds");

	}

	public void close() throws Exception{
		writer.close();
	}
}
