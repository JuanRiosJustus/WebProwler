package webprowler.backend;

import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import webprowler.gui.Window;
import webprowler.objects.Childsite;
import webprowler.objects.Entries;
import webprowler.objects.Website;

public class Backend 
{
	/* INSTANCE VARIABLES */
	Document document;
	Elements links;
	Elements resources;
	String description;
	String tags;
	
	/**
	 * Get the entry url
	 * @param entryUrl url to start crawling from.
	 */
	public void test(String entryUrl)
	{
		//Child-site child = new Child-site();
		try {
			document = Jsoup.connect(entryUrl).get();
			try { links = document.select("a[href]"); } catch (Exception e) { }
			try { description = document.select("meta[name=description]").get(0).attr("content"); } catch (Exception e) { }
			try { tags = document.select("meta[name=keywords]").first().attr("content"); } catch (Exception e) { }
			
			String title = document.title();
			//String[] something = document.text().split(" & ");
			
			System.out.println("Meta Description: " + description);
			System.out.println("Meta Tags: " + tags);
			System.out.println("Meta Title: " + title);
			//System.out.println("Meta check: " + something);
			System.out.println("Entered: " + entryUrl);
			
			for (int index = 0; index < links.size(); index++)
			{
				//System.out.println(something[index]);
				System.out.println(links.get(index).attr("abs:href"));
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * Create a new Child-site based off given url.
	 * @param url - A url represented by a String to base child-site off of
	 * @return New and fully functional Child-site object
	 */
	public Childsite createAndConnect(String url)
	{
		ArrayList<String> children = new ArrayList<String>();
		Childsite site = new Childsite("", "");
		
		
		
		try {
			document = Jsoup.connect(url.replace(" ", "%20")).get();
			
			// Get attributes if they exist
			try { links = document.select("a[href]"); } catch (Exception e) { }
			try { tags = document.select("meta[name=keywords]").first().attr("content"); } catch (Exception e) { }
			try { description = document.select("meta[name=description]").get(0).attr("content"); } catch (Exception e) { }
			
			// determine the valid links in this url - disregard whitespace
			for (Element element : links)
			{ 
				if (!element.attr("abs:href").contains(Entries.getGoogleProducts().get(0).getDescriptor()) || !element.attr("abs:href").contains(""))
				{
					children.add(element.attr("abs:href"));
				}
			}
			
			site.setTitle(document.title());
			site.setDescripton(description);
			site.setKeywords(tags);
			site.setText(document.text());
			site.setSite(url.replace(" ", "%20"));
			site.setChildren(children.toArray(new String[children.size()]));
			
			System.out.println("Current site: " + site.getTitle());
			System.out.println("Keywords: " + site.getKeywords());
			System.out.println("Description: " + site.getDescripton());
			System.out.println("Text: " + site.getText());
			System.out.println("Site: " + site.getNonSearchableURL());
			System.out.println("Children: " + Arrays.toString(site.getChildren()));
			
			// TODO accurate way in determining if two url's are related
			for (String iteration : site.getChildren())
			{
				System.out.println(iteration);
				Window.getWaitList().add(new Childsite(iteration, iteration));
			}
			
			//Window.getWaitList().add(site);
			//Window.getStatusDisplay();
			Window.getQueueDisplay();
			System.out.println("Site was loaded to the queue");
			return site;
		} catch (Exception ex) {
			System.out.println(" FATAL ERROR NO SITE LOADED");
			ex.printStackTrace();
			return null;
		}
	}
}
