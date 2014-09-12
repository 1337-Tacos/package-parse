package main;

import java.util.ArrayList;

import core.MCPackage;
import parser.ListCrawler;
import parser.PageParser;

public class Manager {
	ListCrawler crawler = new ListCrawler("updated");
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
			PageParser parser = new PageParser(page);
			ArrayList<MCPackage> packs = parser.parse();
			mods.addAll(packs);
		}
		System.out.println("done");
		//Deal with packages
	}
}