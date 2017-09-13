package webprowler.handlers;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import webprowler.objects.Parentsite;

public class TaskHandler 
{
	private static ObservableList<String> entryPoints = FXCollections.observableArrayList(); 
	private static ObservableList<String> limits = FXCollections.observableArrayList();
	private static List<Parentsite> websites = new ArrayList<Parentsite>();
	
	static 
	{
		websites.add(new Parentsite("https://www.google.com/", "https://www.google.com/search?q=", "Google"));
		websites.add(new Parentsite("https://www.reddit.com/", "https://www.reddit.com/search?q=", "Reddit"));
		websites.add(new Parentsite("https://www.britannica.com/", "https://www.britannica.com/search?query=", "Britannica"));
		websites.add(new Parentsite("https://www.amazon.com/", "https://www.amazon.com/s/field-keywords=", "Amazon"));
		websites.add(new Parentsite("https://www.answers.yahoo.com/", "https://answers.search.yahoo.com/search?p=", "Yahoo Answers"));
		
		entryPoints.add(websites.get(0).getDomain());
		entryPoints.add(websites.get(1).getDomain());
		entryPoints.add(websites.get(2).getDomain());
		entryPoints.add(websites.get(3).getDomain());
		entryPoints.add(websites.get(4).getDomain());
		
		limits.add("10000000");
		limits.add("1000000");
		limits.add("100000");
		limits.add("10000");
		limits.add("1000");
		limits.add("100");
		limits.add("10");
		
	}
	// TODO
	public static String determineEntry(String entryPoint)
	{
		if (entryPoint.equals(websites.get(0).getDomain())) return websites.get(0).getSearchableURL();
		if (entryPoint.equals(websites.get(1).getDomain())) return websites.get(1).getSearchableURL();
		if (entryPoint.equals(websites.get(2).getDomain())) return websites.get(2).getSearchableURL();
		if (entryPoint.equals(websites.get(3).getDomain())) return websites.get(3).getSearchableURL();
		if (entryPoint.equals(websites.get(4).getDomain())) return websites.get(4).getSearchableURL();
		
		return "";
	}
	
	public static ObservableList<String> getEntryPoints() { return entryPoints; }
	public static ObservableList<String> getLimits() { return limits; }
	
	public static List<Parentsite> getWebsites() { return websites; } 
	public static String getNonSearchableWebsite(int index) { return websites.get(index).getNonSearchableURL(); }
	public static String getSearchableWebsite(int index) { return websites.get(index).getSearchableURL(); }
	
	
	/* Used to get the amount from the JComboBox */
	public static int limit (Object size) { return Integer.valueOf((String)size); }
}
