package com.xtooltech.adtenx.common.obd;

public class Short2HexUtil {
	public static String short2Hex(short[] temp) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < temp.length; i++) {
//			if (temp[i] < 16) {
//				sb.append("0x0").append(Integer.toHexString(temp[i]))
//						.append(",");
//			} else {
//				sb.append("0x").append(Integer.toHexString(temp[i]))
//						.append(",");
//			}
			sb.append(String.format("0x%02x", temp[i])).append(",");
		}
		return sb.toString();

	}
	public static String short2Hex(short[] temp, int index) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i <index; i++) {
			if (temp[i] < 16) {
				sb.append("0x0").append(Integer.toHexString(temp[i]))
						.append(",");
			} else {
				sb.append("0x").append(Integer.toHexString(temp[i]))
						.append(",");
			}
		}
		return sb.toString();

	}
	public static String byte2Hex(byte[] temp) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < temp.length; i++) {
			sb.append(String.format("%02X", temp[i]&0xff)+",");
		}
		return sb.toString();

	}
}
