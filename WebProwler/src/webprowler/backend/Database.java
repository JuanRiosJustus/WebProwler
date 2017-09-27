package webprowler.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import webprowler.frontend.MainWindow;
import webprowler.objects.Childsite;

public class Database 
{
	/* HOLDS THE VALUES FOR THE COLLECTION WINDOW */
	private static ArrayList<Childsite> database = new ArrayList<Childsite>();
	/* HOLDS THE VALUES TO BE LOOKED AT THROUGH THE MAINWINDOW */
	private static Queue<Childsite> queue = new LinkedList<Childsite>();
	/* HOLDS THE LIST OF POLICIES FOR WEBSITES WE HAVE CONNECTED TO */
	private static HashSet<String> blacklist = new HashSet<String>();
	
	/**
	 * Create a Childsite object based off of a given url represented by a String
	 * @param url url represented by a String to base the new Childsite off of.
	 * @return A fully developed Childsite based off of the url given.
	 */
	public static Childsite createAndConnect(String url)
	{
		try {
			//Check permissions given to us by the host.
			PolitenessPolicy.followPolitenessPolicy(url);
	
			Thread.sleep(10000);
			
			Childsite child  = new Childsite(null, null);
			Crawler crawler = new Crawler(url);
			ArrayList<String> children;
			
			// Determine the valid Children for child.
			children = DatabaseAssistant.determineValidChildren(crawler.getLinks());
			
			// Inject data into child.
			develop(child, crawler, children);
			
			// Inject data into the queue.
			DatabaseAssistant.childrenToQueue(child, queue);
			
			// Utility function
			DatabaseAssistant.screening(child);
			
			return child;
			
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
	 * @param gallery The ArrayList of the images to be added to the Childsite's images.
	 * @param url The current url represented by a String used to set the selected Childsite's site.
	 */
	private static void develop(Childsite child, Crawler crawler, ArrayList<String> children)
	{
		child.setSite(crawler.getAddress());
		
		child.setTitle(crawler.getDocumentTitle());
		child.setDescripton(crawler.getDescription());
		child.setKeywords(crawler.getTags());
		child.setText(crawler.getdocumentText());
		child.setChildren(children.toArray(new String[children.size()]));
		//child.setResources(gallery.toArray(new String[gallery.size()]));
		
		child.unifyAllText();
		
		DatabaseAssistant.determineDomain(child);
		DatabaseAssistant.determineScore(child);
		DatabaseAssistant.isWantedChild(child);
	}
	
	/* FOR USE IN THE DATABASEASSISTANT */
	static String getMainWindowTargetTerm() { return MainWindow.getTargetTerm(); }
	static boolean getNegMainWindowTerminateThreadStatus() { return !MainWindow.getTerminateThreadStatus(); }
	static boolean isFoundOnTheBlacklist(String site) { for (String child : blacklist) { if (site.contains(child)) { return true; } } return false; }
	
	/* FOR USE IN POLICY */
	static void addToBlacklist(String site) { blacklist.add(site); }
	
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
	public static void queue_clear() { queue.clear(); }
	public static Childsite queue_peek() { return queue.peek(); }
	public static Queue<Childsite> queue_only_for_status() { return queue; } 
	
	/* FOR USE IN THE COLLECTIONWINDOW */
	public static void database_add(Childsite child) { database.add(child); }
	public static int database_size() { return database.size(); }
	public static void database_clear() { database.clear(); }
	public static void database_removeExcess() { database.removeAll(Collections.singleton(null)); }
	public static ArrayList<Childsite> database_for_gui_only() { return database; }
}
