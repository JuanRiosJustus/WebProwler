package webprowler.utility;

public class URLHandler 
{
	// TODO check to see if a given string is related to the current web-site were traversing 
	public static boolean isRelated(String originalURL)
	{
		//TODO is associate with the determine host method
		
		return true;
	}
	
	// TODO base means in determining which sites we traverse to - needs to be added to web-site object
	public static boolean determineHost(String url)
	{
		//TODO determine the raw host name for the web-site i.e. https://www.google.com/  ---> google
		
		return true;
	}
	
	/**
	 * Need to determine if the current URL is a valid URL for a child
	 * @param url the URL we're basing our condition off of 
	 * @return true if and only if the current URL doesn't contain "google", " ", "".
	 */
	public static boolean isValid(String url)
	{
		if (url.contains(" ") || url.contains("google")) { return false; }
		
		return true; 
	}
}
