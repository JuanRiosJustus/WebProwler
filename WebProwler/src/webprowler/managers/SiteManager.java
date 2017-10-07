package webprowler.managers;

public class SiteManager 
{
	private static String[] invalidChildren = new String[6];
	
	static 
	{
		// Illegal Childsite extensions and characters
		invalidChildren[0] = ".pdf";
		invalidChildren[1] = ".jpg";
		invalidChildren[2] = ".png";
		invalidChildren[3] = ".mov";
		invalidChildren[4] = ".svg";
		invalidChildren[5] = "?";
	}
	/**
	 * Need to determine if the current URL is a valid URL for a child
	 * @param url the URL we're basing our condition off of 
	 * @return true if and only if the current URL doesn't contain "google", " ", "".
	 */
	public static boolean isValidChild(String url)
	{
		if (url.equals("") || url.equals(" ") || url.contains("mailto")) { return false; }
		for (String s : invalidChildren)  { if (url.contains(s)) { return false; } }
		
		return true; 
	}
}
