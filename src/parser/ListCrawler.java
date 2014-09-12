package parser;

import java.io.IOException;
import java.util.ArrayList;

import main.Manager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ListCrawler {
	String sort;
	int numPages;
	
	public ListCrawler(String sort) {
		this.sort = sort;
		
		this.numPages = findNumPages();
	}

	public ArrayList<String> crawl() {
		ArrayList<String> pages = new ArrayList<String>();
		for (int i = 1; i < numPages; i++) {
			Elements mods = download("http://minecraft.curseforge.com/mc-mods?filter-sort=popularity&page=" + i).select(".e-avatar64 ");
			for (Element mod: mods) {
				pages.add(mod.attr("href"));
			}
		}
		return pages;
	}
	
	private int findNumPages() {
		if (Manager.DEBUG)
			return 2;
		Elements pagination = download("http://minecraft.curseforge.com/mc-mods?page=9999").select("span.s-active");
		Element thePaginator = pagination.get(0);
		String theNumber = thePaginator.html();
		return Integer.parseInt(theNumber);
	}
	
	private Document download(String page) {
		Document doc = new Document("lol?");
		try {
			doc = Jsoup.connect(page).get();
		} catch (IOException e) {
			System.out.println(page + " Could not be downloaded. :(");
			return doc;
		}
		
		return doc;
	}

}