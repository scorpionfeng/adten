package com.xtooltech.adten.common.obd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import com.xtooltech.adten.common.db.OBDDataUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class OBDReadAllData {

	public static List<HashMap<String, String>> mOilMouseUnitIDAndNameSets;

	public static StringHash dpDataBaseObj = new StringHash();
	public static String[] carTypeNames;
	public static List<HashMap<String, Object>> mCarIDTypeIconSets;

	public static ArrayList<HashMap<String, String>> mCommandSets;
	public static HashMap<String, String> dpDataBaseText = new HashMap<String, String>();

	public static int language = 0;


	/******************
	 * ʵ��DAO��
	 *****************/

	/**************
	 * 
	 * @param name
	 * @param mContext
	 **************/
	public static void showPairHelpDialog(String name, final Context mContext) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle("XTOOL BlueTooth Help");
		dialog.setMessage(name);
		String backStr = TextString.cancle;
		String pairStr = TextString.pair;
		dialog.setPositiveButton(pairStr,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent bluetoothPair = new Intent(
								Settings.ACTION_BLUETOOTH_SETTINGS);
						((Activity) mContext).startActivity(bluetoothPair);

						dialog.cancel();
					}
				});
		dialog.setNegativeButton(backStr,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		dialog.show();
	}

	private static AlertDialog.Builder builder;


	/****************
	 * 
	 ***************/

	/***********************************
	 * 
	 ***********************************/

	/**************************
	 *
	 **************************/
	public static void getOilMouseUnitSets() {
		mOilMouseUnitIDAndNameSets = new ArrayList<HashMap<String, String>>();
		int mOilMouseUnitIDAndNameSetCounts = OBDDataUtil.mOilMouseUnitIDArray.length;
		for (int i = 0; i < mOilMouseUnitIDAndNameSetCounts; i++) {
			HashMap<String, String> mHashMap = new HashMap<String, String>();
			mHashMap.put("OilMouseUnitId", OBDDataUtil.mOilMouseUnitIDArray[i]);// ID
			try {
				mHashMap.put(
						"OilMouseUnitName",
						DataBaseBin.searchText(
								OBDDataUtil.mOilMouseUnitIDArray[i])
								.textORhelp());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mOilMouseUnitIDAndNameSets.add(mHashMap);
		}
	}

	/********************
	 *
	 *******************/
	public static void getCommandSets() {
		int commandLength = OBDDataUtil.itemsID.length;
		mCommandSets = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < commandLength; i++) {
			HashMap<String, String> mCommandItem = new HashMap<String, String>();
			String id = "";
				id = OBDDataUtil.chemiItemsId[i];

			String name = null;
			String unit = null;
			try {
				DS_File dtcFile = DataBaseBin.searchDS(id);
				name = dtcFile.dsName();
				unit = dtcFile.dsUnit();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mCommandItem.put("id", id);
			mCommandItem.put("name", name);
			mCommandItem.put("unit", unit);
			mCommandSets.add(mCommandItem);
		}
	}

}
