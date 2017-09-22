package webprowler.backend;

import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import webprowler.objects.Childsite;

public class Policy 
{
	private static HashSet<String> blacklist = new HashSet<String>();
	// That is the robot exclusion standard
	private static final String RES = "/robots.txt";
	
	public static void getPolitenessPolicy(Childsite site)
	{
		String protocolLink = getRobotProtocol(site.getDomain());
		
		Document document;
		try {
			document = Jsoup.connect(protocolLink).get();
			//TODO check for the policy of the domain
			
		
			
		} catch (Exception ex) {
			site.setDomain("N/A");
			ex.printStackTrace();
		}
	}
	
	private static String getRobotProtocol(String domain)
	{
		return "https://" + domain + RES;
	}
}
