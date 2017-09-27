package webprowler.backend;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import webprowler.objects.Childsite;

public class PolitenessPolicy 
{
	private static ArrayList<String> invalidChildren = new ArrayList<String>();
	private static HashSet<String> visitedDomains = new HashSet<String>();
	private static Document document;
	private static URI uri;
	/**
	 * Gets the RES for the given host and checks the commands.
	 * @param url The url represented by a  string who we find the politeness policy for.
	 */
	static void followPolitenessPolicy(String url)
	{
		String webAddress = url.replace(" ", "+");
		String protocolLink;
		
		try {
			uri = new URI(webAddress);
			protocolLink = getRobotProtocolAddressS(uri.getHost());
			document = Jsoup.connect(protocolLink).get();
			
			if (!visitedDomains.contains(uri.getHost()))
			{
				visitedDomains.add(uri.getHost());
				readCommands(document.text(), uri.getHost());
			} else { System.out.println("Weve checked this site"); return; }	
		} catch (Exception ex) { ex.printStackTrace(); }
		
		try {
			uri = new URI(webAddress);
			protocolLink = getRobotProtocolAddress(uri.getHost());
			document = Jsoup.connect(protocolLink).get();
		
			if (!visitedDomains.contains(uri.getHost()))
			{
				visitedDomains.add(uri.getHost());
				readCommands(document.text(), uri.getHost());
			} else { System.out.println("Weve checked this site"); return; }
		} catch (Exception ex) { ex.printStackTrace(); }
	}
	/**
	 * Reads the commands of each line pertaining to all user agents.
	 * @param resDocumentText The RES document we want to read.
	 * @param domain The domain of the host.
	 */
	private static void readCommands(String resDocumentText, String domain)
	{
		String[] commands = resDocumentText.split("(?<!\\G\\S+)\\s");
		String userAgent = null;
		for (String command : commands)
		{
			if (command.startsWith("User-agent:")) { userAgent = command; }
			if (command.startsWith("Disallow:") && userAgent.endsWith("*") && userAgent.charAt(userAgent.indexOf("*") - 1) == ' ')
			{
				toBlacklist(domain, new StringBuilder(command).toString().substring(new StringBuilder(command).toString().indexOf(" ") + 1, new StringBuilder(command).toString().length()));
			}
		}
	}
	/**
	 * Add the site that we are told to not search.
	 * @param domain The host we get the commands from.
	 * @param extension Page were told not to view.
	 */
	private static void toBlacklist(String domain, String extension)
	{
		//System.out.println(domain + extension + " was blocked."); 
		if((domain + extension).contains("User-agent:")) { return; }
		if(domain.endsWith("/"))
		{
			Database.addToBlacklist(domain + extension.substring(1));
		} else {
			Database.addToBlacklist(domain + extension);
		}
	}
	/**
	 * Need to determine if the current URL is a valid URL for a child
	 * @param url the URL we're basing our condition off of 
	 * @return true if and only if the current URL doesn't contain "google", " ", "".
	 */
	public static boolean isValidChild(String url)
	{
		if (url.length() > 90 || url.equals("") || url.equals(" ") || url.contains("mailto")) { return false; }
		for (String s : invalidChildren)  { if (url.contains(s)) { return false; } }
		
		return true; 
	}
	/**
	 * Returns the exclusion protocol address.
	 * @param domain The host of the current address.
	 * @return The address of the exclusion protocol.
	 */
	private static String getRobotProtocolAddressS(String domain)
	{
		return "https://" + domain + "/robots.txt";
	}
	/**
	 * Returns the exclusion protocol address.
	 * @param domain The host of the current address.
	 * @return The address of the exclusion protocol.
	 */
	private static String getRobotProtocolAddress(String domain)
	{
		return "http://" + domain + "/robots.txt";
	}
	
}
