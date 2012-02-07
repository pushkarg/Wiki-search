package parser;

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
	public int extractRefTagsFromContent(StringBuffer []ref_tags){
		String tag_list[];	

		boolean ref_tags_exist = true;
		while(ref_tags_exist){
			int start_index = content_text.indexOf("<ref");
			int end_index = content_text.indexOf("</ref");
			if(start_index > content_text.length() || end_index>=content_text.length() ) {
				ref_tags_exist = false;
				break;
			}
		}
		
		return 0;
	}
}
