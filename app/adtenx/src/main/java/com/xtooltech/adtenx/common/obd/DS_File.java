package com.xtooltech.adtenx.common.obd;

public class DS_File {

	private String dsName = new String();
	private String dsUnit = new String();
	private String dsFormat = new String();
	private DataArray dsCommand = new DataArray();
	private boolean isSearch = false;
	private String searchID = new String();
	private String dsAlgorithm = new String();
	public void setSearchID(String searchid){
		searchID = searchid;
	}
	public String searchID(){
		return searchID;
	}
	public void setDSAlgorithm(String alg){
		dsAlgorithm = alg;
	}
	public String dsAlgorithm(){
		return dsAlgorithm;
	}
	public void setIsSearch(boolean is){
		isSearch = is;
	}
	public boolean isSearch(){
		return  isSearch;
	}	
	public void setDSName(String ds){
		dsName = ds;
	}
	public String dsName (){
		return dsName;
	}
	public void setDSUnit(String unit){
		dsUnit = unit;
	}
	public String dsUnit(){
		return dsUnit;
	}
	public void setDSFormat(String format){
		dsFormat = format;
	}
	public String dsFormat(){
		return dsFormat;
	}
	public void setDSCommand(DataArray command){
		dsCommand = command;
	}
	public DataArray dsCommand(){
		return dsCommand;
	}
	public String getDsName() {
		return dsName;
	}
	public void setDsName(String dsName) {
		this.dsName = dsName;
	}
	public String getDsUnit() {
		return dsUnit;
	}
	public void setDsUnit(String dsUnit) {
		this.dsUnit = dsUnit;
	}
	public DataArray getDsCommand() {
		return dsCommand;
	}
	public void setDsCommand(DataArray dsCommand) {
		this.dsCommand = dsCommand;
	}
	public String getDsAlgorithm() {
		return dsAlgorithm;
	}
	public void setDsAlgorithm(String dsAlgorithm) {
		this.dsAlgorithm = dsAlgorithm;
	}
	public String getSearchID() {
		return searchID;
	}

}
