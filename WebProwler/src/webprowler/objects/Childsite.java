package webprowler.objects;

public class Childsite extends Website 
{
	/* INSTANCE VARIABLES */
	private String title;
	private String description;
	private String keywords;
	private String text;
	private String[] children;
	private String[] resources;
	
	private String entirety;
	
	/* TABLE VARIABLES*/
	private String domain;
	private int score;
	
	public Childsite(String nonSearchableURL, String searchableURL) 
	{
		super(nonSearchableURL, nonSearchableURL);
	}
	
	/* GET THE WEBSTIE ATTRIBUTES*/
	public String getDescripton() { return description; }
	public String[] getResources() { return resources; }
	public String[] getChildren() { return children; }
	public String getKeywords() { return keywords; }
	public String getTitle() { return title; }
	public String getText() { return text; }
	public String getSite() { return nonSearchableURL; }
	public String getEntirety() { return entirety; }
	
	public String getDomain() { return domain; }
	public String getURL() { return nonSearchableURL; }
	public int getScore() { return score; }
	
	/* SET THE WEBSTIE ATTRIBUTES */
	public void setDescripton(String description) { this.description = description; }
	public void setKeywords(String keywords) { this.keywords = keywords; }
	public void setResources(String[] resources) { this.resources = resources;}
	public void setSite(String siteURL) { this.nonSearchableURL = siteURL; }
	public void setChildren(String[] children) { this.children = children; }
	public void setTitle(String title) { this.title = title; }
	public void setText(String text) { this.text = text; }
	
	public void setScore(int score) { this.score = score; }
	public void setDomain(String domain) { this.domain = domain; }
	
	public void unifyAllText() 
	{ 
		entirety = title + description + keywords + text; 
	}
	
	
}
