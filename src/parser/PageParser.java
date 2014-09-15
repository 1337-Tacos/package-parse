package parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import core.MCPackage;
import core.Repository;

public class PageParser {
	MCPackage mod;
	String page;
	Repository repo;
	Document site;
	boolean download;

	/**
	 * @param page
	 * @param repo
	 * @param download
	 */
	public PageParser(String page, Repository repo, boolean download) {
		this.page = page;
		this.mod = new MCPackage(repo);
		this.download = download;
	}

	public MCPackage parse() {
		//Download the mod page.
		site = ListCrawler.download(page);

		//Actually do the parsing now.
		parseDescription();
		parseName();
		parseLogo();
		parseID();
		parseHomepage();
		parseTags();
		parseAuthor();

		//Download the download page
		//TODO: download download page

		parseSize();
		parseVersion();

		if (download)
			downloadMod();

		//Now, make sure that we have at least the bare minimum information.
		if (mod.checkValidity())
			return mod;
		else
			return null;
	}

	private void parseDescription() {
		Elements ele = site.select(".project-description");
		for (Element e : ele)
			mod.setDescription(e.html());
	}

	private void parseName() {
		Elements ele = site.select("h1.project-title");
		for (Element e : ele)
			mod.setName(e.select("a").html());
	}

	private void parseLogo() {
		Elements ele = site.select("div.avatar-wrapper");
		for (Element e : ele)
			//TODO:  logo download
			//We need to download the mod's image, since they will be fetched from the repo itself.
			ele = null;	
	}

	/**
	 * Parses the Unique ID of the mod.
	 * Make sure Name is set before calling this function
	 */
	private void parseID() {
		if (mod.getName() != null && mod.getName() != "") {
			String id = mod.getName().toLowerCase();
			//Strip Bad Characters
			mod.setID(id);
		}
	}

	/**
	 * Attempts to parse the mod's tags.
	 * This uses JSoup's wonderful HTML parsing to attempt to find any categories the mod is listed under.
	 */
	private void parseTags() {
		Elements ele = site.select("div.cf-sidebar-wrapper");
		//for each possible sidebar element
		for (Element e : ele) {
			//If this is true, we have found the correct sidebar group.
			//Now, we just need to find the actual categories listed within it.
			if (e.select("h3").html() == "Categories") {
				Elements eleDetail = e.select("li a:last-child");
				//assuming last-child works how I think it should, this returns the li element with the answer in it
				for (Element eDetail : eleDetail) {
					mod.addTag(eDetail.html() );
				}
			}
		}
	}

	private void parseAuthor() {
		Elements ele = site.select("span.owner");
		for (Element e : ele) {
			mod.setAuthor(e.parent().select("a").html() );
		}
	}

	private void parseHomepage() {
		
	}



	private void parseVersion() {
		//Parse Version once decided which one
	}

	private void parseSize() {
		//Parse Size
	}

	private void downloadMod() {
		//TODO:  download mod
		//Parse FileName
	}
}