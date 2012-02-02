
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
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

public class Searcher{

	public void searchFiles(String queryStr) throws Exception{
		String indexDir = "Folder_Index";
    	String field = "contents";

    	IndexReader reader = IndexReader.open(FSDirectory.open(new File(indexDir)));
    	IndexSearcher searcher = new IndexSearcher(reader);

    	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
    	QueryParser parser = new QueryParser(Version.LUCENE_31, field, analyzer);
		
		//System.out.println("Query being searched : "+queryStr);
		Query query = parser.parse(queryStr);
		TopDocs hits = searcher.search(query, 10);

		System.out.println("\nFound "+hits.totalHits +" documents :\n");

		ScoreDoc results[] = hits.scoreDocs;

		for(int i =0;i<results.length ; i++){
			Document doc = searcher.doc(results[i].doc);
			System.out.println(doc.get("title"));
		}

	}
	public static void main(String args[]) throws Exception{
	  	System.out.println("Enter the query : ");
    	BufferedReader in =  new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		String query = in.readLine();

		Searcher obj = new Searcher();
		obj.searchFiles(query);
		//System.out.println("Query read : " + query);
	}
}

