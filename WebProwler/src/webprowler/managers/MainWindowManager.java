package webprowler.managers;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import webprowler.objects.Childsite;

public class MainWindowManager 
{
	private static ObservableList<String> entryPoints = FXCollections.observableArrayList(); 
	private static ObservableList<String> limits = FXCollections.observableArrayList();
	private static List<Childsite> websites = new ArrayList<Childsite>();
	
	static 
	{
		websites.add(new Childsite("https://www.google.com/search?q=", "Google"));
		websites.add(new Childsite("https://www.reddit.com/search?q=", "Reddit"));
		websites.add(new Childsite("https://www.youtube.com/results?search_query=", "YouTube"));
		websites.add(new Childsite("https://www.britannica.com/search?query=", "Britannica"));
		websites.add(new Childsite("https://en.wikipedia.org/w/index.php?search=", "Wikipedia"));
		websites.add(new Childsite("https://www.amazon.com/s/field-keywords=", "Amazon"));
		websites.add(new Childsite("https://answers.search.yahoo.com/search?p=", "Yahoo Answers"));
		websites.add(new Childsite("", "Custom Entry"));
		
		entryPoints.add(websites.get(0).getDomainURL());
		entryPoints.add(websites.get(1).getDomainURL());
		entryPoints.add(websites.get(2).getDomainURL());
		entryPoints.add(websites.get(3).getDomainURL());
		entryPoints.add(websites.get(4).getDomainURL());
		entryPoints.add(websites.get(5).getDomainURL());
		entryPoints.add(websites.get(6).getDomainURL());
		entryPoints.add(websites.get(7).getDomainURL());
		
		limits.add("1000000000");
		limits.add("100000000");
		limits.add("10000000");
		limits.add("1000000");
		limits.add("100000");
		limits.add("10000");
		limits.add("1000");
	}
	/**
	 * To determine what site to go to based on the entry point chosen by the user
	 * @param entryPoint The user selected entry point from the combobox in the Main Window
	 * @return The new searchable url of the entryPoint
	 */
	public static String determineEntry(String entryPoint)
	{
		if (entryPoint.equals(websites.get(0).getDomainURL())) return websites.get(0).getWebsiteURL();
		if (entryPoint.equals(websites.get(1).getDomainURL())) return websites.get(1).getWebsiteURL();
		if (entryPoint.equals(websites.get(2).getDomainURL())) return websites.get(2).getWebsiteURL();
		if (entryPoint.equals(websites.get(3).getDomainURL())) return websites.get(3).getWebsiteURL();
		if (entryPoint.equals(websites.get(4).getDomainURL())) return websites.get(4).getWebsiteURL();
		if (entryPoint.equals(websites.get(5).getDomainURL())) return websites.get(5).getWebsiteURL();
		if (entryPoint.equals(websites.get(6).getDomainURL())) return websites.get(6).getWebsiteURL();
		if (entryPoint.equals(websites.get(7).getDomainURL())) return websites.get(7).getWebsiteURL();
		
		return "";
	}
	
	// GETTER METHODS FOR THE MAIN WINDOW
	public static ObservableList<String> getEntryPoints() { return entryPoints; }
	public static ObservableList<String> getLimits() { return limits; }
	
	public static List<Childsite> getWebsites() { return websites; } 
	public static String getSearchableWebsite(int index) { return websites.get(index).getWebsiteURL(); }
	
	// Determine the limit of searches we want during this time
	public static int limit(Object size) { return Integer.valueOf((String)size); }
}
