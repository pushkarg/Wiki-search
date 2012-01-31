package utilities;

public class Page {
   
    StringBuffer  title;
    StringBuffer content;
    int flag;
   
    public void setTitle(StringBuffer title) {
        this.title = title;
    }
    public void setContent(StringBuffer content) {
        this.content = content;
    }

	public String getTitle(){
		return new String("Pushkar d .. title ");
	}

    public String getContent() {
        return new String("Pushkar D Testing . Sample string abcd");
    }
}
