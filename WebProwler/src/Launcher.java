import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import webprowler.gui.Window;
import webprowler.objects.ProtectedSites;

public class Launcher 
{
	private static ProtectedSites sites = new ProtectedSites();
	
	public static void main (String[] args)
	{
		ProtectedSites.getParents();
		HashSet<String> parents = new HashSet<String>();
		
		/*
		Scanner userInput = new Scanner(System.in);
		System.out.println("Eter somehting between" + ProtectedSites.getParents().size());
		
		
		int index = userInput.nextInt();
		*/
		
		Window window = new Window();
		
		String urlEntry = ProtectedSites.getParents().get(2).getNonSearchableURL();
		
		// attempt to connect to url
		try {
			Document doc = Jsoup.connect(urlEntry).get();
			Elements links = doc.select("a[href]");
			
			// print out all links found
			for (int index = 0; index < links.size(); index++)
			{
				System.out.println(links.get(index).attr("abs:href"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
