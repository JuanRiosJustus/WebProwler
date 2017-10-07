package webprowler.backend;

import java.io.PrintWriter;

import webprowler.frontend.MainWindow;
import webprowler.objects.Childsite;

public class Printer 
{
	public static void print(String filePathAndName)
	{
		String document = "------ PAGES CONTAINING POTENTIAL INFORMATION ON: " + MainWindow.getTargetTerm() + " ------\r\n";
		for (Childsite child : Database.database_collection())
		{
			if (child.getScore() > 0)
			{
				document = document + " " + child.getWebsiteURL() + " " + child.getScore() + "\r\n";
				System.out.println(document);
			}
		}
		findAndCreate(document, filePathAndName);
	}
	private static void findAndCreate(String str, String filePathAndName)
	{
		try {
			PrintWriter out = new PrintWriter(filePathAndName);
			out.print(str);
			out.close();
			System.out.println("WAS SUCCESSFUL");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
