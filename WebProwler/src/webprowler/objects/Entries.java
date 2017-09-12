package webprowler.objects;

import java.util.ArrayList;
import java.util.List;

public class Entries
{
	/* WRAPPER CLASS TO INITIALIZE USABLE WEBSITES*/
	private static List<Website> websites = new ArrayList<Website>();
	private static List<Parentsite> google = new ArrayList<Parentsite>();

	static 
	{
		// TODO (3) Determine what to do with the extra links were not using
		websites.add(new Parentsite("https://www.google.com/", "https://www.google.com/search?q=", "General"));
		websites.add(new Parentsite("https://www.britannica.com/", "https://www.britannica.com/search?query=", "Terms"));
		websites.add(new Parentsite("https://www.ask.com/", "http://www.ask.com/web?q=", "Questions"));
		websites.add(new Parentsite("https://www.answers.yahoo.com/", "https://answers.search.yahoo.com/search?p=", "Humor"));
		
		/* THE VERY GENERAL MOST USABLE GOOGLE SITE */
		google.add(new Parentsite("https://www.google.com/", "https://www.google.com/search?q=", "google"));
		/* THIS IS FOR GOOGLE IMAGES */
		google.add(new Parentsite("https://images.google.com/", "https://www.google.com/search?biw=&tbm=isch&q=", "Images"));
		/* THIS IS FOR GOOGLE VIDEOS (YOUTUBE SEARCH) */
		google.add(new Parentsite("https://www.youtube.com/", "https://www.youtube.com/results?search_query=", "Videos"));
	}
	
	public static List<Website> getParentSites() { return websites; } 
	public static String getWebsiteUrl(int index) { return websites.get(index).getNonSearchableURL(); }
	public static String getSearchableWebsiteUrl(int index) { return websites.get(index).getSearchableURL(); }
	
	public static List<Parentsite> getGoogleProducts() { return google; } 
	public static String getProductMainPage(int index) { return google.get(index).getNonSearchableURL(); }
	public static String getProductSearchPage(int index) { return websites.get(index).getSearchableURL(); }
}
