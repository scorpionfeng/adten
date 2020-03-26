package com.xtooltech.adtenx.common.obd;

public class TextAndHelp_File {
	private String textORhelp = new String();
	private boolean isSearch = false;
	private String searchID = new String();
	
	public void setSearchID(String searchid){
		searchID = searchid;
	}
	public String searchID(){
		return searchID;
	}
	public void setIsSearch(boolean is){
		isSearch = is;
	}
	public boolean isSearch(){
		return  isSearch;
	}
	public void setTextOrHelp(String searchText){
		textORhelp = searchText;
	}
	public String textORhelp(){
		return textORhelp;
	}
	

}
