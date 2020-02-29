package com.xtooltech.adten.common.ble

 const val REQUEST_COARSE_LOCATION = 0x0001
 const val REQUEST_ENABLE_BLUETOOTH = 0x0002
 const val REQUEST_EXTERNAL_STORAGE = 0x0003

const val DETECT_RSSI = "DETECT_RSSI"

const val SCAN_RSSI = "SCAN_RSSI"
const val DEFAULT_SCAN_RSSI = "-50"




const val PID_RPM =0x0C
const val PID_VSS =0x0D
const val PID_MAF =0x10
const val PID_MAP =0x0B
const val PID_IAT =0x0F
const val PID_RUNTM =0x1F
const val PID_LOAD_PCT =0x04
const val PID_ECT =0x05
const val PID_TP =0x11
const val PID_MIL_DIST =0x21
const val PID_FLI =0x2F
const val PID_CLR_DIST =0x31
const val PID_VPWR =0x42
const val PID_OBDSUP =0x1C
const val PID_DTC_MIL =0x01

const val PID_FORWARD_A =0x34
const val PID_FORWARD_V =0x24
const val PID_AirCoefficient =0x9E

const val PID_ENG_TORQUE_V =0x8E

const val PID_Engact_torque =0x62 //实际的发动机扭矩百分比
const val PID_Engref_torque =0x63 //发动机参考扭矩
const val PID_Egr_cmd =0x2C //EGR开度 %
const val PID_Fuel_con =0x5E //油耗量 L/100Km
const val PID_Boopres_sa =0x70  //增压压力传感器A Kpa
const val PID_Boopres_sb =0x70  //增压压力传感器B Kpa
const val PID_Noxcon_b1s1 =0x83 //氮氧化合物传感器浓度-缸组1, 传感器1 ppm
const val PID_Noxcon_b1s2 =0x83 //氮氧化合物传感器浓度-缸组1, 传感器2 ppm
const val PID_Noxcon_b2s1 =0x83 //氮氧化合物传感器浓度-缸组2, 传感器1 ppm
const val PID_Noxcon_b2s2 =0x83 //氮氧化合物传感器浓度-缸组2, 传感器2 ppm
const val PID_Dpf_presb1 =0x7A //柴油机微粒过滤器缸组1压力差 Kpa
const val PID_Fuel_pre =0x23//油轨压力 bar
const val PID_Outtemp_b1s1 =0x78 //废气温度-缸组1, 传感器1
const val PID_Outtemp_b1s2 =0x78 //废气温度-缸组1, 传感器2
const val PID_Outtemp_b1s3 =0x78 //废气温度-缸组1, 传感器3
const val PID_Outtemp_b1s4 =0x78 //废气温度-缸组1, 传感器4
const val PID_Fuel_Air_Ratio =0x44  //燃油/空气指令的当量比 %
const val PID_Eng_power =0xFF //发动机功率 kw
const val PID_EngOilTemp =0x5C //发动机油温   Engine Oil Temperature