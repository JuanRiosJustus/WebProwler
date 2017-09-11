package webprowler.objects;

public class Childsite extends Website 
{
	private String title = "";
	private String description = "";
	private String keywords = "";
	private String text = "";
	private String[] children;
	private String[] resources;
	
	public Childsite(String nonSearchableURL, String searchableURL) 
	{
		super(nonSearchableURL, nonSearchableURL);
		// TODO Auto-generated constructor stub
	}
	/* GET THE WEBSTIE */
	
	public String getDescripton() { return description; }
	public String[] getResources() { return resources; }
	public String[] getChildren() { return children; }
	public String getKeywords() { return keywords; }
	public String getTitle() { return title; }
	public String getText() { return text; }
	// PUublic String get
	/* SET THE WEBSTIE */
	public void setDescripton(String description) { this.description = this.description + description; }
	public void setKeywords(String keywords) { this.keywords = this.keywords + keywords; }
	public void setResources(String[] resources) { this.resources = resources;}
	public void setSite(String siteURL) { this.nonSearchableURL = siteURL; }
	public void setChildren(String[] children) { this.children = children; }
	public void setTitle(String title) { this.title = this.title + title; }
	public void setText(String text) { this.text = this.text + text; }

	
}
