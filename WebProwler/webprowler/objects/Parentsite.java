package webprowler.objects;

public class Parentsite extends Website 
{
	private String descriptor = "";
	
	public Parentsite(String nonSearchableURL, String searchableURL, String descriptor) 
	{
		super(nonSearchableURL, searchableURL);
		this.descriptor = this.descriptor + descriptor;
		// TODO Auto-generated constructor stub
	}
	/* RETURN THE NAME OF THE SITE*/
	public String getDescriptor() { return descriptor; }
}
