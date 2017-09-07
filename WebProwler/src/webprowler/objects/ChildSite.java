package webprowler.objects;

public class ChildSite extends Website 
{
	StringBuilder summary = new StringBuilder();
	
	public ChildSite(String absoluteSite, String siteQuery) 
	{
		super(absoluteSite, siteQuery);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unused")
	private void setSummary(String summary)
	{
		this.summary.append(summary);
	}
}
