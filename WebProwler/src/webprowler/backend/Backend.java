package webprowler.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import webrprowler.gui.Window;
import webprowler.handlers.URLHandler;
import webprowler.objects.Childsite;

public class Backend 
{
	/* OBJECT VARIABLES */
	private Document document;
	private Elements links;
	private Elements resources;
	private String description;
	private String tags;
	private static HashSet<String> set;
	
	private int viewportSize;
	private static int collection;
	
	/**
	 * Constructor to initialize backend
	 * @param viewportSize the amount of visible links visible in the queueDisplay
	 */
	public Backend(int viewportSize)
	{
		set = new HashSet<String>();
		collection = 0;
		this.viewportSize = viewportSize;
	}
	
	/* TRACKING VARIABLES */
	private static int duplicates = 0;
	
	/**
	 * Create a new Childsite based off of the current url
	 * @param url - a url represented by a string to base a new Childsite off of
	 * @return A fully developed Childsite based off of the current URL given
	 */
	public Childsite createAndConnect(String url)
	{
		ArrayList<String> children = new ArrayList<String>();
		Childsite site = new Childsite("", "");
		
		try {
			
			document = Jsoup.connect(url.replace(" ", "%20")).timeout(0).get();
			
			// Get attributes of the current URL
			try { links = document.select("a[href]"); } catch (Exception e) { }
			try { tags = document.select("meta[name=keywords]").first().attr("content"); } catch (Exception e) { }
			try { description = document.select("meta[name=description]").get(0).attr("content"); } catch (Exception e) { }
			
			// determine the valid links in the current URL
			determineChildren(children, links);
			// Populate the Childsite's attributes
			developChildsite(site, children, url);
			// print the contents of the current Childsite
			//screening(site);
			// Import the sites children to the waitList 
			importSitesToQueue(site);
			
			//TODO (2) Add things to the the status display
			Window.getQueueDisplay();
			Window.getStatusDisplay();
			return site;
		} catch (Exception ex) {
			//TODO (4) What do we do when the site couldn't be populated
			System.out.println("NULL SITE WAS CREATED");
			ex.printStackTrace();
			return null;
		}
	}
	/**
	 * Used to develop all of the attributes of the current site
	 * @param site The Childsite we need to set attributes to
	 * @param children The ArrayList of unvisited URL's to be added to the Childsite's Children
	 * @param url The current URL represented by a string used to set the Childsite's site
	 */
	private void developChildsite(Childsite site, ArrayList<String> children, String url)
	{
		site.setTitle(document.title());
		site.setDescripton(description);
		site.setKeywords(tags);
		site.setText(document.text());
		site.setSite(url.replace(" ", "%20"));
		site.setChildren(children.toArray(new String[children.size()]));
	}
	/**
	 * Print the contents of the child site
	 * @param site The Childsite we want to know the information for
	 */
	private void screening(Childsite site)
	{
		System.out.println("Current site: " + site.getTitle());
		System.out.println("Keywords: " + site.getKeywords());
		System.out.println("Description: " + site.getDescripton());
		System.out.println("Text: " + site.getText());
		System.out.println("Site: " + site.getNonSearchableURL());
		System.out.println("Children: " + Arrays.toString(site.getChildren()));
	}
	/**
	 * Imports the children of given site to the waitList
	 * @param site The site we extract the children from
	 */
	private void importSitesToQueue(Childsite site)
	{
		// Add all of the site's children to the waitList 
		for (String iteration : site.getChildren())
		{
			Window.getQueue().add(new Childsite(iteration, iteration));
			collection++;
			
			// update the GUI's queue view
			if (Window.getViewPort() < viewportSize) { Window.setViewPortSize(); }	
		}
	}
	/**
	 * We need to determine if we have seen this link before or if it's something we dont want
	 * @param children The ArrayList we use to add all our unvisited URL's to
	 * @param links The set of links we look through to set our children (unvisited links)
	 */
	private void determineChildren(ArrayList<String> children, Elements links)
	{
		// determine the valid links in the current URL
		for (Element element : links)
		{
			// If we have this in the set, increment duplicates
			if (set.contains(element.attr("abs:href")))
			{
				duplicates++;	
			}
			//TODO (1) Determine a way to get a good descriptor string
			// Determine if the current url is a valid url for a child
			if (!set.contains(element.attr("abs:href")) && URLHandler.isValid(element.attr("abs:href")) && URLHandler.isLegalChild(element.attr("abs:href")))
			{
				children.add(element.attr("abs:href"));
				set.add(element.attr("abs:href"));	
			}	
		}
	}
	
	// GETTERS FOR THE GUI
	public static int getDuplicates() { return duplicates; }
	public static int getCollection() { return collection; }
	public static void resetDuplicates() { duplicates = 0;}
	public static void resetCollection() { collection = 0; }
	public static void resetHashSet() { set.clear(); }
}
