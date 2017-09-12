package webprowler.utility;

public class TASKHandler 
{
	private static String[] tasks = new String[4];
	private static String[] queryLimit = new String[5];
	static 
	{
		tasks[0] = "Mindlessley Explore the web";
		tasks[1] = "Explore the web for pictures";
		tasks[2] = "Explore the web for terms";
		tasks[3] = "Explore the web for answers";
		
		queryLimit[0] = "1000000 (resullts)";
		queryLimit[1] = "100000 (results)";
		queryLimit[2] = "10000 (results)";
		queryLimit[3] = "1000 (results)";
		queryLimit[4] = "100 (results)";
	}
	
	public static String[] getTasks() { return tasks; }
	public static String[] getLimit() { return queryLimit; }
	// TODO (7) create a way to determine the amount of searches we should get
	public static int limit (String size) { return Integer.valueOf((size.replace("[^0-9]", "")));  }
}
