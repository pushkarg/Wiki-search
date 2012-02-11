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

public class Indexer{

	private IndexWriter writer;

	Indexer(String path) throws IOException{
		Directory dir ;
		dir = FSDirectory.open(new File(path) );
	    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);

	    IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_31, analyzer);

		writer = new IndexWriter( dir, iwc);
	}

	//void indexFiles(String path) throws FileNotFoundException , CorruptIndexException{
	int indexFiles(String path) throws IOException{
		File[] files = new File(path).listFiles();
		System.out.println("Indexing files : ");
		for(int i =0;i<files.length;i++){
			if( !files[i].isDirectory() && !files[i].isHidden() && files[i].canRead() ){
				if(files[i].getName().endsWith(".xml") || files[i].getName().endsWith(".txt") ){
					System.out.println(files[i]);
					Document indexDoc = new Document();
					Field field1 = new Field("contents", new FileReader(files[i]) );
					indexDoc.add(field1);

					Field field2 = new Field("filename" , files[i].getName(),Field.Store.YES, Field.Index.NOT_ANALYZED);
					indexDoc.add(field2);
					//Field field3 = new Field("lastModified" , files[i].lastModified() ,Field.Store.YES, Field.Index.NOT_ANALYZED);

					writer.addDocument(indexDoc);
				}
			}
		}
		
		return writer.numDocs();
	}

	public static void main(String args[]) throws Exception{
		String path = "files";
		Indexer obj = new Indexer("Folder_Index");
		int numOfDocs = obj.indexFiles(path);
		System.out.println("Number of files indexed : " +numOfDocs);
		obj.close();

	}
	public void close() throws Exception{
		writer.close();
	}
}
