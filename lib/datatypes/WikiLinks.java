package datatypes;
import java.util.*;

public class WikiLinks{
	
	Vector<String> outPutLink = new Vector<String>();
	Vector<String> CategoryLink = new Vector<String>();
	String titleOfThePage;
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
