package com.xtooltech.adtenx.common.obd;

import com.xtooltech.adtenx.common.obd.DataArray;

import java.util.ArrayList;

public class Frame {
	private ArrayList<DataArray> frame = new ArrayList<DataArray>();


	public Frame() {
	}

	public Frame(int capacity) {
		frame = new ArrayList<DataArray>(capacity);
	}

	public Frame(String... data) {
		for (String oneData : data) {
			frame.add(new DataArray(oneData));
		}
	}

	public Frame(DataArray addDataArray) {
		frame.add(addDataArray);
	}

	public void add(DataArray data) {
		frame.add(data);
	}

//	public void add(DataArray dataArray, short index) {
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("index", String.format("0x%04X", index));
//		map.put("dataArray", dataArray.binaryToCommand());
//		frameIndex.add(map);
//	}
//
//	public void add(DataArray dataArray, DataArray index) {
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("index", index.binaryToCommand());
//		map.put("dataArray", dataArray.binaryToCommand());
//		frameIndex.add(map);
//	}
//
//	public void add(String dataArray, int index) {
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("dataArray", dataArray);
//		map.put("index", String.format("0x%04X", index));
//		frameIndex.add(map);
//	}
//
//	public void add(String dataArray, short index) {
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("dataArray", dataArray);
//		map.put("index", String.format("0x%04X", index));
//		frameIndex.add(map);
//	}
//
//	public void add(String dataArray, DataArray index) {
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("dataArray", dataArray);
//		map.put("index", index.binaryToCommand());
//		frameIndex.add(map);
//	}
//
//	public void add(DataArray dataArray, String command) {
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("command", command);
//		map.put("dataArray", dataArray.binaryToCommand());
//		frameIndex.add(map);
//	}
//
//	public String getCommand(int index) {
//		return frameIndex.get(index).get("command");
//
//	}
//
//	public short getIndexShort(int index) {
//		return Short.parseShort(frameIndex.get(index).get("index"));
//	}
//
//	public int getIndexInt(int index) {
//		return Integer.parseInt(frameIndex.get(index).get("index"));
//	}
//
//	public DataArray getIndexDataArray(int index) {
//		return new DataArray(frameIndex.get(index).get("dataArray"));
//
//	}

	public void add(String str) {
		frame.add(new DataArray(str));
	}

	public DataArray get(int index) {
		return frame.get(index);
	}

	public short get(int index, int pos) {
		return frame.get(index).get(pos);
	}

	public int count() {
		return frame.size();
	}

	public void clear() {
		frame.clear();
	}

	public boolean isEmpty() {
		return frame.isEmpty();
	}

	public String print() {
		StringBuffer strBuffer = new StringBuffer();
		for (int i = 0; i < frame.size(); i++) {
			strBuffer.append(frame.get(i));
			if (i != frame.size() - 1)
				strBuffer.append("\n");
		}
		return strBuffer.toString();
	}

	public void setData(DataArray array, int index) {
		// TODO Auto-generated method stub
		frame.set(index, array);
	}
}
