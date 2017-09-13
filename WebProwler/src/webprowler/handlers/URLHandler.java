package webprowler.handlers;

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
	// TODO
	/**
	 * Determine if the current url is not a usable format for a Childsite
	 * @param url The String representation of a URL
	 * @return true If and only if the URL is a valid child
	 */
	public static boolean isLegalChild(String url)
	{
		if (url.toLowerCase().endsWith(".pdf") || url.toLowerCase().endsWith(".jpg") || url.toLowerCase().endsWith(".png")
				|| url.toLowerCase().endsWith(".mov") || url.endsWith("#") || url.toLowerCase().contains("lang=")
				|| url.toLowerCase().endsWith(".svg") || url.length() > 100 || url.equals("") || url.equals(" "))
		{
			return false;
		}
		
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
