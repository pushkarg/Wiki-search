package parser;
import java.util.regex.*;


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
	public StringBuffer[] extractRefTagsFromContent(StringBuffer []ref_tags){
		String tag_list[] = new String[100];	
		int current_string_count = 0;
		boolean ref_tags_exist = true;
		int ref_index = 0;

		Pattern p = Pattern.compile("<ref[^<]*</ref>", Pattern.MULTILINE);
		Matcher m = p.matcher(content_text);

		while(m.find() ){
			tag_list[ref_index] = new String( m.group() );
			ref_index++;
		}

		ref_tags=new StringBuffer[ref_index];
		for(int i=0;i<ref_index;i++)
			ref_tags[i] = new StringBuffer( tag_list[i]);

		for(int i=0;i<ref_tags.length ; i++){
			 int start_index = content_text.indexOf(ref_tags[i].toString());
			 content_text.replace(start_index , start_index + ref_tags[i].length() , "");
		}

		return ref_tags;
	}
}
