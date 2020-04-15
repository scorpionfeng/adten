package com.xtooltech.adtenx.common.obd;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class CurrentData {
	
	
	

	private static String handwareSerial = new String();
	public static ArrayList<Short> ECUlist = new ArrayList<Short>();

	public static boolean isExsit = false;
	public static boolean isEnterErr = false; //
	public static boolean isEnterSucc = false; // ��ֵΪtrueʱ��ʾ���ӳɹ������

	// VW engine
	public static String vwEngine = new String();
	// 循环读数据时候，判断是否终止
	public static boolean isEnd = false;
	// �Ƿ�����iOBD2��ʹ�ø�ֵ�ж�
	public static int vehicleType = 0;
	public static boolean isSendReceive = false;
	public static boolean isConnectDevice = false;
	public static boolean isStopSendReceive = false;
	// 设置温度过高报警标志
	public static boolean isWaterAlarm = false;
	// 设置超速行驶报警标志
	public static boolean isSpeedAlarm = false;
	// 设置疲劳驾驶报警标志
	public static boolean isFatgureAlarm = false;

	// public static boolean cancel = false;
	// honda本田相关数据
	public static int hondaSysBps = 0;
	public static String hondaActiveCommand = new String();
	public static DataArray hondaMask = new DataArray();
	// honda打包类型
	public static int packType = 0;
	public static List<Frame> hondaDsId = new ArrayList<Frame>();
	// opel EcuId
	public static int opelEcuId = 0;
	// 进入协议类型
	public static int enterMode = -1;

	
	
	

	// SN
	public static String SN = "";
	public static boolean isCarInfoUpdateSuccess = false;// 判断车辆信息是否已经上传成功
	public static boolean isNewCar = false;// 判断是否更换车辆
	public static boolean isNewSN = false;// 判断是否更换了OBD2
	// installAPP参数
	public static String appCode = "";
	public static double lon = 0;// 经度
	public static double lat = 0;// 纬度
	public static boolean isMapGet = false;
	public static String country = "";// 国家
	public static String localTime = "";// 本地时间
	public static String state = "";//
	public static String city = "";//
	public static String address = "";//
	public static String osType = "";//
	public static String appVersion = "";//
	public static String appFrom = "";//
	public static boolean isLocationSend = false;// 位置信息是否已经上传
	public static boolean isWifiUpdateSucc = false;
	public static boolean exit = false;// 设置是否退出
	public static boolean isStopAlarm = false;
	public static boolean isTimeoutException = false;// 是否通讯超时
	public static int screenWidth=0;
	public static int screenHeight=0;

	{
		localTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		localTime = localTime.replace(' ', 'T');
	}

	public static void readHandwareSerial(String serial) {
		handwareSerial = serial;
	}

	public static List<Frame> getHondaDsId() {
		return hondaDsId;
	}

	public static void setHondaDsId(List<Frame> hondaDsId) {
		CurrentData.hondaDsId = hondaDsId;
	}

	public static String handwareSerial() {
		return handwareSerial;
	}

	public static void addECUlist(short ecuID) {
		// if (!ECUlist.contains(ecuID)) {
		ECUlist.add(ecuID);
		// }
	}

	public static ArrayList<Short> getECUlist() {
		ArrayList<Short> retECUlist = new ArrayList<Short>();
		for (int i = 0; i < ECUlist.size(); i++) {
			retECUlist.add(ECUlist.get(i));
		}
		return listSort(retECUlist);
	}

	public static void initECUlist() {
		ECUlist.clear();
	}

	public static int getECUcount() {
		return ECUlist.size();
	}

	private static ArrayList<Short> listSort(ArrayList<Short> list) { // ����Ҫ���򣬻���Ҫȷ��Ψһ��
		if ((list != null) && list.size() > 0) {
			short ecuTemp = 0;
			for (int i = 0; i < list.size() - 1; i++) { // ECU���մ�С����
				for (int j = i + 1; j < list.size(); j++) {
					if (list.get(i) > list.get(j)) {
						ecuTemp = list.get(i);
						list.set(i, list.get(j));
						list.set(j, ecuTemp);
					}
				}
			}
		}
		return list;
	}
/**
 * 单位选择
 * @param initUnit
 * @return
 * @throws UnsupportedEncodingException
 */
	public static String unitChoose(String initUnit)
			throws UnsupportedEncodingException { //

		String ret = new String();
		//
		if (initUnit == null)
			return ret;
		if (initUnit.equalsIgnoreCase(TextString.celsius)
				|| initUnit.equalsIgnoreCase(TextString.fahrenheit)) {// 摄氏度
			if (DataStream.temperatureUnit == 0) {
				ret = TextString.celsius;
			}// 摄氏度
			else {
				ret = TextString.fahrenheit;
			}// 费氏度
		}

		else if (initUnit.equalsIgnoreCase(TextString.Km)
				|| initUnit.equalsIgnoreCase(TextString.Mile)) {// Km
			if (DataStream.distanceUnit == 0) {
				ret = TextString.Km;
			}// Km
			else {
				ret = TextString.Mile;
			}// mile
		}

		else if (initUnit.equalsIgnoreCase(TextString.Km_h)
				|| initUnit.equalsIgnoreCase(TextString.Mile_h)) {// km/h
			if (DataStream.speedUnit == 0) {
				ret = TextString.Km_h;
			}// km/h
			else {
				ret = TextString.Mile_h;
			}// mile/h
		}

		else if (initUnit.equalsIgnoreCase(TextString.L_100Km)
				|| initUnit.equalsIgnoreCase(TextString.mile_gal_US)
				|| initUnit.equalsIgnoreCase(TextString.mile_gal_UK)) { // L/100km
			if (DataStream.fuelUnit == 0) {
				ret = TextString.L_100Km;
			}// L/100Km
			else if (DataStream.fuelUnit == 1) {
				ret = TextString.mile_gal_US;
			}// mile/gal(US)
			else if (DataStream.fuelUnit == 2) {
				ret = TextString.mile_gal_UK;
			}// mile/gal(UK)
		}

		else if (initUnit.equalsIgnoreCase(TextString.Nm)
				|| initUnit.equalsIgnoreCase(TextString.kg_m)
				|| initUnit.equalsIgnoreCase(TextString.lb_ft)) {// Nm
			if (DataStream.torqueUnit == 0) {
				ret = TextString.Nm;
			}// Nm
			else if (DataStream.torqueUnit == 1) {
				ret = TextString.kg_m;
			}// "kg-m"
			else if (DataStream.torqueUnit == 2) {
				ret = TextString.lb_ft;
			}// "lb-ft"
		}

		else if (initUnit.equalsIgnoreCase(TextString.PS)
				|| initUnit.equalsIgnoreCase(TextString.hp)) {// PS
			if (DataStream.horsepowerUnit == 0) {
				ret = TextString.PS;
			}// PS
			else if (DataStream.horsepowerUnit == 1) {
				ret = TextString.hp;
			}// "hp"
		} else if (initUnit.equalsIgnoreCase(TextString.kpa)) {
			if (DataStream.presseureUnit == 0) {
				ret = TextString.kpa;
			} else if (DataStream.presseureUnit == 1) {
				ret = TextString.PSI;
			}
		} else if (initUnit.equalsIgnoreCase("L/h")
				|| initUnit.equalsIgnoreCase("gal(US)/h")
				|| initUnit.equalsIgnoreCase("gal(UK)/h")) {
			if (DataStream.fuelOilUnit == 0) {
				ret = initUnit;
			} else if (DataStream.fuelOilUnit == 1) {
				ret = "gal(US)/h";// us
			} else if (DataStream.fuelOilUnit == 2) {
				ret = "gal(UK)/h";// uk
			}
		} else {
			ret = initUnit;
		} // ��
		return ret;
	}
}
