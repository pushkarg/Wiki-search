package parser;
import java.util.regex.*;
import datatypes.*;
import utilities.WikiConstants;


public class  WikiContentParser{
	StringBuffer content_text;
	String content_raw;

	//Metadata - 
	final int NUM_OF_REF_URLS = 500;
	final int NUM_OF_BOLD_AND_ITALIC = 500;

	public WikiContentParser(StringBuffer text){
		content_text = text;
		content_raw = text.toString();
	}

	public StringBuffer getContentText(){
		 return content_text;
	}

	public String getContentTextRaw(){
		 return content_raw;
	}

	public void loadBufferWithRawContent(StringBuffer raw_text ){
		content_text = raw_text;
	}

	/*
		This extracts the <ref></ref> tags from the content and adds them to the ref_tags array
	*/
	public WikiUrl[] extractRefTagsFromContent(WikiUrl []ref_tags){
		String tag_list[] = new String[NUM_OF_REF_URLS];	
		int current_string_count = 0;
		boolean ref_tags_exist = true;
		int ref_index = 0;
		WikiUrl []temp_url_list = new WikiUrl[NUM_OF_REF_URLS];

		//Extract strings with the pattern given below
		Pattern p = Pattern.compile("<ref[^<]*</ref>", Pattern.MULTILINE);
		Matcher m = p.matcher(content_text);

		while(m.find() ){
			tag_list[ref_index] = new String( m.group() );
			ref_index++;
		}

		temp_url_list=new WikiUrl[ref_index];

		//Replace the <ref> tags with blank strings in the main string, once they have been extracted
		for(int i=0;i<temp_url_list.length ; i++){
			 int start_index = content_text.indexOf(tag_list[i].toString());
			 content_text.replace(start_index , start_index + tag_list[i].length() , "");
		}


		//Extract Urls
		Pattern url_pattern = Pattern.compile("http://[a-z.]*[com|org|net][^ ]*");
		int num_of_valid_urls=0;
		for(int i=0;i<ref_index;i++){
			Matcher url_match = url_pattern.matcher(tag_list[i]);
			if(url_match.find()){
				temp_url_list[num_of_valid_urls] = new WikiUrl(url_match.group(0));
				num_of_valid_urls++;
				//Extract and add the Url title if required later !!
			}
		}

		ref_tags = new WikiUrl[num_of_valid_urls];
		for(int i=0;i<ref_tags.length;i++)
			ref_tags[i] = temp_url_list[i];

		return ref_tags;
	}

	public WikiPhrase[] extractBoldAndItalicText(){
		WikiPhrase []bold_and_italic= new WikiPhrase[NUM_OF_BOLD_AND_ITALIC];
		
		int phrase_index=0;
		//Extract strings with the pattern given below
		Pattern p = Pattern.compile("'''''[a-zA-Z0-9,.!:?\" ]*'''''", Pattern.MULTILINE);
		Matcher m = p.matcher(content_text);

		while(m.find() ){
			String phrase = m.group();
			int index = getContentTextRaw().indexOf( phrase );

			//Remove the ''''' from the beginning and end.
			String extracted_phrase = phrase.substring( 5, phrase.length() - 5 );
			int text_type = WikiConstants.BOLD_AND_ITALICS;
			bold_and_italic[phrase_index] = new WikiPhrase( extracted_phrase , index , text_type );
			phrase_index++;
		}

		if(phrase_index ==0){
			WikiPhrase return_array[] = new WikiPhrase[0];
			return  return_array;
		}

		/*
		for(int i =0;i<phrase_index;i++){
			int start_index = getContentText().toString().indexOf("'''''"+bold_and_italic[i] + "'''''");
			int end_index = start_index + bold_and_italic[i].getPhrase().length() ;
			System.out.println("start index " + start_index + " , end : " + end_index );
			//getContentText().replace(start_index , end_index , "" );	
		}
		*/

		//The Bold_and_italic array has extra, blank objects. Return only the valid Phrases
		WikiPhrase return_array[] = new WikiPhrase[ phrase_index];
		for(int i =0;i<phrase_index;i++)
			return_array[i] = bold_and_italic[i];

		return return_array;
	}

	/*
	public WikiPhrase[] extractBoldText(){
	}

	public WikiPhrase[] extractItalicText(){
	}
	*/
}
