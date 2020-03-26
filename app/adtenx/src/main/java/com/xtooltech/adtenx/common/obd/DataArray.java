package com.xtooltech.adtenx.common.obd;

import java.util.ArrayList;

public class DataArray {
	private ArrayList<Short> array = new ArrayList<Short>();
	String cmd = new String();

	public DataArray() {

	}

	public DataArray(short cmd) {
		array.add(cmd);
	}

	public ArrayList<Short> getArray() {
		return array;
	}

	public void setArray(ArrayList<Short> array) {
		this.array = array;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public DataArray(String str, String cmd) {
		this.add(str);
		this.cmd = cmd;
	}

	public DataArray(short[] buffer, int length) {
		this.add(buffer, length);
	}
	
	public DataArray(byte[] buffer, int len){
		for (int i = 0; i < len; i++) {
			array.add((short)(buffer[i] &0xFF));
		}
	}

	public DataArray(short[] buffer, int range, int length) {
		this.add(buffer, range, length);
	}
	
	public DataArray(byte[] buffer, int range, int length){
		for (int i = range; i < length + range; i++)
			array.add(Short.valueOf((short)(buffer[i]&0xFF)));
	}

	public DataArray(DataArray bin, int range, int length) {
		this.add(bin, range, length);
	}

	public DataArray(DataArray bin) {
		this.add(bin);
	}

	public DataArray(String str) {
		this.add(str);
	}

	// ���һ���ֽ����
	public void add(short value) {
		array.add(Short.valueOf(value));
	}

	// ���һ���ֽ����
	public void add(short[] buffer, int length) {
		for (int i = 0; i < length; i++)
			array.add(Short.valueOf(buffer[i]));
	}
	
	public void add(byte[] buffer,int length){
		for (int i = 0; i < length; i++)
			array.add((short) (buffer[i] & 0xFF));
	}

	public void add(short[] buffer, int range, int length) {
		for (int i = range; i < length + range; i++)
			array.add(Short.valueOf(buffer[i]));
	}

	public void add(DataArray bin, int range, int length) {
		for (int i = range; i < length + range; i++)
			array.add(bin.get(i));
	}

	// �������
	public void add(DataArray bin) {
		for (int i = 0; i < bin.length(); i++)
			array.add(Short.valueOf(bin.get(i)));
	}

	// ����ַ�����
	public void add(String str) {
		this.add(this.stringToShortArray(str));
	}

	// ����ַ�����
	public void add(String str, String cmd) {
		this.add(this.stringToShortArray(str));
	}

	// ȥ��EUC ID RF_UNPACK_SYSID �ڼ���ʱ��ȥ��ID
	public DataArray unpackSysID() {
		short[] buffer = new short[this.array.size() - 1];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = array.get(i + 1);
		}
		return new DataArray(buffer, buffer.length);
	}

	// �õ������
	public int length() {
		return array.size();
	}

	// �õ��ֽ����
	public short get(int index) {
		// �����ȡԽ��
		if (index >= 0 && index < array.size()) {
			return array.get(index);
		} else {
			return 0;
		}
	}

	// ���
	public void clear() {
		array.clear();
	}

	public String print() {
		StringBuffer strBuffer = new StringBuffer();
		for (int i = 0; i < array.size(); i++) {
			strBuffer.append(String.format("0x%X", array.get(i)));
			if (i != array.size() - 1)
				strBuffer.append(",");
		}
		return strBuffer.toString();
	}

	public Boolean equals(short[] buffer, int length) {
		return this.equals(new DataArray(buffer, length));
	}

	public Boolean equals(DataArray bin) {
		Boolean ret = true;
		if (array.size() != bin.length()) {
			ret = false;
		} else {
			for (int i = 0; i < array.size(); i++) {
				if (array.get(i).shortValue() != bin.get(i)) {
					ret = false;
					break;
				}
			}
		}
		return ret;
	}

	public Boolean equals(String str) {
		return this.equals(new DataArray(str));
	}

	// �ַ�ת����
	private DataArray stringToShortArray(String str) {
		if(str==null){
			return new DataArray((short)0);
		}
		DataArray bin = new DataArray();
		String[] strs = str.split(",");
		for (int i = 0; i < strs.length; i++) {
			short temp = 0;
			if (strs[i].trim().startsWith("0x")
					|| strs[i].trim().startsWith("0X")) {
				temp = Integer.valueOf(strs[i].trim().substring(2), 16)
						.shortValue();
			} else {// ͨѶ���ͳһʹ��16����
				if (strs[i].contains(".")) {
					temp=(short) Double.parseDouble(strs[i]);
				} else {
					temp = Integer.valueOf(strs[i].trim(), 16).shortValue();
				}
			}
			bin.add(temp);
		}
		return bin;
	}

	// ת��Ϊ�ַ�
	public String binaryToString() {
		String binStr = array.toString();
		return binStr;
	}

	// ת��Ϊ�����ʽ
	public String binaryToCommand() {
		StringBuffer strBuffCommand = new StringBuffer();

		for (int i = 0; i < array.size(); i++) {
			if ((array.get(i).shortValue()) >= 0x10) {
				strBuffCommand.append("0x");
				strBuffCommand.append(String.format("%2X", array.get(i)
						.shortValue()));
				if (i < array.size() - 1) {
					strBuffCommand.append(",");
				}
			} else {
				strBuffCommand.append("0x0");
				strBuffCommand.append(String.format("%X", array.get(i)
						.shortValue()));
				if (i < array.size() - 1) {
					strBuffCommand.append(",");
				}
			}
		}
		return strBuffCommand.toString();
	}

	public String binaryToFile() {
		StringBuffer strBuffCommand = new StringBuffer();

		for (int i = 0; i < array.size(); i++) {
			strBuffCommand.append(String.format("%02X ", array.get(i)
					.shortValue() & 0xff));
		}
		return strBuffCommand.toString();
	}

	public boolean indexIsEquals(int index, short value) {
		if (array.get(index) == value) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		short[] newArr = new short[array.size()];
		for (int i = 0; i < array.size(); i++) {
			newArr[i] = array.get(i);
		}
		return Short2HexUtil.short2Hex(newArr);
	}

	public void replaceShort(int pos,  short value) {
		array.set(pos, value);
	}
	
	public void add(int index,short s){
		array.add(index, s);
	}
}