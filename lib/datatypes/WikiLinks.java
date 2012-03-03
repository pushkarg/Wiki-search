package datatypes;
import java.util.*;

public class WikiLinks{
	String titleOfThePage;
	Vector<String> outPutLink = new Vector<String>();
	Vector<String> CategoryLink = new Vector<String>();
	
	
	public Vector<String> getOutPutLink() {
		return outPutLink;
	}

	public void setOutPutLink(Vector<String> outPutLink) {
		this.outPutLink = outPutLink;
	}

	public Vector<String> getCategoryLink() {
		return CategoryLink;
	}

	public void setCategoryLink(Vector<String> categoryLink) {
		CategoryLink = categoryLink;
	}

	
	
	public void setTitleToLink(String titleBuffer)
	{
		titleOfThePage = titleBuffer;
	}
	
	public String getTitleToLink()
	{
		return titleOfThePage;
	}
	
	public void AddLinkString(String strBuffer)
	{
		outPutLink.add(strBuffer);
	}
	
	public void AddCategoryLink(String categoryBuffer)
	{
		CategoryLink.add(categoryBuffer);
	}
	
	public int DisplayOutLinks()
	{
		Iterator<String> linkItr =  outPutLink.iterator();
		while(linkItr!=null && linkItr.hasNext())
		{
			System.out.println(linkItr.next().toString());
		}
		
		return 0;
	}
	
}
