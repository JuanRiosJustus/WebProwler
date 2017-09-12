package webprowler.utility;

public class TaskHandler 
{
	private static String[] tasks = new String[4];
	private static String[] queryLimit = new String[7];
	static 
	{
		tasks[0] = "Mindlessley Explore the web";
		tasks[1] = "Explore the web for pictures";
		tasks[2] = "Explore the web for terms";
		tasks[3] = "Explore the web for answers";
		
		queryLimit[0] = "10000000";
		queryLimit[1] = "1000000";
		queryLimit[2] = "100000";
		queryLimit[3] = "10000";
		queryLimit[4] = "1000";
		queryLimit[5] = "100";
		queryLimit[6] = "10";
	}
	
	public static String[] getTasks() { return tasks; }
	public static String[] getLimit() { return queryLimit; }
	/* Used to get the amount from the JComboBox */
	public static int limit (Object size) { return Integer.valueOf((String)size); }
}
