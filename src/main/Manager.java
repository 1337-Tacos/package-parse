package main;

import java.util.ArrayList;

import core.MCPackage;
import core.Repository;
import parser.ListCrawler;
import parser.PageParser;

public class Manager {
	ListCrawler crawler = new ListCrawler("updated");
	Repository repo = new Repository("RENAME ME", "http://www.UPDATE_ME.com");
	ArrayList<String> pages = new ArrayList<String>();
	ArrayList<MCPackage> mods = new ArrayList<MCPackage>();
	//-----------------------------------------------------
	//-----------------------------------------------------
	public static final boolean DEBUG = true;
	//-----------------------------------------------------
	//-----------------------------------------------------
	public static void main(String[] args) {
		Manager man = new Manager();
		man.run();
	}
	
	public void run() {
		pages = crawler.crawl();
		for (String page : pages) {
			//TODO:  make downloading optional
			PageParser parser = new PageParser(page, repo, true);
			MCPackage pack = parser.parse();
			mods.add(pack);
		}
		System.out.println("done");
		//Deal with packages
	}
}