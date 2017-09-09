package webprowler.objects;

public class Parentsite extends Website 
{
	private String site = "";
	
	public Parentsite(String nonSearchableURL, String searchableURL, String site) 
	{
		super(nonSearchableURL, searchableURL);
		this.site = this.site + site;
		// TODO Auto-generated constructor stub
	}
	/* RETURN THE NAME OF THE SITE*/
	public String getSiteName() { return site; }
}
