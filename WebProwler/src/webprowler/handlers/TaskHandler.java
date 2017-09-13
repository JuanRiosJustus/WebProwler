package webprowler.handlers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskHandler 
{
	private static ObservableList<String> entryPoints = FXCollections.observableArrayList(); 
	private static ObservableList<String> limits = FXCollections.observableArrayList();
	static 
	{
		entryPoints.add("Google");
		entryPoints.add("Reddit");
		entryPoints.add("Amazon");
		entryPoints.add("Yahoo Answers");
		
		limits.add("10000000");
		limits.add("1000000");
		limits.add("100000");
		limits.add("10000");
		limits.add("1000");
		limits.add("100");
		limits.add("10");
	}
	
	public static ObservableList<String> getEntryPoints() { return entryPoints; }
	public static ObservableList<String> getLimits() { return limits; }
	/* Used to get the amount from the JComboBox */
	public static int limit (Object size) { return Integer.valueOf((String)size); }
}
