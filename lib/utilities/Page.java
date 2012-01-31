package parser;

public class Page {
	
	StringBuffer  title;
	StringBuffer content;
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


