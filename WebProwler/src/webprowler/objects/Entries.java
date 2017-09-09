package webprowler.objects;

import java.util.ArrayList;
import java.util.List;

public class Entries
{
	/* WRAPPER CLASS TO INITIALIZE USABLE WEBSITES*/
	private static List<Website> websites = new ArrayList<Website>();
	
	static 
	{
		websites.add(new Parentsite("https://www.ask.com/", "http://www.ask.com/web?q=", "ask"));
		websites.add(new Parentsite("https://www.britannica.com/", "https://www.britannica.com/search?query=", "britannica"));
		websites.add(new Parentsite("https://www.encyclopedia.com/", "https://www.encyclopedia.com/search?keys=", "encyclopedia"));
		websites.add(new Parentsite("https://www.answers.yahoo.com/", "https://answers.search.yahoo.com/search?p=", "answers.yahoo"));
	}
	
	public static List<Website> getParentSites()
	{
		return websites;
	}
	public static String getWebsiteUrl(int index)
	{
		return websites.get(index).getNonSearchableURL();
	}
}
