package parser;

import java.util.ArrayList;

import core.MCPackage;
import core.Repository;

public class PageParser {
	MCPackage mod;
	String page;
	
	public PageParser(String page, Repository repo) {
		this.page = page;
		this.mod = new MCPackage(repo);
	}

	public ArrayList<MCPackage> parse() {
		// TODO Auto-generated method stub
		return null;
	}
}