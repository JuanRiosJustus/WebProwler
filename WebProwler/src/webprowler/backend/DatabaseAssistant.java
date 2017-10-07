package webprowler.backend;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import webprowler.managers.SiteManager;
import webprowler.objects.Childsite;

public class DatabaseAssistant
{
	private static HashSet<String> listOfVisitedChildren = new HashSet<String>();
	
	private static int parentsFound = 0;
	private static int valuableChildrenFound = 0;
	
	/**
	 * To determine if we have seen this link/child before, and negate it.
	 * @param links The list of links we look through to determine children.
	 * @return The list of unvisited children represented by the links
	 */
	static ArrayList<String> determineValidChildren(Elements links)
	{
		ArrayList<String> children = new ArrayList<String>();
		
		for (Element element : links)
		{
			// If we have this in the set, increment duplicates; else, add it to the children and the list of visited children.
			if (listOfVisitedChildren.contains(element.attr("abs:href"))) 
			{ 
				parentsFound++;
			} else if (SiteManager.isValidChild(element.attr("abs:href")) && !Database.isFoundOnTheBlacklist(element.attr("abs:href"))) {
				// TODO determine a better way of finding if a Childsite and it's children are related.
				children.add(element.attr("abs:href"));
				listOfVisitedChildren.add(element.attr("abs:href"));
			}
		}
		return children;
	}
	/**
	 * Imports the children of the selected Childsite into the queue.
	 * @param site The site we extract the children from.
	 * @param queue The queue we add the children to.
	 */
	static void childrenToQueue(Childsite site, Queue<Childsite> queue)
	{
		for (String child : site.getChildren())
		{
			// Check to see if the threads are being stopped.
			if(Database.getNegMainWindowTerminateThreadStatus())
			{	
				queue.offer(new Childsite(child, child));
			}
		}
	}
	/**
	 * Determine the domain of the given child.
	 * @param child The child we set the domain of.
	 */
	static void determineDomain(Childsite child)
	{
		try {
			URI uri = new URI(child.getWebsiteURL());
			child.setDomainURL(uri.getHost());
		} catch (Exception ex) {
			child.setDomainURL("N/A");
		}
	}
	/**
	 * Determines the amount of time the user term has been found within a child.
	 * @param child The child we look at to determine its worth.
	 */
	static void determineScore(Childsite child)
	{
		child.setScore(frequency(child.getEntirety(), Database.getMainWindowTargetTerm()));
	}
	/**
	 * Print the contents of the Childsite to system.
	 * @param site The Childsite we want to know the information of.
	 */
	static void screening(Childsite child)
	{
		System.out.println("Current site: " + child.getTitle());
		System.out.println("Domain: " + child.getDomainURL());
		System.out.println("Keywords: " + child.getKeywords());
		System.out.println("Description: " + child.getDescripton());
		System.out.println("Text: " + child.getText());
		System.out.println("Site: " + child.getWebsiteURL());
		System.out.println("Children: " + Arrays.toString(child.getChildren()));
	}
	static boolean isWantedChild(Childsite child)
	{
		if (child.getScore() > 0)
		{
			valuableChildrenFound++;
			return true;
		}
		return false;
	}
	/**
	 * Used to aggregate the frequency of the target term within a Childsite.
	 * @param text The original string in which we find the stringToFind in.
	 * @param stringToFind The string to be searched for.
	 * @return The frequency of a substring in a given String.
	 */
	private static int frequency(String text, String stringToFind)
	{
		int end = 0;
		int count = 0;

		while(end != -1){
			end = text.toLowerCase().indexOf(stringToFind.toLowerCase(), end);
			if (end != -1)
			{
				count++;
				end = end + stringToFind.length();
			}
		}
		return count;
	}
	/* ONLY ACCESS THROUGH DATABASE */
	static void clearListOfVisitedChildren() { listOfVisitedChildren.clear(); }
	static void clearParentsFound() { parentsFound = 0; }
	static void clearValuableChildrenFound() { valuableChildrenFound = 0; }
	
	static int getParentsFound() { return parentsFound; }
	static int getValuableChildrenFound() { return valuableChildrenFound; }
}
