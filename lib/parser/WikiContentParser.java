package parser;
import java.util.regex.*;
import java.util.Vector;
import datatypes.*;
import utilities.WikiConstants;


public class  WikiContentParser{
	StringBuffer content_text;
	String content_raw;

	//Metadata - 
	final int NUM_OF_REF_URLS = 500;
	final int NUM_OF_BOLD_AND_ITALIC = 500;

	public WikiContentParser(StringBuffer text){
		content_text = new StringBuffer(text);
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
	public Vector<WikiUrl>  extractRefTagsFromContent(Vector <WikiUrl> input){
		Vector <String> tag_list = new Vector <String>();	
		int current_string_count = 0;
		boolean ref_tags_exist = true;
		
		Vector <WikiUrl> temp_url_list = new Vector <WikiUrl> ();

		//Extract strings with the pattern given below
		Pattern p = Pattern.compile("<ref[^<]*</ref>", Pattern.MULTILINE);
		Matcher m = p.matcher(content_text);

		while(m.find() ){
			tag_list.add(m.group());
		}

		//temp_url_list=new WikiUrl[ref_index];

		//Replace the <ref> tags with blank strings in the main string, once they have been extracted
		for(int i=0;i<tag_list.size() ; i++){
			 int start_index = content_text.indexOf(tag_list.elementAt(i));
			 content_text.replace(start_index , start_index + tag_list.elementAt(i).length() , "");
		}


		//Extract Urls
		Pattern url_pattern = Pattern.compile("http://[a-z.]*[com|org|net|co][^ ]*");
		WikiUrl wiki;
		for(int i=0;i<tag_list.size() ;i++){
			Matcher url_match = url_pattern.matcher(tag_list.elementAt(i));
			if(url_match.find()){	
				wiki = new WikiUrl(url_match.group(0));
				temp_url_list.add(wiki);
				
				//Extract and add the Url title if required later !!
			}
		}


		return temp_url_list;
	}

	public Vector <WikiPhrase>  extractBoldAndItalicText(){
		Vector <WikiPhrase> bold_and_italic= new Vector<WikiPhrase>();
		WikiPhrase wikiphrase = new WikiPhrase();	
	
		//Extract strings with the pattern given below
		Pattern p = Pattern.compile("'''''[a-zA-Z0-9,.!:?\" ]*'''''", Pattern.MULTILINE);
		Matcher m = p.matcher(content_text);

		while(m.find() ){
			String phrase = m.group();
			
			//Remove the ''''' from the beginning and end.
			String extracted_phrase = phrase.substring( 5, phrase.length() - 5 );
			
			wikiphrase.setPhrase(extracted_phrase);
			wikiphrase.setPhrasePosition(getContentTextRaw().indexOf( phrase ));
			wikiphrase.setPhraseType(WikiConstants.BOLD_AND_ITALICS);
			
			bold_and_italic.add(wikiphrase);

		}

		
		while( content_text.indexOf("'''''") > 0 )
			content_text.replace( content_text.indexOf("'''''"), content_text.indexOf("'''''") + 5 , "");
		

		return bold_and_italic;
	}

	
	
	public Vector <WikiPhrase> extractBoldText(){
		Vector <WikiPhrase> bold= new Vector<WikiPhrase>();
		WikiPhrase wikiphrase = new WikiPhrase();
		
		
		//Extract strings with the pattern given below
		Pattern p = Pattern.compile("'''[a-zA-Z0-9,.!:?\" ]*'''", Pattern.MULTILINE);
		Matcher m = p.matcher(content_text);

		while(m.find() ){
			String phrase = m.group();
			
			//Remove the ''' from the beginning and end.
			String extracted_phrase = phrase.substring( 3, phrase.length() - 3 );
			
			wikiphrase.setPhrase(extracted_phrase);
			wikiphrase.setPhrasePosition(getContentTextRaw().indexOf( phrase ));
			wikiphrase.setPhraseType(WikiConstants.BOLD);
			
			bold.add(wikiphrase);
		}		
		while( content_text.indexOf("'''") > 0 )
			content_text.replace( content_text.indexOf("'''") , content_text.indexOf("'''") + 3 , "");
		
		return bold;
	}

	
	public Vector <WikiPhrase> extractItalicText(){
		Vector <WikiPhrase> italic = new Vector<WikiPhrase>();
		WikiPhrase wikiphrase = new WikiPhrase();
		
		
		//Extract strings with the pattern given below
		Pattern p = Pattern.compile("''[a-zA-Z0-9,.!:?\" ]*''", Pattern.MULTILINE);
		Matcher m = p.matcher(content_text);

		while(m.find() ){
			String phrase = m.group();
			int index = getContentTextRaw().indexOf( phrase );
			String extracted_phrase;
			
			//Remove the '' from the beginning and end.
			extracted_phrase = phrase.substring( 2, phrase.length() - 2 );
			
				
			wikiphrase.setPhrase(extracted_phrase);
			wikiphrase.setPhrasePosition(index); 
			wikiphrase.setPhraseType(WikiConstants.ITALICS);
			
			italic.add(wikiphrase);
		}		
		while( content_text.indexOf("''") > 0 )
			content_text.replace( content_text.indexOf("''") , content_text.indexOf("''") + 2 , "");
		
		return italic;
	}

    public WikiLinks ExtractOutLinks()
    {
            WikiLinks linkObj = new WikiLinks();
            //Vector<WikiLinks> linkArray = new Vector<WikiLinks>();// to store all
                                                                                                                            // the values
            //linkArray.add(linkObj);
            // Extract the string with the pattern given below, [[ ]]
            Pattern p = Pattern.compile("\\[\\[(.*?)\\]\\]");

            Matcher matcher = p.matcher(content_text.toString());
            while (matcher.find())
            {

                    String groupStr = matcher.group(1);

                    boolean  categoryFlag = groupStr.startsWith("Category:");
                    if(categoryFlag)
                    {
                            linkObj.AddCategoryLink(groupStr.substring(9));
                           // System.out.println("CategoryLink:" + groupStr.substring(9));
                    }
                    else
                    {
                            linkObj.AddLinkString(groupStr);
                            //System.out.println("Link:"+groupStr);
                    }

                    /*
                     * for(int i=0; i<matcher.groupCount(); i++) { String groupStr =
                     * matcher.group(i); System.out.println(groupStr); }
                     */
            };
            return linkObj;
    }

	
	public StringBuffer getSummaryText(){
		StringBuffer content = new StringBuffer(this.getContentText());
		boolean summary_found = false;
	
		try{
			//Pattern p = Pattern.compile("== [{ASCII}]* ==", Pattern.MULTILINE);
			//Pattern p = Pattern.compile("==[a-zA-Z0-9{}?.,'|\" ]*==[\r\n]", Pattern.MULTILINE);
			Pattern p = Pattern.compile("=?==[^=]*===?[\r\n]", Pattern.MULTILINE);
			Matcher m = p.matcher(content);

			String matched_phrase = "";
			if(m.find())
				matched_phrase = m.group();
			
			if(content.indexOf(matched_phrase) != 0)
				summary_found = true;
			content.replace( content.indexOf(matched_phrase) , content.length() , "");
		}catch(IllegalStateException e){
			System.out.println("Issue with Regular expression matching. Func: getSummaryText");
		}

		if( summary_found == true){
			//System.out.println("BEFORE removing the infobox : \n" + content);
			this.removeExtraStuff(content);	
			//System.out.println("AFTER removing the infobox : \n" + content +"\n\n\n\n\n");
		}
		
		//Remove [[]] links
		Pattern p_link = Pattern.compile("\\[\\[(.*?)\\]\\]");
		Matcher matcher_link = p_link.matcher(content);

		String matched_phrase = "";
		while(matcher_link.find()){
			matched_phrase = matcher_link.group();
			if(matched_phrase.indexOf("|")>=0){
				int pipe_loc = content.indexOf(matched_phrase) + matched_phrase.lastIndexOf('|');
				String temporary_buffer=content.substring(0, content.indexOf(matched_phrase) ) + content.substring(pipe_loc+1, content.length() ) ;
				content = new StringBuffer(temporary_buffer );
			}
		}
		String temp_str = content.toString().replace("[[", "");
		temp_str = temp_str.replace("]]", "");
		content=new StringBuffer(temp_str);
		
		if(content.indexOf("<!--")>0 ){
			int start_of_comment = content.indexOf("<!--") ;
			int end_of_comment = content.indexOf("-->") ;
			content.replace(start_of_comment , end_of_comment + 3, "" );
		}

		return content;
	}

	/*
		This function will remove unwanted content from the WikiText like '''' , [] , {} 
	*/

	public void removeExtraStuff(StringBuffer content){
		while( content.indexOf("'''''") > 0 )
			content.replace( content.indexOf("'''''"), content.indexOf("'''''") + 5 , "");

		while( content.indexOf("'''") > 0 )
			content.replace( content.indexOf("'''") , content.indexOf("'''") + 3 , "");

		while( content.indexOf("''") > 0 )
			content.replace( content.indexOf("''") , content.indexOf("''") + 2 , "");

		//Remove Info box
		Pattern p = Pattern.compile("[{][{][^}]*", Pattern.MULTILINE);
		Matcher m = p.matcher(content);

		String matched_phrase = "";
		Vector<String> matched_list=new Vector<String>();;
		while(m.find()){
			matched_phrase = m.group();
			matched_list.add(matched_phrase);
		}
		//matched_list vector has the list of info boxes

		for(int i =0;i<matched_list.size() ; i++){
			int indexOfMatchedBracket=content.indexOf(matched_list.get(i) );	
			
			int countOfMatchedStartingBracket = 1;
			indexOfMatchedBracket+=2;//Skip over the 1st set of open brackets 
			while(countOfMatchedStartingBracket!=0 && indexOfMatchedBracket<content.length()){
				if(content.charAt(indexOfMatchedBracket) == '{' && content.charAt(indexOfMatchedBracket+1) == '{')
					countOfMatchedStartingBracket++;
				if(content.charAt(indexOfMatchedBracket) == '}' && content.charAt(indexOfMatchedBracket+1) == '}')
					countOfMatchedStartingBracket--;
				indexOfMatchedBracket++;	
			}
			try{

				if(indexOfMatchedBracket!=content.length())
					matched_list.set(i , content.substring( content.indexOf(matched_list.get(i)) , indexOfMatchedBracket+1  ) );
				else
					matched_list.removeElementAt(i);
			}catch(StringIndexOutOfBoundsException e){
				System.out.println("COMIN HERE !! ");
				//System.out.println("Content : " + content + " indexof ; " +content.indexOf(matched_list.get(i))  + " , length : " + content.length() + " Index of matched bracket : " + indexOfMatchedBracket + " , Also matched phrase : " + matched_list.get(i)  );
				System.exit(0);	
			}
		}

		for(int i =0;i<matched_list.size() ;i++){
			if( content.indexOf(matched_list.get(i)) != -1){
				//Replace only if found. The matched pattern in the current iteration could have been a sub pattern in the previous iteration, which might have already been replaced.
				content.replace( content.indexOf(matched_list.get(i)) , content.indexOf(matched_list.get(i)) + matched_list.get(i).length() , "");
			}
		}
		//Info box and patterns with {{ }} removed 


		//Remove <ref> & </ref> tags
		while(content.indexOf("<ref>")!= -1){
			content.replace( content.indexOf("<ref>") , content.indexOf("<ref>") + 5 , "");
		}
		while(content.indexOf("</ref>")!= -1){
			content.replace( content.indexOf("</ref>") , content.indexOf("</ref>") + 6 , "");
		}

	}
}
