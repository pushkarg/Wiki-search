import java.io.*;
import java.net.*;

import org.apache.lucene.analysis.*;
//import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
//import org.apache.lucene.analysis.miscellaneous.PatternAnalyzer;
//import org.apache.lucene.analysis.snowball.*;
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
//import org.apache.lucene.analysis.en.*;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Date;


public class LuceneServer {

	int number_of_clients;
	final int SERVER_PORT=5000;
    ServerSocket Server = null;
	String descriptionsArray[];

	//This is required to send the entire Html
	public void establishInitialConnection() throws FileNotFoundException , IOException{
		String line_in_file, full_html = "";
		FileReader html_file = new FileReader("page.html"); 
		BufferedReader html_file_br = new BufferedReader(html_file);

		while( (line_in_file=html_file_br.readLine() ) != null){
			full_html=  full_html + line_in_file + "\n";
		}

        ServerSocket Server;
		try{
        	Server = new ServerSocket (SERVER_PORT);
			Server.setReuseAddress(true);
		}catch(Exception e){
			System.out.println("Issue with close connection ?? !!!!!!!!! " + e.toString() +"\n\n" );
			return;
		}

        Socket connection = Server.accept();
		connection.setSoLinger(false, 0);
		PrintWriter outToClient = new PrintWriter(connection.getOutputStream(),true);
		String header="HTTP/1.1: 200 OK\r\n"+"Content-Type: text/html\r\n" + "\r\n";
 		outToClient.println(header +  full_html);
		connection.shutdownOutput();
		outToClient.close();
		connection.close();
		if(!connection.isClosed() ) {
			System.out.println("Cannot close the connection and create another 1 ");
			return;
		}

		System.out.println("Sent the html ");
		Server.close();
		return;
	}


	public void processQueries() throws   IOException {
		try{
        	Server = new ServerSocket (SERVER_PORT);
			Server.setReuseAddress(true);
		}catch(Exception e){
			System.out.println("Issue with close connection ?? !!!!!!!!! " + e.toString() +"\n\n" );
			return;
		}
		int c=0;
		while(true){
			System.out.println("Waiting for incoming Query terms ");
			Socket clientSocket = null;
			try{
        		clientSocket = Server.accept();
				Thread.currentThread().sleep(300);
			 	InputStream input_stream= clientSocket.getInputStream();
			 	OutputStream output_stream= clientSocket.getOutputStream();
				int num_of_chars = input_stream.available();
				char input_char_seq[] = new char[num_of_chars];
				for(int i=0;i<input_stream.available() ; i++){
					input_char_seq[i] = (char) input_stream.read();
				}
				String client_string = new String(input_char_seq);

				int position_of_question =client_string.indexOf( '?' ,  client_string.indexOf("POST") );
				if(client_string.length() == 0 || position_of_question <=0 ){
					System.out.println("Comin here : "+ client_string );
				}else{
					String query_params = client_string.substring(position_of_question+1,client_string.indexOf("&param=EOS"));
					String  params[] = query_params.split("=") ;
					String query = params[1];
					PrintWriter outputChannel = new PrintWriter( output_stream ,true);
					System.out.println("\n\n comin here .. query  : " + query);
					String results[] = searchFiles(query, 0);

					//for(int list=0;list<10;list++)
 					outputChannel.println( createHtml(results) );

					output_stream.flush();

					output_stream.close();
					input_stream.close();
					clientSocket.close();
				}
				//System.out.println("Query param : " + query_params );
			}catch(Exception e){
				System.out.println("Cannot accept connections . Cause : "+ e.toString());
			}
	   }
	}

	public String createLink(String link ){
		return "<a href=\"http://en.wikipedia.org/wiki/" +link +"\">"+link+"</a>";	
	}

	public String createLiTag(String result){
		String liTag = "<li class=\"resultLi\" target=\"_blank\">" +createLink(result) +"</li>";
		return liTag;
	}
	public String createHtml(String results[] ){
		String final_html = "<ul id=\"resultsUl\"> ";
		for(int i=0;i<results.length  && i< 10;i++){
			final_html+=createLiTag(results[i] );
		}
		final_html+="</ul>";
		return final_html;
	}

	public void startServer(){
		
		//Send Html for all the clients
		try{
			establishInitialConnection();
		}catch(FileNotFoundException e){
			System.out.println("Cannot establish initial connection. FileNotFoundException");
		}catch(IOException e){
			System.out.println("Cannot establish initial connection. IOException" + e.toString());
		}

		System.out.println("Initial connection setup. Waiting for Query terms now ");
		//Establish Persistent connection with all the clients 
		try{
			processQueries();
		}catch(FileNotFoundException e){
			System.out.println("Could not establish persistent connection. FileNotFoundException");
		}catch(IOException e){
			System.out.println("Could not establish persistent connection. IOException");
		}	

	}

	public LuceneServer(){
		descriptionsArray = new String[100];
	}
	
	public String[] searchFiles(String queryStr, int pageNum) throws Exception{
		System.out.println("query ; " + queryStr);
		String indexDir = "Folder_Index";
    	String[] field = {"contents","title","Exacttitle","Bold","Summary","Category_Links"};

    	IndexReader reader = IndexReader.open(FSDirectory.open(new File(indexDir)));
    	IndexSearcher searcher = new IndexSearcher(reader);
    	
    	Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
    	
    	MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_35, field, analyzer);
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

		ScoreDoc results[] = hits.scoreDocs;
		
		String resultArr[] = new String[100];
		for(int i =0;i<results.length && i<10 ; i++){
			Document doc = searcher.doc(results[i].doc);
			descriptionsArray[i]="";
			resultArr[i] = doc.get("Exacttitle");
			String description = doc.get

			//System.out.println("resultArr : " + resultArr[i] );
		}
		return resultArr;
	}

	public static void main(String args[]){

		LuceneServer collab_doc_server = new LuceneServer();
		System.out.println("Starting the server.");
		collab_doc_server.startServer();
	
	}
}
