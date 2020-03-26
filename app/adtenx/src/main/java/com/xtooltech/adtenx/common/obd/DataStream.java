package com.xtooltech.adtenx.common.obd;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DataStream {

	public static final int DS_SUPPORT = 0x01; // ֧�ָ������
	public static final int DS_NO_SUPPORT = 0x00; // ��֧�ָ������
	public static final int DS_SPECIAL = 0x02; // �踴�ϼ���
	// 新加的求油耗
	public static long endTime;// 最后读取的行驶时间

	public static int rpm = 0;
	public static int airPressure = 0;
	public static int airTemperature = 0;
	// public static boolean isTimeStarted = false;// 已经获取了启始时间
	public static int temperatureUnit = 0; // �¶ȵ�λ
	public static int distanceUnit = 0; // ��̵�λ
	public static int speedUnit = 0; // �ٶȵ�λ
	public static int fuelOilUnit = 0; // ȼ�͵�λ
	public static int engineType = 0; // ���������ͣ�0Ϊ���� 1Ϊ����
	public static int fuelUnit = 0; // �ͺĵ�λ
	public static int torqueUnit = 0; // Ť�ص�λ
	public static int horsepowerUnit = 0; // ������λ
	public static int weightUnit = 0; // ������λ
	public static int presseureUnit = 0; // ѹ����λ
	public static final float KAPPRESURE = 101.3f;// 1标准大气压＝101.3 kpa
	public static final float PSIPRESURE = 14.7f;// 1标准大气压＝14.65 PSI
	public static float oilCost;// 即时油耗
	public static float fuelAdjustment = 0.0F; // �ͺ�ϵ��
	public static float speedAdjustment = 0.0F; // ����ϵ��
	public static int weight = 0; // ����
	public static float displacement = 0.0F; // ����

	public static float currentSpeed = 0.0F;
	public static float airFlow = 0.0F;
	public static float lastSpeed = 0.0F;
	public static float _distance = 0.0F;
	public static float distance = 0.0F;// �ۼ����
	public static float _fuelConsumption = 0.0F;
	public static float fuelConsumption = 0.0F;
	public static long startTime = 0;
	public static float accel = 0.0F;
	public static long lastTime = 0;
	public static boolean isGetStartTime = false;
	public static boolean isTestPerformance = false;// 是否性能测试
	public static long timeDvalue = 0; // ���γ��ټ����ʱ���
	public static long lastTimeAccel = 0;

	public static long addSubTime; // �Ӽ��ٲ���ʱ���¼
	public static int asstartSpeed = 0; // �Ӽ��ٲ�����ʼ�ٶȺ���ֹ�ٶ� �����ȡ
	public static int asendSpeed = 0;

	public static long zeroTo400Time; // 0-400ʱ���¼
	public static boolean asStartDistance = true; // �Ӽ��ٲ��ԣ���֤��ʼ����ʱ����ʼʱ�����ʼ��̾�ֻ��¼һ�Σ����ò��Է���ǰ����Ϊtrue
	public static boolean asStartTime = true;
	public static boolean z4StartDistance = true;
	public static boolean z4StartTime = true;
	public static boolean asTestStart = false; // �жϲ����Ƿ�ʼ
	public static short speedECU = 0;
	public static short rpmECU = 0;
	public static float addSubRealDistance = 0.0F;
	public static long addSubStartTime = 0;
	public static float addSubStartDistance = 0.0F;
	public static long addSubRealTime = 0;

	public static long zeroTo400StartTime = 0;
	public static long zeroTo400RealTime = 0;
	public static float zeroTo400RealDistance = 0.0F;
	public static float zeroTo400StartDistance = 0.0F;
	public static boolean testTimeStart = false;
	public static String tempVoltate = "0";
	public static boolean isChangeMode = true;

	public static ArrayList<HashMap<String, Object>> stuatuSendItems = null; // �ҵ��Ǳ�
																				// ����������

	public static boolean isSupportAirFlow = false; // �Ƿ�֧�֡�����������

	public static String commonCalcExpress(String dataStreamId, DataArray data)
			throws UnsupportedEncodingException {
		String ret = "";
		short[] buffer = new short[1024];// MAX_DATA_SIZE
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = 0;
		}
		for (int j = 0; j < data.length(); j++) {
			buffer[j] = (short) (data.get(j)&0xff);
		}
		if (dataStreamId.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x01")) {
			ret = "";
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x02")) {
			StringBuffer retStr = new StringBuffer();
			int count = (data.length() - 2) / 2;
			for (int i = 0; i < count; i++) {
				int dtcId = (buffer[2 + i] << 8) + buffer[3 + i];
				if (dtcId == 0)
					break;
				if (dtcId < 0x4000) { // P
					retStr.append(String.format("P%04X", dtcId));
				} else if (dtcId < 0x8000) { // C
					retStr.append(String.format("C%04X", (dtcId - 0x4000)));
				} else if (dtcId < 0xC000) { // B
					retStr.append(String.format("B%04X", (dtcId - 0x8000)));
				} else { // U
					retStr.append(String.format("U%04X", (dtcId - 0xC000)));
				}
			}
			ret = retStr.toString();
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x03")) {
			switch (buffer[2]) {
			case 0x01:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x1B")
						.textORhelp();
				break;
			case 0x02:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x1C")
						.textORhelp();
				break;
			case 0x04:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x1D")
						.textORhelp();
				break;
			case 0x08:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x1E")
						.textORhelp();
				break;
			case 0x10:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x1F")
						.textORhelp();
				break;
			case 0xFF:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x1B")
						.textORhelp();
				break;
			default:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x2A")
						.textORhelp();
				break;
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x03")) {
			switch (buffer[3]) {
			case 0x01:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x1B")
						.textORhelp();
				break;
			case 0x02:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x1C")
						.textORhelp();
				break;
			case 0x04:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x1D")
						.textORhelp();
				break;
			case 0x08:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x1E")
						.textORhelp();
				break;
			case 0x10:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x1F")
						.textORhelp();
				break;
			case 0xFF:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x1B")
						.textORhelp();
				break;
			default:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x2A")
						.textORhelp();
				break;
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x04")) {
			ret = String.format("%.1f", buffer[2] * 100.0 / 255);
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x05")) {

			if (temperatureUnit == 0) {
				ret = String.format("%d", buffer[2] - 40);
			} else {
				ret = String.format("%d",
						((Number) ((buffer[2] - 40) * 1.8 + 32)).intValue());
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x06")) {
			ret = String.format("%4.1f", buffer[2] * 199.2 / 255 - 100);
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x07")) {
			ret = String.format("%4.1f", buffer[2] * 199.2 / 255 - 100);
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x08")) {
			ret = String.format("%4.1f", buffer[2] * 199.2 / 255 - 100);
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x09")) {
			ret = String.format("%4.1f", buffer[2] * 199.2 / 255 - 100);
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x0A")) {
			if (presseureUnit == 0) {
				ret = String.format("%d", buffer[2]);
			} else {
				ret = String.format("%d", (buffer[2] * 1000 + 6895 / 2) / 6895);
			}
			// 进气岐管绝对压力
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x0B")) {
			// kpa
			if (presseureUnit == 0) {
				airPressure = buffer[2];
				ret = String.format("%d", airPressure);
			} else {
				// psi
				airPressure = (buffer[2] * 1000 + 6895 / 2) / 6895;
				ret = String.format("%d", airPressure);
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x0C")) {
			rpm = (buffer[2] * 64 + buffer[3] * 64 / 255);
			rpm = (rpm > 7800) ? 7800 : rpm;
			ret = String.format("%d", rpm);
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x0D")) {
			if (speedUnit == 0) {
				ret = String.format("%.1f", buffer[2] * speedAdjustment);
			} else {
				ret = String.format("%.1f", buffer[2] * speedAdjustment
						/ 1.6093);
			}
			lastSpeed = currentSpeed; // ��ȡ�ϴε��ٶ�
			if (buffer[2] == 0xFF) {
				currentSpeed = 0;
			} else {
				if(speedUnit == 0){
					currentSpeed = buffer[2] * speedAdjustment; // �������ʱ��ȡ��ǰ����
				}else{
					currentSpeed = (float) (buffer[2] * speedAdjustment / 1.6093); // �������ʱ��ȡ��ǰ����
				}
			}
			if (isGetStartTime || isTestPerformance) { // ���롰�ҵ��Ǳ?��startTimeֻ��ȡһ�Ρ�
				Date getStart = new Date();
				startTime = getStart.getTime();
				isGetStartTime = false;
			}
			Date myDateBeforeGet = new Date();
			long nowTime = myDateBeforeGet.getTime();
			if (lastTime == 0) {
				lastTime = System.currentTimeMillis();
			}
			_distance = currentSpeed
					* ((nowTime - lastTime) / 1000.0F / 3600.0F);
			timeDvalue = nowTime - lastTime; // ƽ���ͺļ�����Ҫʱ���
			if ((lastTime > 0.0) && ((nowTime - lastTime) > 0)) {
				distance += _distance;
			}
			Date myDate = new Date();
			lastTime = myDate.getTime();
			endTime = lastTime;// 最后读取的行驶时间
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x0E")) {
			ret = String.format("%4.1f", buffer[2] * 127.5 / 255 - 64);
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x0F")) {
			if (temperatureUnit == 0) {
				ret = String.format("%d", buffer[2] - 40);
			} else {
				ret = String.format("%d",
						((Number) ((buffer[2] - 40) * 1.8 + 32)).intValue());
			}
			airTemperature = buffer[2] - 40;
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x10")) {// 空气流量
			if (isSupportAirFlow) {
				airFlow = (buffer[2] * 652.8F / 255 + buffer[3] * 2.5F / 255); // 支持空气流量时
			} else {
				if (engineType == 0) {
					airFlow = (float) ((9.6898 / 1000 * rpm * airPressure
							/ (airTemperature + 273.15) * displacement * 0.85) * 10657.5 / 3600);
					// 10657.5=14.7 // */ 0.725 // 1000
				} else {
					airFlow = (float) ((8.513 / 1000 * rpm * airPressure
							/ (airTemperature + 273.15) * displacement * 0.85) * 12298 / 3600);// 12298=14.3*0.86/1000
				}
			}
			ret = String.format("%.2f", airFlow);
			if (airFlow == 0) { // ƽ���ͺļ���
				if ((lastTime > 0) && (timeDvalue > 0) && (distance > 0)) {
					fuelConsumption += 0;
				}
			} else if (currentSpeed >= 5 || rpm > 900 && currentSpeed > 0) {
				float trendsCon = (fuelAdjustment * airFlow * 33.77903f / currentSpeed); // L/100km//
																							// //��̬�ͺ�
																							// 33.779=3.6F
																							// /
																							// 14.7F/
																							// 0.725F
																							// *
																							// 100
				if ((lastTime > 0.0) && (timeDvalue > 0) && (distance > 0)) {
					_fuelConsumption = trendsCon * (_distance / 100);
					fuelConsumption += _fuelConsumption;
				}
			} else {
				float staticCon = 0.0f;
				if (engineType == 0) {
					staticCon = (float) (airFlow / 14.7 / 0.725 / 1000 * 3600); // L/h
				} else {
					staticCon = (float) (airFlow / 14.3 / 0.86 / 1000 * 3600); // L/h
				} // 怠速油耗
				if ((lastTime > 0.0) && (timeDvalue > 0) && (distance > 0)) {
					_fuelConsumption = staticCon
							* ((float) timeDvalue / 1000 / 3600);
					fuelConsumption += _fuelConsumption;
				}
			}

		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x11")) {
			ret = String.format("%4.1f",
					((Number) (buffer[2] * 100.0 / 255)).floatValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x12")) {
			switch (buffer[2]) {
			case 0x01:
			case 0xFF:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x2B")
						.textORhelp();
				break;
			case 0x02:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x2C")
						.textORhelp();
				break;
			case 0x04:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x2D")
						.textORhelp();
				break;
			default:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x2E")
						.textORhelp();
				break;
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x13")) {
			if ((buffer[2] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0D")
						.textORhelp();
			} else if ((buffer[2] & 0x02) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0E")
						.textORhelp();
			} else if ((buffer[2] & 0x04) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0F")
						.textORhelp();
			} else if ((buffer[2] & 0x08) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x01,0x00")
						.textORhelp();
			} else if ((buffer[2] & 0x10) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x01,0x01")
						.textORhelp();
			} else if ((buffer[2] & 0x20) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x01,0x02")
						.textORhelp();
			} else if ((buffer[2] & 0x40) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x01,0x03")
						.textORhelp();
			} else if ((buffer[2] & 0x80) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x01,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x2A")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x14")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x14")) {
			ret = String.format("%5.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x14")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x14")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x15")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x15")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x15")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x15")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x16")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x16")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x16")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x16")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x17")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x17")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x17")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x17")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x18")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x18")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x18")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x18")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x19")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x19")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x19")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x19")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x1A")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x1A")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x1A")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x1A")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x1B")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x1B")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x1B")) {
			ret = String.format("%4.2f",
					((Number) (buffer[2] * 0.005)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x1B")) {
			ret = String.format("%3.1f",
					((Number) (buffer[3] * 199.2 / 255 - 100)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x1C")) {
			switch (buffer[2]) {
			case 0x00:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x20")
						.textORhelp();
				break;
			case 0x01:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x21")
						.textORhelp();
				break;
			case 0x02:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x22")
						.textORhelp();
				break;
			case 0x03:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x23")
						.textORhelp();
				break;
			case 0x04:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x24")
						.textORhelp();
				break;
			case 0x05:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x25")
						.textORhelp();
				break;
			case 0x06:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x26")
						.textORhelp();
				break;
			case 0x07:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x27")
						.textORhelp();
				break;
			case 0x08:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x28")
						.textORhelp();
				break;
			case 0x09:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x29")
						.textORhelp();
				break;
			default:
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x2A")
						.textORhelp();
				break;
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x1D")) {
			if ((buffer[2] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0D")
						.textORhelp();
			} else if ((buffer[2] & 0x02) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0E")
						.textORhelp();
			} else if ((buffer[2] & 0x04) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x01,0x01")
						.textORhelp();
			} else if ((buffer[2] & 0x08) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x01,0x02")
						.textORhelp();
			} else if ((buffer[2] & 0x10) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x01,0x05")
						.textORhelp();
			} else if ((buffer[2] & 0x20) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x01,0x06")
						.textORhelp();
			} else if ((buffer[2] & 0x40) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x01,0x07")
						.textORhelp();
			} else if ((buffer[2] & 0x80) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x01,0x08")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x2A")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x1E")) {
			if ((buffer[2] & 0x01) != 0)
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x02")
						.textORhelp();
			else
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x01")
						.textORhelp();
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x1F")) {
			ret = String.format("%d",
					((Number) (buffer[2] * 256 + buffer[3])).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x21")) {
			if (distanceUnit == 0) {
				ret = String.format("%d",
						((Number) (buffer[2] * 256 + buffer[3])).intValue());
			} else {
				ret = String.format("%.1f",
						((Number) ((buffer[2] * 256 + buffer[3]) / 1.6093))
								.doubleValue());
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x22")) {
			if (presseureUnit == 0) {
				ret = String.format("%d",
						((Number) (((buffer[2] * 256 + buffer[3]) * 0.079)))
								.intValue());
			} else if (presseureUnit == 1) {
				ret = String
						.format("%d",
								((Number) (((buffer[2] * 256 + buffer[3]) * 0.079) / 6.895))
										.intValue());
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x23")) {
			if (presseureUnit == 0) {
				ret = String.format("%d",
						((Number) (((buffer[2] * 256 + buffer[3]) * 10)))
								.intValue());
			} else if (presseureUnit == 1) {
				ret = String
						.format("%d",
								((Number) (((buffer[2] * 256 + buffer[3]) * 10 * 1000 + 6895 / 2) / 6895))
										.intValue());
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x24")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x24")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x24")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x24")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x25")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x25")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x25")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x25")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x26")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x26")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x26")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x26")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x27")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x27")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x27")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x27")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x28")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x28")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x28")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x28")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x29")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x29")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x29")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x29")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x2A")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x2A")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x2A")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x2A")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x2B")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x2B")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x2B")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x2B")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[4] * 256 + buffer[5]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x2C")) {
			ret = String.format("%.1f",
					((Number) (buffer[2] * 100.0 / 255)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x2D")) {
			ret = String.format("%.1f",
					((Number) (buffer[2] * 100.0 / 128)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x2E")) {
			ret = String.format("%.1f",
					((Number) (buffer[2] * 100.0 / 255)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x2F")) {
			ret = String.format("%.1f",
					((Number) (buffer[2] * 100.0 / 255)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x30")) {
			ret = String.format("%d", ((Number) buffer[2]).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x31")) {
			if (distanceUnit == 0) {
				ret = String.format("%d",
						((Number) (buffer[2] * 256 + buffer[3])).intValue());
			} else {
				ret = String.format("%.1f",
						((Number) ((buffer[2] * 256 + buffer[3]) / 1.6093))
								.doubleValue());
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x32")) {
			ret = String.format("%.2f",
					((Number) ((buffer[2] * 256 + buffer[3]) * 0.25))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x33")) {

			if (presseureUnit == 0) {
				ret = String.format("%d", ((Number) buffer[2]).intValue());
			} else if (presseureUnit == 1) {
				ret = String.format("%d",
						((Number) ((buffer[2] * 1000 + 6895 / 2) / 6895))
								.intValue());
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x34")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x34")) {
			ret = String.format("%4.2f",
					(buffer[4] * 256 + buffer[5]) * 0.00390625 - 128);
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x34")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x34")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x35")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x35")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x35")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x35")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x36")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x36")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x36")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x36")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x37")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x37")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x37")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x37")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x38")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x38")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x38")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x38")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x39")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x39")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x39")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x39")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x3A")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x3A")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x3A")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x3A")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x3B")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x01,0x3B")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x02,0x3B")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[2] * 1.991 + buffer[3] * 0.008) / 255))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x03,0x3B")) {
			ret = String
					.format("%4.2f",
							((Number) ((buffer[4] * 256 + buffer[5]) * 0.00390625 - 128))
									.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x3C")) {
			if (temperatureUnit == 0) {
				ret = String.format("%.1f", ((Number) (buffer[2] * 25.5
						+ buffer[3] * 0.1 - 40)).doubleValue());
			} else {
				ret = String.format("%.1f", ((Number) ((buffer[2] * 25.5
						+ buffer[3] * 0.1 - 40) * 1.8 + 32)).doubleValue());
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x3D")) {
			if (temperatureUnit == 0) {
				ret = String.format("%.1f", ((Number) (buffer[2] * 25.5
						+ buffer[3] * 0.1 - 40)).doubleValue());
			} else {
				ret = String.format("%.1f", ((Number) ((buffer[2] * 25.5
						+ buffer[3] * 0.1 - 40) * 1.8 + 32)).doubleValue());
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x3E")) {
			if (temperatureUnit == 0) {
				ret = String.format("%.1f", ((Number) (buffer[2] * 25.5
						+ buffer[3] * 0.1 - 40)).doubleValue());
			} else {
				ret = String.format("%.1f", ((Number) ((buffer[2] * 25.5
						+ buffer[3] * 0.1 - 40) * 1.8 + 32)).doubleValue());
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x3F")) {
			if (temperatureUnit == 0) {
				ret = String.format("%.1f", ((Number) (buffer[2] * 25.5
						+ buffer[3] * 0.1 - 40)).doubleValue());
			} else {
				ret = String.format("%.1f", ((Number) ((buffer[2] * 25.5
						+ buffer[3] * 0.1 - 40) * 1.8 + 32)).doubleValue());
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x71,0X41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x72,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x73,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x74,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x75,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x76,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x77,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x78,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x79,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x7A,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x7B,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x7C,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x7D,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x7E,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x7F,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x80,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x81,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x82,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x83,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x84,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x85,0x41")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x04")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x03")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x42")) {
			ret = String.format("%5.2f",
					((Number) (buffer[2] * 0.255 + buffer[3] * 0.001))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x43")) {
			ret = String.format("%3.1f",
					((Number) (buffer[2] * 100.0 + buffer[3] / 255.0))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x44")) {
			ret = String.format("%.2f",
					((Number) (buffer[2] * 0.0077775 + buffer[3] * 0.0000305))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x45")) {
			ret = String.format("%4.1f",
					((Number) (buffer[2] * 100.0 / 255)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x46")) {
			if (temperatureUnit == 0) {
				ret = String.format("%d",
						((Number) (buffer[2] - 40)).intValue());
			} else {
				ret = String.format("%d",
						((Number) ((buffer[2] - 40) * 1.8 + 32)).intValue());
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x47")) {
			ret = String.format("%4.1f",
					((Number) (buffer[2] * 100.0 / 255)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x48")) {
			ret = String.format("%4.1f",
					((Number) (buffer[2] * 100.0 / 255)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x49")) {
			ret = String.format("%4.1f",
					((Number) (buffer[2] * 100.0 / 255)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x4A")) {
			ret = String.format("%4.1f",
					((Number) (buffer[2] * 100.0 / 255)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x4B")) {
			ret = String.format("%4.1f",
					((Number) (buffer[2] * 100.0 / 255)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x4C")) {
			ret = String.format("%4.1f",
					((Number) (buffer[2] * 100.0 / 255)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x4D")) {
			ret = String.format("%4.1f",
					((Number) (buffer[2] * 100.0 / 255)).doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x00,0x4E")) {
			ret = String.format("%d",
					((Number) (buffer[2] * 256 + buffer[3])).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x01")) {
			ret = String.format("%d",
					((Number) (buffer[0] * 256 + buffer[1])).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x02")) {
			ret = String.format("%5.1f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.1))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x03")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x04")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x05")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.0000305))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x06")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.000305))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x07")) {
			ret = String.format("%d",
					((Number) (buffer[0] * 256 + buffer[1])).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x08")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x09")) {
			ret = String.format("%d",
					((Number) (buffer[0] * 256 + buffer[1])).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x0A")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.000122))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x0B")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x0C")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x0D")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.00390625))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x0E")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x0F")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x10")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x11")) {
			ret = String.format("%4.1f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.1))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x12")) {
			ret = String.format("%d",
					((Number) (buffer[0] * 256 + buffer[1])).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x13")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x14")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x15")) {
			ret = String.format("%d",
					((Number) (buffer[0] * 256 + buffer[1])).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x16")) {
			ret = String.format("%d",
					((Number) (buffer[0] * 256 + buffer[1] - 40)).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x17")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x18")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.012))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x19")) {
			ret = String.format("%7.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.079))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x1A")) {
			ret = String.format("%d",
					((Number) (buffer[0] * 256 + buffer[1])).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x1B")) {
			ret = String.format("%d",
					((Number) ((buffer[0] * 256 + buffer[1]) * 10)).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x1C")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x1D")) {
			ret = String.format("%5.1f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.5))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x1E")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.0000305))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x1F")) {
			ret = String.format("%6.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.05))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x20")) {
			ret = String.format("%6.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.004))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x21")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x22")) {
			ret = String.format("%d",
					((Number) (buffer[0] * 256 + buffer[1])).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x23")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x24")) {
			ret = String.format("%d",
					((Number) (buffer[0] * 256 + buffer[1])).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x25")) {
			ret = String.format("%d",
					((Number) (buffer[0] * 256 + buffer[1])).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x26")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.0001))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x27")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x28")) {
			ret = String.format("%d",
					((Number) (buffer[0] * 256 + buffer[1])).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x29")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.00025))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x2A")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x2B")) {
			ret = String.format("%d",
					((Number) (buffer[0] * 256 + buffer[1])).intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x2C")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x2D")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x2E")) {
			if ((buffer[0] * 256 + buffer[1]) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x06")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x05")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x2F")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x30")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.001526))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x31")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x32")) {
			ret = String.format("%5.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.0007747))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x33")) {
			ret = String.format("%4.2f",
					((Number) ((buffer[0] * 256 + buffer[1]) * 0.00024414))
							.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x81")) {
			ret = String
					.format("%d",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000))).intValue()
									: ((Number) ((buffer[0] * 256 + buffer[1])))
											.intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x82")) {
			ret = String
					.format("%5.1f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.1))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.1))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x83")) {
			ret = String
					.format("%5.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 00.1))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x84")) {
			ret = String
					.format("%5.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.001))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x85")) {
			ret = String
					.format("%5.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.0000305))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.0000305))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x86")) {
			ret = String
					.format("%5.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.000305))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.000305))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x8A")) {
			ret = String
					.format("%5.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.122))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.122))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x8B")) {
			ret = String
					.format("%5.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.001))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x8C")) {
			ret = String
					.format("%5.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.01))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x8D")) {
			ret = String
					.format("%6.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.00390625))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.00390625))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x8E")) {
			ret = String
					.format("%5.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.001))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x90")) {
			ret = String
					.format("%5.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.001))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x96")) {
			ret = String
					.format("%5.1f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.1))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.1))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x9C")) {
			ret = String
					.format("%5.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.01))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0x9D")) {
			ret = String
					.format("%5.1f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.5))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.5))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0xA8")) {
			ret = String
					.format("%d",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000))).intValue()
									: ((Number) ((buffer[0] * 256 + buffer[1])))
											.intValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0xA9")) {
			ret = String
					.format("%6.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.25))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.25))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0xAF")) {
			ret = String
					.format("%5.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.01))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.01))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0xB0")) {
			ret = String
					.format("%5.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.001))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0xFD")) {
			ret = String
					.format("%5.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.001))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.001))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x00,0x10,0xFE")) {
			ret = String
					.format("%6.2f",
							(buffer[0] * 256 + buffer[1]) > 0x7FFF ? ((Number) ((buffer[0]
									* 256 + buffer[1] - 0x10000) * 0.25))
									.doubleValue()
									: ((Number) ((buffer[0] * 256 + buffer[1]) * 0.25))
											.doubleValue());
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x01")) {
			if ((buffer[2] & 0x80) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x08")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x07")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x02")) {
			if ((buffer[3] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0A")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x09")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x03")) {
			if ((buffer[3] & 0x02) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0A")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x09")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x04")) {
			if ((buffer[3] & 0x04) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0A")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x09")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x05")) {
			if ((buffer[3] & 0x10) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0C")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0B")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x06")) {
			if ((buffer[3] & 0x20) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0C")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0B")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x07")) {
			if ((buffer[3] & 0x40) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0C")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0B")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x08")) {
			if ((buffer[4] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0A")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x09")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x09")) {
			if ((buffer[4] & 0x02) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0A")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x09")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x0A")) {
			if ((buffer[4] & 0x04) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0A")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x09")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x0B")) {
			if ((buffer[4] & 0x08) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0A")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x09")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x0C")) {
			if ((buffer[4] & 0x10) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0A")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x09")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x0D")) {
			if ((buffer[4] & 0x20) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0A")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x09")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x0E")) {
			if ((buffer[4] & 0x40) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0A")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x09")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x0F")) {
			if ((buffer[4] & 0x80) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0A")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x09")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x10")) {
			if ((buffer[5] & 0x01) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0C")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0B")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x11")) {
			if ((buffer[5] & 0x02) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0C")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0B")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x12")) {
			if ((buffer[5] & 0x04) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0C")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0B")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x13")) {
			if ((buffer[5] & 0x08) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0C")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0B")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x14")) {
			if ((buffer[5] & 0x10) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0C")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0B")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x15")) {
			if ((buffer[5] & 0x20) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0C")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0B")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x16")) {
			if ((buffer[5] & 0x40) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0C")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0B")
						.textORhelp();
			}
		} else if (dataStreamId
				.equalsIgnoreCase("0x00,0x00,0x00,0x01,0x00,0x17")) {
			if ((buffer[5] & 0x80) != 0) {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0C")
						.textORhelp();
			} else {
				ret = DataBaseBin.searchText("0x00,0x00,0x00,0x02,0x00,0x0B")
						.textORhelp();
			}
		} else {
			ret = DataBaseBin.searchText("0x00,0x00,0x00,0x00,0x00,0x2A")
					.textORhelp();
		} // N/A
		return ret;
	}

}
