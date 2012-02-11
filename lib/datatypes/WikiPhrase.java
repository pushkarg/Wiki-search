package datatypes;

public class WikiPhrase {

	StringBuffer phrase;
	int position_in_text;
	int type; 	//Bold or italic text or Heading

	public WikiPhrase(){
	}
	
	public WikiPhrase(String phrase){
		this.phrase = new StringBuffer(phrase);
	}

	public WikiPhrase(String phrase , int position){
		this.phrase = new StringBuffer(phrase);
		this.position_in_text = position;
	}

	public WikiPhrase(String phrase , int position , int type){
		this.phrase = new StringBuffer(phrase);
		this.position_in_text = position;
		this.type = type;
	}

	public String getPhrase(){
		return this.phrase.toString();
	}

	public void setPhrase(String phrase){
		this.phrase = new StringBuffer( phrase) ;
	}

	public int getPhrasePosition(){
		return this.position_in_text;
	}

	public void setPhrasePosition(int position){
		this.position_in_text = position;
	}

	public int getPhraseType(int type){
		return this.type;
	}

	public void setPhraseType(int type){
		this.type = type;
	}

}

