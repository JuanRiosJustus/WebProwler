package webprowler.objects;

public class Childsite
{
	/* Child identifiers*/
	private String domainURL;
	private String websiteURL;
	
	/* Contents */
	private String title;
	private String description;
	private String keywords;
	private String text;
	private String[] children;
	
	private String entirety;
	
	/* TABLE VARIABLES*/
	private int score;
	
	public Childsite(String websiteURL, String domainURL) 
	{
		this.websiteURL = websiteURL;
		this.domainURL = domainURL;
	}
	
	/* WEBSTIE ACCESSORS ATTRIBUTES*/
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	public String getDescripton() { return description; }
	public void setDescripton(String description) { this.description = description; }
	
	public String getKeywords() { return keywords; }
	public void setKeywords(String keywords) { this.keywords = keywords; }
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	public String getDomainURL() { return domainURL; }
	public void setDomainURL(String domain) { this.domainURL = domain; }
	
	public String getWebsiteURL() { return websiteURL; }
	public void setWebsiteURL(String websiteURL) { this.websiteURL = websiteURL; } 
	
	public int getScore() { return score; }
	public void setScore(int score) { this.score = score; }
	
	public String[] getChildren() { return children; }
	public void setChildren(String[] children) { this.children = children; }
	
	public String getEntirety() { return entirety; }
	public void unifyAllText() { entirety = title + description + keywords + text; }
}
