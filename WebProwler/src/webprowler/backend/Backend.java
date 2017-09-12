package webprowler.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import webprowler.gui.Window;
import webprowler.objects.Childsite;
import webprowler.objects.Entries;
import webprowler.utility.URLHandler;

public class Backend 
{
	/* OBJECT VARIABLES */
	private Document document;
	private Elements links;
	private Elements resources;
	private String description;
	private String tags;
	private HashSet<String> set = new HashSet<String>();
	private int viewportSize;
	
	public Backend(int viewportSize)
	{
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
			
			document = Jsoup.connect(url.replace(" ", "%20")).get();
			
			// Get attributes of the current URL
			try { links = document.select("a[href]"); } catch (Exception e) { }
			try { tags = document.select("meta[name=keywords]").first().attr("content"); } catch (Exception e) { }
			try { description = document.select("meta[name=description]").get(0).attr("content"); } catch (Exception e) { }
			
			// determine the valid links in the current URL
			for (Element element : links)
			{ 
				//TODO (1) Determine a way to get a good descriptor string
				// Determine if the current url is a valid url for a child
				if (!set.contains(element.attr("abs:href")) && URLHandler.isValid(element.attr("abs:href")))
				{
					children.add(element.attr("abs:href"));
					set.add(element.attr("abs:href"));
				}
				if (set.contains(element.attr("abs:href")))
				{
					duplicates++;
				}
			}
			
			// Populate the Childsite's attributes
			develop(site, children, url);
			// print the contents of the current Childsite
			//screening(site);
			
			// Add all of the site's children to the waitList 
			for (String iteration : site.getChildren())
			{
				Window.getWaitList().add(new Childsite(iteration, iteration));
				
				if (Window.getViewPort() < viewportSize) { Window.setViewPortSize(); }
			}
			//TODO (2) Add things to the the status display
			Window.getQueueDisplay();
			Window.getStatusDisplay();
			//System.out.println(" A SITE HAS BEEN POPULATED ");
			return site;
		} catch (Exception ex) {
			//TODO (4) What do we do when the site couldn't be populated
			//System.out.println(" FATAL ERROR EXCEPTION ");
			ex.printStackTrace();
			return null;
		}
	}
	/**
	 * Used to develop all of the attributes of the current site
	 * @param site The Childsite we need to set attributes to
	 * @param children The list of URL's to be added to the Childsite's Children
	 * @param url The current URL represented by a string used to set the Childsite's site
	 */
	private void develop(Childsite site, ArrayList<String> children, String url)
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
	
	// GETTERS FOR THE GUI
	public static int getDuplicates() { return duplicates; }
}
