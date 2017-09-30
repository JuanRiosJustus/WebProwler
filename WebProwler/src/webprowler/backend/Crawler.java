package webprowler.backend;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Crawler 
{
	private static Document document;
	private static Elements links;
	private static Elements images;
	private static String tags;
	private static String description;
	private static String address;
	
	/**
	 * Constructor for the crawler object.
	 * @param url The url  represented by a String.
	 */
	public Crawler(String url)
	{
		crawl(url);
	}
	
	/**
	 * The operation used to grab all the information of the given url.
	 * @param url The url used to gather all of the information off of.
	 */
	private static void crawl(String url)
	{
		try {
			address = url.replace(" ", "+");
			document = Jsoup.connect(address)
					.get();
			
			try { links = document.select("a[href]"); } catch (Exception e) { }
			try { images = document.select("img"); } catch (Exception e) { }
			try { tags = document.select("meta[name=keywords]").first().attr("content"); } catch (Exception e) { }
			try { description = document.select("meta[name=description]").get(0).attr("content"); } catch (Exception e) { }
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	String getDocumentTitle() { return document.title(); }
	String getdocumentText() { return document.text(); }
	Elements getLinks() { return links; }
	Elements getImages() { return images; }
	String getTags() { return tags; }
	String getDescription() { return description; }
	String getAddress() { return address; }
}
