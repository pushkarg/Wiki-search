
import org.apache.lucene.analysis.*;
//import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.miscellaneous.PatternAnalyzer;
import org.apache.lucene.analysis.snowball.*;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.en.*;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Date;

public class WikiSearcher{

	public void searchFiles(String queryStr) throws Exception{
		String indexDir = "Folder_Index";
    	String[] field = {"contents","title","Exacttitle","Bold","BoldAndItalic","Summary","Italic","Category_Links"};

    	IndexReader reader = IndexReader.open(FSDirectory.open(new File(indexDir)));
    	IndexSearcher searcher = new IndexSearcher(reader);
    	
    	
    	//Analyzer analyzer = new SnowballAnalyzer(Version.LUCENE_35,"English");
    	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
    	//Analyzer analyzer = new PatternAnalyzer(Version.LUCENE_35);
    	//QueryParser parser = new QueryParser(Version.LUCENE_35, field, analyzer);
    	
    		
    	
    	MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_35, field, analyzer);
		//System.out.println("Query being searched : "+queryStr);
    	StringBuffer query_text = new StringBuffer(queryStr);
    	
    	int posn = query_text.indexOf(" ");
    	
    	if(posn>0){
    		query_text.insert(posn, "^6 ");
    		int posn2 = query_text.indexOf(" ", posn +4);
    		if(posn2>0){
    			query_text.insert(posn2, "^4 ");
    			int posn3 = query_text.indexOf(" ", posn2 +4);
        		if(posn3>0)
        			query_text.insert(posn3, "^2 ");
    		}
    	}
		Query query = parser.parse(query_text.toString());
		TopDocs hits = searcher.search(query, 30);

		//System.out.println("\nFound "+hits.totalHits +" documents :\n");

		ScoreDoc results[] = hits.scoreDocs;

		for(int i =0;i<results.length ; i++){
			Document doc = searcher.doc(results[i].doc);
			System.out.println(doc.get("Exacttitle"));
		}

	}
	public static void main(String args[]) throws Exception{
	  	//System.out.println("Enter the query : ");
    	//BufferedReader in =  new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		
		//String query = in.readLine();
		String query = args[0];

		WikiSearcher obj = new WikiSearcher();
		obj.searchFiles(query);
		//System.out.println("Query read : " + query);
	}
}

/*
Charles V, Holy Roman Emperor
Jean-Marie Le Pen
Category:Spanish Civil War
99 Luftballons
Ferdinand I, Holy Roman Emperor
Martyrs of the Spanish Civil War
Maya Plisetskaya
Spanish Civil War
*/