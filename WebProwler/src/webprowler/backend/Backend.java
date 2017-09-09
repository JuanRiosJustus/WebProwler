package webprowler.backend;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Backend 
{
	Document doc;
	Elements links;
	String description;
	String tags;
	
	/**
	 * Get the entry url
	 * @param entryUrl url to start crawling from.
	 */
	public void connect(String entryUrl)
	{
		//Childsite child = new Childsite();
		try {
			doc = Jsoup.connect(entryUrl).get();
			try { links = doc.select("a[href]"); } catch (Exception e) { }
			try { description = doc.select("meta[name=description]").get(0).attr("content"); } catch (Exception e) { }
			try { tags = doc.select("meta[name=keywords]").first().attr("content"); } catch (Exception e) { }
			
			String title = doc.title();
			String[] something = doc.text().split(" & ");
			
			System.out.println("Meta Description: " + description);
			System.out.println("Meta Tags: " + tags);
			System.out.println("Meta Title: " + title);
			System.out.println("Meta check: " + something);

			
			for (int index = 0; index < something.length; index++)
			{
				System.out.println(something[index]);
				//System.out.println(links.get(index).attr("abs:href"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
