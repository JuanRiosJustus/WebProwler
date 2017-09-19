package webprowler.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import webprowler.gui.MainWindow;
import webprowler.handlers.URLHandler;
import webprowler.objects.Childsite;

public class Backend 
{
	/* OBJECT / INSTANCE VARIABLES */
	private static HashSet<String> listOfVisited = new HashSet<String>();
	private static Document document;
	private static Elements links;
	//TODO TBD private Elements resources;
	
	private static String description;
	private static String tags;
	
	private static int viewportSize = 0;
	private static int duplicates = 0;
	
	static 
	{
		
	}
	/**
	 * Adjust the size of the viewable elements shown.
	 * @param size The size of viewable items in the queue.
	 */
	public static void setViewport(int size)
	{
		viewportSize = size;
	}
	/**
	 * Create a Childsite object based off of a given url represented by a String
	 * @param url url represented by a String to base the new Childsite off of.
	 * @return A fully developed Childsite based off of the url given.
	 */
	public static Childsite createAndConnect(String url)
	{
		Childsite site = new Childsite("", "");
		try {
			ArrayList<String> children = new ArrayList<String>();
			
			document = Jsoup.connect(url.replace(" ", "%20")).timeout(0).get();
			
			// Get attributes of the current URL
			try { links = document.select("a[href]"); } catch (Exception e) { }
			try { tags = document.select("meta[name=keywords]").first().attr("content"); } catch (Exception e) { }
			try { description = document.select("meta[name=description]").get(0).attr("content"); } catch (Exception e) { }
			//TODO DETERMINE CONNECTION
			// Determine the valid children/links for the Childsite.
			determineChildren(children, links);
			// Populate the current Childsite.
			developChildsite(site, children, url);
			// print the contents of the current Childsite
			//screening(site);
			// Import the all the Children of the current Childsite.
			importSitesToQueue(site);
			
			//TODO (2) Add things to the the status display
			MainWindow.refresh();
			return site;
		} catch (Exception ex) {
			System.out.println("Null site added.");
			ex.printStackTrace();
			return null;
		}
	}
	/**
	 * To develop all of the attributes of the selected Childsite.
	 * @param site The Childsite selected to set the attributes of.
	 * @param children The ArrayList of UNVISITED url's to be added to the Childsite's Children.
	 * @param url The current url represented by a String used to set the selected Childsite's site.
	 */
	private static void developChildsite(Childsite site, ArrayList<String> children, String url)
	{
		site.setTitle(document.title());
		site.setDescripton(description);
		site.setKeywords(tags);
		site.setText(document.text());
		site.setSite(url.replace(" ", "%20"));
		site.setChildren(children.toArray(new String[children.size()]));
	}
	/**
	 * Print the contents of the Childsite to system.
	 * @param site The Childsite we want to know the information of.
	 */
	private static void screening(Childsite site)
	{
		System.out.println("Current site: " + site.getTitle());
		System.out.println("Keywords: " + site.getKeywords());
		System.out.println("Description: " + site.getDescripton());
		System.out.println("Text: " + site.getText());
		System.out.println("Site: " + site.getNonSearchableURL());
		System.out.println("Children: " + Arrays.toString(site.getChildren()));
	}
	/**
	 * Imports the children of the selected Childsite into the queue.
	 * @param site The site we extract the children from.
	 */
	private static void importSitesToQueue(Childsite site)
	{
		for (String child : site.getChildren())
		{
			// Check to see if the queue is not filled
			if(!MainWindow.getTerminateThreadStatus())
			{
				MainWindow.getQueue().add(new Childsite(child, child));
			}
			// Update's the size of visible queue data in queueDisplay in Window
			if (MainWindow.getViewPort() < viewportSize) { MainWindow.setViewPortSize(); }	
		}
	}
	/**
	 * To determine if we have seen this link before.
	 * @param children The ArrayList we use to add all our UNVISITED url's to.
	 * @param links The list of links we look through.
	 */
	private static void determineChildren(ArrayList<String> children, Elements links)
	{
		for (Element element : links)
		{
			// If we have this in the set, increment duplicates; else, add it to the children and the list of visited children.
			if (listOfVisited.contains(element.attr("abs:href"))) 
			{ 
				duplicates++;
			} else if (URLHandler.isValidChild(element.attr("abs:href"))) {
				// TODO determine a better way of finding if a Childsite and it's children are related.
				children.add(element.attr("abs:href"));
				listOfVisited.add(element.attr("abs:href"));	
			}
		}
	}
	
	// GETTERS FOR THE WINDOW
	public static int getDuplicates() { return duplicates; }
	public static void resetDuplicates() { duplicates = 0;}
	public static void resetHashSet() { listOfVisited.clear(); }
}
