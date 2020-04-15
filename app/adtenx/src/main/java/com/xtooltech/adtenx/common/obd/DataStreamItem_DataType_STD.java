package com.xtooltech.adtenx.common.obd;

public class DataStreamItem_DataType_STD {

	private DataArray dsID = new DataArray();
	private short sysID = 0;
	private String dsUnit = new String();
	private DataArray dsCMD = new DataArray();
	private String dsValue = new String();
	private int supportType = 0;
	private String dsName = new String();
	private DataArray index = new DataArray();
	private int indexInt=0;
	public DataArray getIndex() {
		return index;
	}

	public void setIndex(DataArray index) {
		this.index = index;
	}

	public DataStreamItem_DataType_STD() {

	}

	public int getIndexInt() {
		return indexInt;
	}

	public void setIndexInt(int indexInt) {
		this.indexInt = indexInt;
	}

	public DataStreamItem_DataType_STD(String dsID) {
		this.dsID = new DataArray(dsID);
		this.dsValue = TextString.N_A;
	}

	public DataStreamItem_DataType_STD(DataArray dsID) {
		this.dsID = dsID;
		this.dsValue = TextString.N_A;
	}

	public String getDsName() {
		return dsName;
	}

	public void setDsName(String dsName) {
		this.dsName = dsName;
	}

	public int getSupportType() {
		return supportType;
	}

	public void setSupportType(int supportType) { // 0x00 ��֧�� 0x01 ֧�� 0x02
													// ���ϼ���
													// ����ֵ��Ч�����⴫�����
		if ((supportType == 0x00) || (supportType == 0x01)
				|| (supportType == 0x02)) {
			this.supportType = supportType;
		}
	}

	public DataArray getDsID() {
		return dsID;
	}

	public void setDsID(DataArray dsID) {
		this.dsID = dsID;
	}

	public void setDsID(String dsID) {
		this.dsID = new DataArray(dsID);
	}

	public short getSysID() {
		return sysID;
	}

	public void setSysID(short sysID) {
		this.sysID = sysID;
	}

	public String getDsUnit() {
		return dsUnit;
	}

	public void setDsUnit(String dsUnit) {
		this.dsUnit = dsUnit;
	}

	public DataArray getDsCMD() {
		return dsCMD;
	}

	public void setDsCMD(DataArray dsCMD) {
		this.dsCMD = dsCMD;
	}

	public String getDsValue() {
		return dsValue;
	}

	public void setDsValue(String dsValue) {
		this.dsValue = dsValue;
	}

}
