package webprowler.objects;

public class Childsite extends Website 
{
	private String title = "";
	private String description = "";
	private String keywords = "";
	
	public Childsite(String absoluteSite, String siteQuery) 
	{
		super(absoluteSite, siteQuery);
		// TODO Auto-generated constructor stub
	}
	/* SET THE WEBSTIE SUMMARY */
	public String getDescripton() { return description; }
	public String getKeywords() { return keywords; }
	public String getTitle() { return title; }
}
