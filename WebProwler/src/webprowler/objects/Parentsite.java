package webprowler.objects;

public class Parentsite extends Website 
{
	private String domain = "";
	
	public Parentsite(String nonSearchableURL, String searchableURL, String domain) 
	{
		super(nonSearchableURL, searchableURL);
		this.domain = this.domain + domain;
		// TODO Auto-generated constructor stub
	}
	/* RETURN THE NAME OF THE SITE*/
	public String getDomain() { return domain; }
}
