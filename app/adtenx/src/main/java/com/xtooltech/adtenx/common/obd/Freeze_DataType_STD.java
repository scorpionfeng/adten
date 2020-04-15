package com.xtooltech.adtenx.common.obd;

public class Freeze_DataType_STD {
	private String freezeID = new String();
	private String freezeName = new String();
	private String freezeValue = new String();
	private String freezeUnit = new String();
	private DataArray freezeCommand = new DataArray();
	
	
	public void setFreezeID(String id){
		freezeID = id;
	}
	public String freezeID(){
		return freezeID;
	}
	
	public void setFreezeName(String name){
		freezeName = name;
	}
	public String getFreezeName(){
		return freezeName;
	}
	
	public void setFreezeValue(String value){
		freezeValue = value;
	}
	public String getFreezeValue(){
		return freezeValue;
	}
	
	public void setFreezeUnit(String unit){
		freezeUnit = unit;
	}
	public String getFreezeUnit(){
		return freezeUnit;
	}
	
	public void setFreezeCommand(DataArray command){
		freezeCommand = command;
	}
	public DataArray getFreezeCommand(){
		return freezeCommand;
	}
	

}
