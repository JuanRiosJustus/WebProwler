package webprowler.utility;

public class URLHandler 
{
	
	// TODO check to see if child is the same as parent
	public static boolean isAssociated(String parentURL, String childURL)
	{
		if (parentURL.equals(childURL)) return true;
		
		// the child is hopfully and ususally larger thant he parent
		int index = 0;
		while (index < parentURL.length())
		{
			if (parentURL.charAt(index) != childURL.charAt(index))
			{
				return false;
			}
			index++;
		}
		
			
		return true;
	}
}
