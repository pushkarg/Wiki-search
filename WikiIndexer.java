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
	String index_dir;
	public WikiUtils utils_Obj;
	private XMLFileManager xml_files;

	private void initializeIndexWriter(String path) throws IOException {
		Directory dir ;
		dir = FSDirectory.open(new File(path) );
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

	WikiIndexer(String path) throws IOException{
		//Intitialize all the objects

		//Initializing WikiUtils obj
		utils_Obj = new WikiUtils();
		if(utils_Obj.validateFolder(path) !=0)
		{
			System.out.println("Invalid folder . Exiting");
			System.exit(1);
		}
		setIndexDirectory(path);

		//Initializing XMLFileManager 
		//xml_files = new XMLFileManager( path );
		xml_files = new XMLFileManager();
		
		//Initializing Index Writer.
	 	initializeIndexWriter(path);
	}

	public int indexFiles(){
		return 1;
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

		String index_path = args[1];
		WikiIndexer indexer_obj = new WikiIndexer(index_path);

		int numOfDocs = indexer_obj.indexFiles();
		System.out.println("Number of files indexed : " +numOfDocs);
		indexer_obj.close();

	}

	public void close() throws Exception{
		writer.close();
	}
}
