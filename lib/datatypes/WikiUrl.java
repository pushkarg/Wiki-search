package datatypes;

public class WikiUrl {
	 String url, title,additional_data;

	 public WikiUrl(String url){
	 	 this.url = url;
	 }

	 public WikiUrl(String url , String title){
	 	 this.url = url;
		 this.title = title;
	 }

	 public WikiUrl(String url , String title, String additional_data){
	 	 this.url = url;
		 this.title = title;
		 this.additional_data = additional_data;
	 }


	
	public void setUrl(String url){
		 this.url = url;
	}

	public void setUrlTitle(String title){
		 this.title = title;
	}

	public void setUrlAdditionalData(String data){
		 this.additional_data  = data;
	}

	public String getUrl(){
		return  url;
	}

	public String getUrlTitle(){
		return title;
	}

	public String getAdditionalData(){
		 return additional_data;
	}
}
