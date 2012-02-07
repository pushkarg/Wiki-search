package parser;
import java.util.regex.*;
import datatypes.*;


public class  WikiContentParser{
	StringBuffer content_text;

	public WikiContentParser(StringBuffer text){
		content_text = text;
	}

	public void loadBufferWithRawContent(StringBuffer raw_text ){
		content_text = raw_text;
	}

	/*
		This extracts the <ref></ref> tags from the content and adds them to the ref_tags array
	*/
	public WikiUrl[] extractRefTagsFromContent(WikiUrl []ref_tags){
		String tag_list[] = new String[100];	
		int current_string_count = 0;
		boolean ref_tags_exist = true;
		int ref_index = 0;
		WikiUrl []temp_url_list = new WikiUrl[100];

		Pattern p = Pattern.compile("<ref[^<]*</ref>", Pattern.MULTILINE);
		Matcher m = p.matcher(content_text);

		while(m.find() ){
			tag_list[ref_index] = new String( m.group() );
			ref_index++;
		}

		temp_url_list=new WikiUrl[ref_index];
		/*
		for(int i=0;i<ref_index;i++)
			ref_tags[i] = new StringBuffer( tag_list[i]);
		*/

		for(int i=0;i<temp_url_list.length ; i++){
			 int start_index = content_text.indexOf(tag_list[i].toString());
			 content_text.replace(start_index , start_index + tag_list[i].length() , "");
		}

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
}
