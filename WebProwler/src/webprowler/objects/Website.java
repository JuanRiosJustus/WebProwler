package webprowler.objects;

public class Website 
{
	protected String nonSearchableURL = "";
	protected String searchableURL = "";
	
	/* CONTSTRUCT THE WEBSITE OBJECT */
	public Website(String nonSearchableURL, String searchableURL)
	{
		this.nonSearchableURL = this.nonSearchableURL + nonSearchableURL;
		this.searchableURL = this.searchableURL + searchableURL;
	}
	/* RETURN THE NON SEARCHABLE (MAIN PAGE) URL */
	public String getNonSearchableURL() { return nonSearchableURL; }
	/* RETURN THE SEARCHABLE (QUERY) URL */
	public String getSearchableURL() { return searchableURL; }
}
