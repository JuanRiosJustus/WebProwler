package webprowler.objects;

import java.util.ArrayList;
import java.util.List;

public class ProtectedSites 
{
	/* WRAPPER CLASS TO INITIALIZE USABLE WEBSITES*/
	private static List<Website> websites = new ArrayList<Website>();
	
	public ProtectedSites()
	{
		initParents();
	}
	
	private static void initParents()
	{
		websites.add(new ParentSite("https://www.ask.com/", "http://www.ask.com/web?q="));
		websites.add(new ParentSite("https://www.britannica.com/", "https://www.britannica.com/search?query="));
		websites.add(new ParentSite("https://www.encyclopedia.com/", "https://www.encyclopedia.com/search?keys="));
		websites.add(new ParentSite("https://answers.yahoo.com/", "https://answers.search.yahoo.com/search?fr=uh3_answers_vert_gs&type=2button&p="));
	}
	
	public static List<Website> getParents()
	{
		return websites;
	}
}
