package utilities;

import utilities.WikiConstants;

public class Page {
	
	StringBuffer  title;
	StringBuffer content;
	Boolean redirect;
	StringBuffer timestamp;
	int flag , page_type;

	public Page(){
		 flag = 0;
		 page_type = WikiConstants.UNKNOWN_PAGE;
	}
	
	public Boolean getRedirect() {
		return redirect;
	}
	public void setRedirect(Boolean redirect) {
		this.redirect = redirect;
	}
	public StringBuffer getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(StringBuffer timestamp) {
		this.timestamp = timestamp;
	}
	
	public StringBuffer getTitle() {
		return title;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public void setTitle(StringBuffer title) {
		this.title = title;
	}
	public StringBuffer getContent() {
		return content;
	}
	public void setContent(StringBuffer content) {
		this.content = content;
	}

	public void resetPage(){
	//This function can be used to set all the properties of the Page object to blank. This is useful when the same Page object is used for different Wiki articles, without instantiating new Objs
		title = new StringBuffer();
 		content = new StringBuffer();
		redirect = false;
		timestamp=new StringBuffer();
		flag = 0;
	}

	public int getPageType(){
		System.out.println("Coming to the getPageTyp func ");
		 if(page_type != WikiConstants.UNKNOWN_PAGE )
		 	return page_type;	//Return the page type if it is known already

		//Else parse the title field and get the page type
		StringBuffer page_title = this.title;
		if(page_title.toString().contains(":") == false)
			return WikiConstants.CONTENT_PAGE; 		// This is a normal wikipedia article page
		
		String type = page_title.substring(0, page_title.indexOf(":") );
		System.out.println("The type is : " + type);
		

		return 0;
	}
}


