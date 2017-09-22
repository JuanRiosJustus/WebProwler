package webprowler.backend;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import webprowler.frontend.MainWindow;
import webprowler.handlers.SiteHandler;
import webprowler.objects.Childsite;

public class Database 
{
	private static Document document;
	private static Elements links;
	private static Elements images;
	private static String description;
	private static String tags;
	
	/* HOLDS THE VALUES FOR THE COLLECTION WINDOW */
	private static ArrayList<Childsite> database = new ArrayList<Childsite>();
	/* HOLDS THE VALUES TO BE LOOKED AT THROUGH THE MAINWINDOW */
	private static Queue<Childsite> queue = new LinkedList<Childsite>();
	
	/**
	 * Create a Childsite object based off of a given url represented by a String
	 * @param url url represented by a String to base the new Childsite off of.
	 * @return A fully developed Childsite based off of the url given.
	 */
	public static Childsite createAndConnect(String url)
	{
		try {
			Childsite child  = new Childsite(null, null);
			ArrayList<String> children;
			ArrayList<String> gallery;
			
			document = Jsoup.connect(url.replace(" ", "%20")).get();
			
			//String text = document.select()
			
			// Get attributes of the current URL
			try { links = document.select("a[href]"); } catch (Exception e) { }
			try { images = document.select("img"); } catch (Exception e) { }
			try { tags = document.select("meta[name=keywords]").first().attr("content"); } catch (Exception e) { }
			try { description = document.select("meta[name=description]").get(0).attr("content"); } catch (Exception e) { }
			
			// Determine the valid resources and children for children.
			children = DatabaseAssistant.determineValidChildren(links);
			gallery = DatabaseAssistant.determineValidResources(links);
			
			// Inject data into child.
			develop(child, children, gallery, url);
			MainWindow.refreshDisplays();
			// Inject data into the queue.
			DatabaseAssistant.toQueue(child, queue);
			
			// Utility function
			DatabaseAssistant.screening(child);
			
			return child;
		} catch (Exception ex) {
			System.out.println("Null site added.");
			ex.printStackTrace();
			return null;
		}
	}
	private static void determinePolicy(Childsite child)
	{
		// TODO
	}
	/**
	 * To develop all of the attributes of the selected Childsite.
	 * @param site The Childsite selected to set the attributes of.
	 * @param children The ArrayList of UNVISITED url's to be added to the Childsite's Children.
	 * @param gallery The ArrayList of the images to be added to the Childsite's images.
	 * @param url The current url represented by a String used to set the selected Childsite's site.
	 */
	private static void develop(Childsite child, ArrayList<String> children, ArrayList<String> gallery, String url)
	{
		child.setSite(url.replace(" ", "%20"));
		
		child.setTitle(document.title());
		child.setDescripton(description);
		child.setKeywords(tags);
		child.setText(document.text());
		child.setChildren(children.toArray(new String[children.size()]));
		child.setResources(gallery.toArray(new String[gallery.size()]));
		
		child.unifyAllText();
		
		DatabaseAssistant.determineDomain(child);
		DatabaseAssistant.determineScore(child);
		DatabaseAssistant.isWantedChild(child);
	}
	
	/* FOR USE IN THE DATABASEASSISTANT */
	static String getMainWindowTargetTerm() { return MainWindow.getTargetTerm(); }
	static boolean getNegMainWindowTerminateThreadStatus() { return !MainWindow.getTerminateThreadStatus(); }
	
	/* FOR USE IN THE MAINWINDOW */
	public static int getLoops() { return DatabaseAssistant.getParentsFound(); }
	public static int getHits() { return DatabaseAssistant.getValuableChildrenFound(); }
	public static void resetBlacklist() { DatabaseAssistant.clearListOfVisitedChildren(); }
	public static void resetLoops() { DatabaseAssistant.clearParentsFound(); }
	public static void resetHits() { DatabaseAssistant.clearValuableChildrenFound(); }
	
	public static void queue_enqueue(Childsite child) { queue.offer(child); }
	public static Childsite queue_dequeue() { return queue.poll(); }
	public static boolean queue_isEmpty() { return queue.isEmpty(); }
	public static int queue_size() { return queue.size(); }
	public static Queue<Childsite> queue_only_for_status() { return queue; } 
	
	/* FOR USE IN THE COLLECTIONWINDOW */
	public static void database_add(Childsite child) { database.add(child); }
	public static int database_size() { return database.size(); }
	public static void database_clear() { database.clear(); }
	public static void database_removeExcess() { database.removeAll(Collections.singleton(null)); }
	public static ArrayList<Childsite> database_for_gui_only() { return database; }
}
