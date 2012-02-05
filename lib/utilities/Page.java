package utilities;

public class Page {
	
	StringBuffer  title;
	StringBuffer content;
	Boolean redirect;
	StringBuffer timestamp;
	
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
	int flag;
	
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
}


