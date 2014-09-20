package parser;

import java.util.ArrayList;
import java.util.HashMap;

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
	Document siteDownload;
	boolean download;
	ArrayList<HashMap<String, String>> downloadOptions = new ArrayList<HashMap<String, String>>();

	/**
	 * An object which downloads and parses a mod page, optionally downloading the mod.
	 * @param page the URL for which the mod is located on on curseforge
	 * @param repo the repository this mod should have as a parent
	 * @param download whether or not to download the mod's jar file
	 */
	public PageParser(String page, Repository repo, boolean download) {
		this.page = page;
		this.mod = new MCPackage(repo);
		this.download = download;
	}

	/**
	 * Initiates the downloading and parsing of the mod
	 * @return the MCPackage object for the parsed package
	 */
	public ArrayList<MCPackage> parse(String wantedTypes) {
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
		parseLicense();
		parseLogo();

		parseDownloadList();

		//Make a list of packages, each referring to a different download option.
		ArrayList<MCPackage> options = new ArrayList<MCPackage>();
		for (int i = 0; i < downloadOptions.size(); i++) {
			MCPackage pack = parseDownload(i);
			options.add(pack);
		}

		ArrayList<MCPackage> choosen = new ArrayList<MCPackage>();
		if (wantedTypes == "all")
			choosen = options;
		else if (wantedTypes == "one") {
			//TODO: choose one package fitting each release type
			//and return those only.
			//See checkContains Below.
		}

		ArrayList<MCPackage> newChoosen = choosen;
		for (MCPackage pack : choosen) {
			//download any that we need to download
			if (download)
				downloadMod(pack.getFileName(), "/mods", pack);
			//If any of the packs are not complete, remove it from our list of chosen mods.
			if (pack.checkValidity() != true)
				choosen.remove(newChoosen);
		}

		return newChoosen;
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
		String ele = site.select("div.avatar-wrapper a").attr("href");
		//TODO:  logo download
		//We need to download the mod's image, since they will be fetched from the repo itself.
		mod.setLogo(ele);
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
		//Sadly, not sure we can do this automatically:(
	}

	private void parseLicense() {
		mod.setLicense(
				site.select("[data-title=Project License] span").html(),
				site.select("li a.modal-link truncate").attr("href") );
	}

	/******************************************
	 *          Download download list
	 *****************************************/

	private void parseDownloadList() {
		this.siteDownload = ListCrawler.download(page + "/files");
		Elements ele = siteDownload.select("tr.project-file-list-item");
		for (Element e : ele) {
			HashMap<String, String> downloadOption = new HashMap<String, String>();  
			downloadOption.put("file", e.select("td.project-file-name div a").attr("href") + "/download");
			downloadOption.put("size", e.select("td.project-file-size").html() );
			downloadOption.put("date", e.select("td.project-file-date-uploaded abbr").html());
			downloadOption.put("version", e.select("td.project-file-game-version span").html());
			downloadOption.put("downloads", e.select("td.project-file-downloads").html());
			downloadOption.put("release", e.select("td.project-file-release-type div").attr("title"));
			//Add the Hashmap (downloadOption) to the list of downloadOptions
			downloadOptions.add(downloadOption);
		}
	}

	/******************************************
	 *            After downloadList
	 *****************************************/

	private MCPackage parseDownload(int num) {
		if (downloadOptions.size() >= num + 1) {
			//TODO: Not sure about size index starting at 0 or not.
			HashMap<String, String> downloadOption = this.downloadOptions.get(num);
			MCPackage pack = mod;
			//TODO: pack.setSize(Integer.parseInt(downloadOption.get("size")) );
			//TODO: downloadOption.get("file");
			pack.setReleaseDate(downloadOption.get("date") );
			pack.setVersion(downloadOption.get("version") );
			pack.setReleaseType(downloadOption.get("release") );
			
			return pack;
		} else
			return null;
	}

	private void downloadMod(String link, String dir, MCPackage pack) {
		//TODO: Download (link)
		//TODO: Save to (dir)
		//TODO: Update filename in (pack)
	}
}