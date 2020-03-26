package com.xtooltech.adtenx.common.obd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.InputType;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;


import com.xtooltech.adtenx.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OBDUtil extends Activity {
	public static final String OBD2_SCREEN_SHOT = "obd2ShotFile";
	public static final String ZIP_DIR = "zipObd2File";
	private static WakeLock wl;
	private static Dialog mDialog;
	private static long firClick;
	private static long secClick;
	private static int count;
	private static int a = 0;
	private static int b = 0;
	private static boolean isDel = false;
	public static boolean isFirstUpdateProgress = false;
	private static TextView mProgressTextView;
	public static boolean isDashboard = false;

	private static NotificationManager notificationManager;
	//private static Notification notification;
	private static NotificationCompat.Builder builder;
	private static PendingIntent contentItent;
	private static final int NOTIFICATION_FLAG = 0;
	// popupwindow的高度
	public static int popupHeight = 0;


	/************************
	 * 启动和刷新连接标志
	 * 
	 * @param mContext
	 * @param img
	 * @param msg
	 * @param contentTitle
	 * @param contentText
	 ***********************/
	public static void updateNotification(Context mContext, int img,
                                          String msg, String contentTitle, String contentText) {
		builder.setSmallIcon(img);
		builder.setTicker(msg);
		builder.setContentTitle(contentTitle);
		builder.setContentText(contentText);
		builder.setContentIntent(contentItent);
		notificationManager.notify(NOTIFICATION_FLAG,builder.build());

		//notification.icon = img;
	//	notification.tickerText = msg;
	//	notification.setLatestEventInfo(mContext, contentTitle, contentText,
	//			contentItent);
		//notificationManager.notify(NOTIFICATION_FLAG, notification);
	}

	/************************
	 * 启动和刷新连接标志图片
	 * 
	 * @param mContext
	 * @param img
	 ***********************/
	public static void updateNotificationPic(Context mContext, int img) {

		if (builder != null) {
			builder.setSmallIcon(img);
			builder.setTicker(TextString.info);
			builder.setContentTitle("");
			builder.setContentText("");
			builder.setContentIntent(contentItent);
			notificationManager.notify(NOTIFICATION_FLAG,builder.build());
		}


//		if (notification != null) {
//			notification.icon = img;
//			notification.tickerText = TextString.info;
//			if (DebugInfo.isDebug()) {
//				System.out
//						.println("OBDUtil updateNotificationPic TextString.info = "
//								+ TextString.info);
//			}
//			notification.setLatestEventInfo(mContext, "", "", contentItent);
//			notificationManager.notify(NOTIFICATION_FLAG, notification);


	//	}

	}

	/************************
	 * 启动和刷新连接标志文字
	 * 
	 * @param mContext
	 * @param msg
	 ***********************/
	public static void updateNotificationText(Context mContext, String msg) {

		if (builder != null) {
			builder.setTicker(msg);
			builder.setContentTitle("");
			builder.setContentText("");
			builder.setContentIntent(contentItent);
			notificationManager.notify(NOTIFICATION_FLAG,builder.build());
		}

//		if (notification != null) {
//			notification.tickerText = msg;
//			notification.setLatestEventInfo(mContext, "", "", contentItent);
//			notificationManager.notify(NOTIFICATION_FLAG, notification);
//		}
	}

	/*************
	 * 删除通知
	 * 
	 */
	public static void clearNotification() {
		// 启动后删除之前我们定义的通知
		notificationManager.cancel(NOTIFICATION_FLAG);
	}

	/**
	 * 对百度地图结果解析
	 * 
	 * @param mJsons
	 * @return
	 */
	private static String parseJsonBaidu(String mJsons) {
		// renderReverse&&renderReverse({"status":0,"result":{"location":{"lng":114.06575700326,"lat":22.579379960605},"formatted_address":"广东省深圳市福田区梅坳八路26","business":"上梅林,莲花北村,银湖","addressComponent":{"city":"深圳市","direction":"附近",
		// "distance":"27","district":"福田区","province":"广东省","street":"梅坳八路","street_number":"26"},"poiRegions":[],"cityCode":340}})
		String city = null;
		int start = mJsons.indexOf("(");
		int end = mJsons.lastIndexOf(")");
		String jsons = mJsons.substring(start + 1, end);
		try {
			JSONObject jsonObject = new JSONObject(jsons);
			if (jsonObject != null) {
				JSONObject resultObject = jsonObject.getJSONObject("result");
				if (resultObject != null) {
					JSONObject addressJsonObject = resultObject
							.getJSONObject("addressComponent");
					if (addressJsonObject != null) {
						city = addressJsonObject.get("city").toString();
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return city;
	}



	/*********************
	 * 第一次安装，启动软件，读取bin文件,获取数据
	 ******************/
	public static boolean readDataToMemory(Context mContext) {
		boolean isReadSuccess = true;
		InputStream is = Language.getCountryLanguage(mContext);
		int flag = Language.languageType;
		if (is != null) {
			try {
				DataBaseBin.initBin(is.available());
				is.read(DataBaseBin.getdataBin());
			} catch (IOException e1) {
				isReadSuccess = false;
			}
			if (isReadSuccess) {
				InputStream fisA = mContext.getResources().openRawResource(
						R.raw.lower_a);
				InputStream fisB = mContext.getResources().openRawResource(
						R.raw.lower_b);
				InputStream fisC = mContext.getResources().openRawResource(
						R.raw.iobd4);
					isReadSuccess = false;
				}
			TextString.initTextString();
			OBDReadAllData.getCommandSets();

		} else {
			isReadSuccess = false;
		}
		return isReadSuccess;
	}

	/************************
	 * @param body
	 *            json数据
	 * @return
	 **********************/
	public static String parseJson(String body) {
		String cityName = null;
		JSONArray array;
		try {
			array = new JSONObject(body).getJSONArray("results");
			JSONObject jsonObject = array.getJSONObject(0);
			JSONArray mJsonArray = (JSONArray) jsonObject
					.get("address_components");
			for (int i = 0; i < mJsonArray.length(); i++) {
				JSONObject mObject = (JSONObject) mJsonArray.get(i);
				JSONArray mArray = (JSONArray) mObject.get("types");
				if (((String) mArray.get(0)).equals("locality")) {
					cityName = (String) mObject.get("short_name");
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return cityName;
	}

	// //
	// ʹ�ÿ�ݷ�����ɷ��?�������ϸ�Ķ�λ��SDK��ѹĿ¼��Docs�ļ�����OnekeyShare���JavaDoc��
	// public static void showShare(boolean silent, String platform,
	// Context mContext, String imgpath) {
	// OnekeyShare oks = new OnekeyShare();
	// oks.setNotification(R.drawable.icon,
	// mContext.getString(R.string.app_name));
	// // oks.setAddress("12345678901");
	// oks.setTitle("");// 乱码
	// // oks.setTitleUrl("http://sharesdk.cn");
	// oks.setText("");// ����
	// oks.setImagePath(imgpath);// ͼƬ
	// //
	// oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
	// oks.setUrl("http://www.xtooltech.com");// ΢����ʹ��
	// // oks.setAppPath(MainActivity.TEST_IMAGE);
	// // oks.setComment(mContext.getString(R.string.share));
	// // oks.setSite(mContext.getString(R.string.app_name));
	// // oks.setSiteUrl("http://sharesdk.cn");
	// // oks.setVenueName("Southeast in China");
	// // oks.setVenueDescription("This is a beautiful place!");
	// // oks.setLatitude(23.122619f);
	// // oks.setLongitude(113.372338f);
	// oks.setSilent(silent);
	// if (platform != null) {
	// oks.setPlatform(platform);
	// }
	//
	// // ȥ��ע�ͣ����ݷ���ķ���ӹ����OneKeyShareCallback�ص�
	// // oks.setCallback(new OneKeyShareCallback());
	// // oks.setShareContentCustomizeCallback(new
	// // ShareContentCustomizeDemo());
	//
	// oks.show(mContext);
	// }

	public static void getNumber(String args) {
		if (args.matches("-?\\d+\\.?\\d*")) {
			if (args.contains(".")) {
				double num1 = Double.parseDouble(args);
			} else {
				int num2 = Integer.parseInt(args);
			}
		} else {
		}
	}


	public static String[] getUnits(String[] units) {
		String[] data = null;
		boolean isFindSuccess = false;
		for (int i = 0; i < units.length; i++) {// ��ȡ��λ
			isFindSuccess = false;
			if (units[i] != null) {
				try {
					units[i] = CurrentData.unitChoose(units[i]);
					isFindSuccess = true;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} finally {
					if (!isFindSuccess) {
						break;
					}
				}
			} else {
				isFindSuccess = false;
				break;
			}
		}
		if (isFindSuccess) {
			data = units;
		}
		return data;
	}

	public static String getString() {
		String db = "/data/data/com.xtooltech.ui/databases/obdcar.db";
		String db2 = "/data/data/com.xtooltech.ui/files/obdcar.txt";
		try {

			File fileIn = new File(db);
			File fileOut = new File(db2);
			java.io.FileInputStream fis = new java.io.FileInputStream(fileIn);
			FileOutputStream fos = new FileOutputStream(fileOut);
			java.io.BufferedInputStream bis = new java.io.BufferedInputStream(
					fis);
			java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(
					fos);
			byte[] b = new byte[(int) fileIn.length()];
			int len = 0;
			if ((len = bis.read(b)) != -1) {
				bos.write(b, 0, len);
			}
			fis.close();
			fos.close();
		} catch (Exception e) {
		}
		return db2;
	}

	//








	public static boolean isSettingDash = false;



	/******************
	 * ���ý�����Ի���
	 *
	 * @param mContext
	 * @param msg
	 * @return
	 ****************/
	public static ProgressDialog setProgressDialog(Context mContext, String msg) {
		ProgressDialog mDialog = new ProgressDialog(mContext);
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage(msg);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// system.out.println("setProgressDialog onDismiss");
				CurrentData.isStopSendReceive = true;
			}
		});
		mDialog.show();
		return mDialog;
	}

	public static ProgressDialog setProgressDialog2(Context mContext, String msg) {
		ProgressDialog mDialog = new ProgressDialog(mContext);
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage(msg);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		mDialog.show();
		return mDialog;
	}



	public static EditText getAlarmEditText(Context context, int alarmValue) {
		EditText mEditText = new EditText(context);
		mEditText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		mEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
		mEditText.setText(String.valueOf(alarmValue));
		return mEditText;
	}

	/************************
	 * ������������ʾ�Ի���
	 *
	 * @param mContext
	 * @param title
	 * @param msg
	 * @param btnTitle
	 * @param mOnClickListener
	 *************************/
	public static void toastBackMainUiHint(Context mContext, String title,
                                           String msg, String btnTitle,
                                           DialogInterface.OnClickListener mOnClickListener) {
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
		mBuilder.setTitle(title);
		mBuilder.setMessage(msg);
		mBuilder.setPositiveButton(btnTitle, mOnClickListener);
		mBuilder.show();
	}

	/***************
	 * ��ת����ҳ��
	 *
	 * @param mContext
	 ***************/

	/******************
	 * ɾ�����������
	 *****************/

	/************************
	 * ɾ��������ʷ���
	 *
	 * @param mContext
	 ***********************/

	/****************
	 * �ϲ�����bitmap
	 ***************/
	public static Bitmap combineBitmap(Bitmap background, Bitmap foreground,
                                       float top) {
		if (background == null) {
			return null;
		}
		int bgWidth = background.getWidth();
		int bgHeight = background.getHeight();
		// int fgWidth =
		foreground.getWidth();
		// int fgHeight =
		foreground.getHeight();
		Bitmap newmap = Bitmap
				.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(newmap);
		canvas.drawBitmap(background, 0, 0, null);
		canvas.drawBitmap(foreground, 10, top, null);
		if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.P) {
			canvas.save();
		}else {

			try {
				Class<?> clazz= Class.forName("android.graphics.Canvas");
				Method methSave = clazz.getDeclaredMethod("save", Integer.class);
				if (methSave != null) {
					Object invoke = methSave.invoke(canvas, Canvas.ALL_SAVE_FLAG);
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		//canvas.save(Canvas.ALL_SAVE_FLAG);
		}
		canvas.restore();
		return newmap;
	}

	// /*
	// * �õ������������
	// */
	// public static float getVolumnRatio(Context mContext){
	// AudioManager am = (AudioManager)
	// mContext.getSystemService(Context.AUDIO_SERVICE);
	// float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	// float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);
	// float volumnRatio = audioCurrentVolumn / audioMaxVolumn;
	// return volumnRatio;
	// }
	/*****************************
	 * ��ȡfiles�ļ�������ļ�����
	 ****************************/
	public static ArrayList<String> getAttachementFileName(String directoryPath) {
		ArrayList<String> attachementNames = new ArrayList<String>();
		File mFile = new File(directoryPath);
		if (mFile.exists()) {
			if (mFile.isDirectory()) {
				String[] fileNames = mFile.list();
				for (int i = 0; i < fileNames.length; i++) {
					if (fileNames[i].length() >= 4) {
						if (fileNames[i].substring(fileNames[i].length() - 4,
								fileNames[i].length()).equals(".txt")
								&& !fileNames[i].equals("feedback.txt")) {
							attachementNames.add(fileNames[i]);
						}
					}
				}
			}
		}
		return attachementNames;
	}

	/**********************
	 * ����������ݵ��ļ���
	 *********************/

	/***********************
	 * ƥ���Ƿ�Ϊ �����������
	 *
	 * @param a
	 * @return
	 **********************/
	public static boolean isNumber(String a) {
		boolean flag;
		if (a.matches("^\\+?[1-9][0-9]*$")) {// ������ʽ ��
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	/***************
	 * ��ȡ����ѡ��
	 *
	 * @param carInfoFlag
	 * @return
	 **************/

	/***********************
	 * �����Ѿ�ѡ���˵�����ѡ��
	 *
	 * @param mIDSets
	 * @param mCommandSets
	 * @return
	 ***********************/
	public static ArrayList<HashMap<String, String>> getSingleChoiseCommandItmes(
			ArrayList<String> mIDSets,
			ArrayList<HashMap<String, String>> mCommandSets) {
		for (String id : mIDSets) {
			for (int i = 0; i < mCommandSets.size(); i++) {
				if (id.equals(mCommandSets.get(i).get("id"))) {
					mCommandSets.remove(i);
				}
			}
		}
		return mCommandSets;
	}





	// 遍历map
	private static void Output(HashMap<String, String> hashMap) {
		Set<Map.Entry<String, String>> entrySet = hashMap.entrySet();
		for (java.util.Map.Entry<String, String> e : entrySet) {
			System.out.print(e.getKey() + "=" + e.getValue() + "\t");
		}
		System.out.println();

	}


	/***************
	 * ���õ���Ի���
	 *
	 * @param mContext
	 * @param title
	 * @param items
	 * @param btnName
	 * @param mDialogClickListener
	 * @param resid
	 ******************/
	public static void setItemDialog(Context mContext, String title,
                                     String[] items, String btnName,
                                     DialogInterface.OnClickListener mDialogClickListener, int resid) {
		Builder mBuilder = new Builder(mContext);
		mBuilder.setTitle(title);
		mBuilder.setItems(items, mDialogClickListener);
		Button mButton = new Button(mContext);
		mButton.setBackgroundResource(resid);
		mButton.setText(btnName);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mBuilder.setView(mButton);
		mDialog = mBuilder.create();
		mDialog.show();
	}

	//
	// //���õ���Ի���
	// public static void setRpmDialog(Context mContext,final String
	// rpmName,final String rpmID,final TextView mName,final TextView mUnit){
	//
	// Builder mBuilder = new Builder(mContext);
	// mBuilder.setTitle(OBDReadAllData.prompt);
	// mBuilder.setItems(new String[]{rpmName},new
	// DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog, int which) {
	// try {
	// Frame f = OBDUiActivity.mDiagnosis.searchIdUsingString(rpmID, "DS");
	// String rpmUnit = f.get(2).toString();
	// rpmUnit = Diagnosis.unitChoose(rpmUnit);
	// mUnit.setText(rpmUnit);
	// mName.setText(rpmName);
	// } catch (Exception e) {
	// }
	// }
	// });
	// Button mButton = new Button(mContext);
	// mButton.setBackgroundResource(R.drawable.button_yellow_press);
	// mButton.setText(OBDReadAllData.cancle);
	// mBuilder.setView(mButton);
	// mButton.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// mDialog.dismiss();
	// }
	// });
	// mDialog = mBuilder.create();
	// mDialog.show();
	// }
	/************************************
	 * * ������Ļ����״̬���������Ʋ�Ϩ��
	 *
	 * @param on
	 *            �Ƿ���
	 ***********************************/

	/*****************************************
	 * ��ȡ��Ļ�ķֱ��ʺ���Ļ���ܶ�,�Լ���Ļ�ߴ�
	 *
	 * @param mContext
	 * @return
	 ****************************************/
	public static ArrayList<Object> getmScreenParameters(Context mContext) {
		ArrayList<Object> mArrayList = new ArrayList<Object>();
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);// ��Ļ�Ŀ�Ⱥ͸߶� �ܶȺ��ܶ�DPI
		int screenWidth = metrics.widthPixels;// ��Ļ��ȣ����أ�
		int screenHeight = metrics.heightPixels;// ��Ļ�߶ȣ����أ�
		float screenDensity = metrics.density;// ��Ļ�ܶȣ�0.75 / 1.0 / 1.5��
		int screenDensityDpi = metrics.densityDpi;// ��Ļ�ܶ�DPI��120 / 160 /
													// 240��
		double diagonalPixels = Math.sqrt(Math.pow(screenWidth, 2)
				+ Math.pow(screenHeight, 2));// ��Ļ�ĶԽ��ߣ����أ�
		double screenSize = diagonalPixels / (160 * screenDensity);// ��Ļ�ĳߴ��С������ֵ
		mArrayList.add(screenWidth);
		mArrayList.add(screenHeight);
		mArrayList.add(screenDensity);
		mArrayList.add(screenDensityDpi);
		mArrayList.add(screenSize);
		return mArrayList;
	}


	/*******************************
	 * �����Ļ���������С,��ʽ ����ɫ
	 *
	 * @param mView
	 * @param screenSize
	 * @param type
	 * @param color
	 ******************************/
	public static void setTextAttr(TextView mView, double screenSize, int type,
                                   int color) {
		if (screenSize < 2.5f) {
			mView.setTextSize(14);
		} else if (screenSize < 4.0f) {
			mView.setTextSize(18);
		} else if (screenSize < 5.0f) {
			mView.setTextSize(22);
		} else if (screenSize < 6.0f) {
			mView.setTextSize(24);
		} else if (screenSize < 7.0f) {
			mView.setTextSize(26);
		} else if (screenSize < 8.0f) {
			mView.setTextSize(32);
		} else if (screenSize < 9.0f) {
			mView.setTextSize(36);
		} else {
			mView.setTextSize(45);
		}
		// mView.setTextSize(OBDUiActivity.fonsize);
		if (type == 1) {
			mView.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);// ����
		} else if (type == 2) {
			mView.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);// ��б
		}
		if (color == 0) {
			return;
		} else if (color == 1) {
			mView.setTextColor(Color.WHITE);
		} else if (color == 2) {
			mView.setTextColor(Color.GRAY);
		} else if (color == 3) {
			mView.setTextColor(Color.YELLOW);
		} else if (color == 4) {
			mView.setTextColor(Color.RED);
		} else if (color == 5) {
			mView.setTextColor(Color.GREEN);
		} else if (color == 6) {
			mView.setTextColor(Color.rgb(130, 130, 130));
		} else if (color == 7) {
			mView.setTextColor(Color.rgb(174, 174, 174));
		} else if (color == 8) {
			mView.setTextColor(Color.BLACK);
		} else if (color == 9) {
			mView.setTextColor(Color.BLUE);
		}
	}

	/**
	 *
	 * @param mView
	 * @param screenSize
	 * @param type
	 * @param color
	 */
	public static void setTextAttrOfHud(TextView mView, double screenSize,
                                        int type, int color) {
		if (screenSize < 2.5f) {
			mView.setTextSize(14);
		} else if (screenSize < 4.0f) {
			mView.setTextSize(18);
		} else if (screenSize < 5.0f) {
			mView.setTextSize(22);
		} else {
			mView.setTextSize(24);
		}
		// mView.setTextSize(OBDUiActivity.fonsize);
		if (type == 1) {
			mView.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);// ����
		} else if (type == 2) {
			mView.setTypeface(Typeface.MONOSPACE, Typeface.ITALIC);// ��б
		}
		if (color == 1) {
			mView.setTextColor(Color.WHITE);
		} else if (color == 2) {
			// ����ɫ
			// mView.setTextColor(Color.rgb(220, 220, 220));
			mView.setTextColor(Color.GRAY);
		} else if (color == 3) {
			mView.setTextColor(Color.YELLOW);
		} else if (color == 4) {
			mView.setTextColor(Color.RED);
		} else if (color == 5) {
			mView.setTextColor(Color.GREEN);
		} else if (color == 6) {
			mView.setTextColor(Color.rgb(130, 130, 130));
		} else if (color == 7) {
			mView.setTextColor(Color.rgb(174, 174, 174));
		} else if (color == 8) {
			mView.setTextColor(Color.BLACK);
		} else if (color == 9) {
			mView.setTextColor(Color.BLUE);
		}
	}

	/********************
	 * ��ȡϵͳ�����С
	 *
	 * @return
	 *******************/
	public static float getFontSize() {
		Configuration mCurConfig = new Configuration();
		try {
			Class<?> activityManagerNative = Class
					.forName("android.app.ActivityManagerNative");
			Object am = activityManagerNative.getMethod("getDefault").invoke(
					activityManagerNative);
			Object config = am.getClass().getMethod("getConfiguration")
					.invoke(am);
			Configuration configs = (Configuration) config;
			mCurConfig.updateFrom(configs);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		// Log.w("fontsize", "getFontSize(), Font size is " +
		// mCurConfig.fontScale);
		return mCurConfig.fontScale;
	}

	/**
	 * 读ECU工作状态，返回true,通讯中断，false:通讯正常
	 *
	 * @return
	 * @throws InterruptedException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("finally")

	/*******************
	 * ��֤�ַ����Ч��
	 *
	 * @param str
	 * @param numType
	 * @return
	 ******************/
	public static boolean isNumber(String str, String numType) {// �ж�һ���ַ��ܷ�תΪ����
		try {
			if (numType.equalsIgnoreCase("Int")) {
				Integer.valueOf(str);
			} else if (numType.equalsIgnoreCase("Float")) {
				Float.valueOf(str);
			} else {
				Float.valueOf(str);
			}
			return true;// ��������֣�����True
		} catch (Exception e) {
			return false;// ����׳��쳣������False
		}
	}

	/**********************
	 * ����listview���ܸ߶�
	 *
	 * @param listView
	 *********************/
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter mListAdapter = listView.getAdapter();// ��ȡListView��Ӧ��Adapter
		if (mListAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = mListAdapter.getCount(); i < len; i++) { // listAdapter.getCount()������������Ŀ
			View listItem = mListAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // ��������View �Ŀ��
			totalHeight += listItem.getMeasuredHeight(); // ͳ������������ܸ߶�
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (mListAdapter.getCount() - 1));
		// listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�
		// params.height���õ����ListView������ʾ��Ҫ�ĸ߶�
		listView.setLayoutParams(params);
	}


	/*****************
	 * ��ͼ
	 *
	 * @param mActivity
	 * @return
	 ****************/
	public static Bitmap getBitmap(Activity mActivity) {
		View views = mActivity.getWindow().getDecorView();
		views.buildDrawingCache();
		Rect frames = new Rect();
		views.getWindowVisibleDisplayFrame(frames);
		int statusBarHeights = frames.top;// 获取状态栏高度
		Display display = mActivity.getWindowManager().getDefaultDisplay();
		int widths = display.getWidth();
		int heights = display.getHeight();// 测试
		views.setDrawingCacheEnabled(true);//
		Bitmap bmp = Bitmap.createBitmap(views.getDrawingCache(), 0,
				statusBarHeights, widths, heights - statusBarHeights);
		return bmp;
	}


	/**
	 * 获取和保存当前屏幕的截图
	 */
	public static void GetandSaveCurrentImage(Context mContext) {
		// 1.构建Bitmap
		WindowManager windowManager = ((Activity) mContext).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int w = display.getWidth();
		int h = display.getHeight();

		Bitmap Bmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);

		// 2.获取屏幕
		View decorview = ((Activity) mContext).getWindow().getDecorView();
		decorview.setDrawingCacheEnabled(true);
		Bmp = decorview.getDrawingCache();

		String SavePath = "/data/data/com.xtooltech.ui/OBD";

		// 3.保存Bitmap
		try {
			File path = new File(SavePath);
			// 文件
			String filepath = SavePath + "/Screen_1.png";
			File file = new File(filepath);
			if (!path.exists()) {
				path.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos) {
				Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();

				Toast.makeText(mContext,
						"截屏文件已保存至SDCard/AndyDemo/ScreenImage/下",
						Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取SDCard的目录路径功能
	 *
	 * @return
	 */
	private String getSDCardPath() {
		File sdcardDir = null;
		// 判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist) {
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}



	// // PopupWindow
	// public static PopupWindow initPopupwindwow(final Context mContext,
	// final String path) {
	// final PopupWindow popupWindow;
	// LayoutInflater mInflater = LayoutInflater.from(mContext);
	// View popupWindow_view = mInflater.inflate(R.layout.weibo_main, null,
	// false);
	// popupWindow_view.setFocusable(true);
	// popupHeight = initPopupHeight();
	// popupWindow = new PopupWindow(popupWindow_view, 150, popupHeight, true);
	// popupWindow.setAnimationStyle(R.style.PopupAnimation);
	// LinearLayout v = (LinearLayout) popupWindow_view
	// .findViewById(R.id.ll_weibo);
	// v.setFocusable(true);// ��ȡ�����¼���������
	// v.setFocusableInTouchMode(true);// ��ȡ�����¼���������
	// v.setOnKeyListener(new OnKeyListener() {
	// @Override
	// public boolean onKey(View v, int keyCode, KeyEvent event) {
	// // TODO Auto-generated method stub
	// if (event.getAction() == KeyEvent.ACTION_DOWN
	// && keyCode == KeyEvent.KEYCODE_BACK) {
	// dismissPopupwindow(popupWindow);
	// }
	// if (event.getAction() == KeyEvent.ACTION_DOWN
	// && keyCode == KeyEvent.KEYCODE_MENU) {
	// dismissPopupwindow(popupWindow);
	// }
	// return false;
	// }
	// });
	// //
	// //-------------------------------------------------------------------------------
	//
	// // //�ڶ��ַ�������ȡ�����¼�----------------------------------------------
	// // popupWindow.setBackgroundDrawable(new BitmapDrawable()); //
	// // ��Ӧ���ؼ��������
	//
	// //
	// //------------------------------------------------------------------------
	// // �������ط���ʧ
	// popupWindow_view.setOnTouchListener(new OnTouchListener() {
	// @Override
	// public boolean onTouch(View arg0, MotionEvent arg1) {
	// // TODO Auto-generated method stub
	// dismissPopupwindow(popupWindow);
	// return false;
	// }
	// });
	//
	// Button btn1 = (Button) popupWindow_view.findViewById(R.id.button1);
	// btn1.setBackgroundResource(R.drawable.button_sina);
	// Button btn2 = (Button) popupWindow_view.findViewById(R.id.button2);
	// btn2.setBackgroundResource(R.drawable.button_tencent);
	// Button btn3 = (Button) popupWindow_view.findViewById(R.id.button3);
	// btn3.setBackgroundResource(R.drawable.button_facebook);
	// Button btn4 = (Button) popupWindow_view.findViewById(R.id.button4);
	// btn4.setBackgroundResource(R.drawable.button_twitter);
	// Button btn5 = (Button) popupWindow_view.findViewById(R.id.button5);
	// btn5.setBackgroundResource(R.drawable.button_wx);
	// Button btn6 = (Button) popupWindow_view
	// .findViewById(R.id.btn_share_more_green);
	// btn6.setBackgroundResource(R.drawable.share_more_green);
	// btn1.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// OBDUtil.showShare(true, SinaWeibo.NAME, mContext, path);
	// }
	// });
	// btn2.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// OBDUtil.showShare(true, TencentWeibo.NAME, mContext, path);
	// }
	// });
	// btn3.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// OBDUtil.showShare(true, Facebook.NAME, mContext, path);
	// }
	// });
	// btn4.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// OBDUtil.showShare(true, Twitter.NAME, mContext, path);
	// }
	// });
	// btn5.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// if (isMM(mContext)) {
	// Intent mIntent = new Intent(mContext,
	// WeiXinShareActivity.class);
	// mIntent.putExtra("path", path);
	// mContext.startActivity(mIntent);
	// } else {
	// // ��������΢�ſͻ���
	// Toast.makeText(mContext, TextString.downloadWechat,
	// Toast.LENGTH_LONG).show();
	// }
	// }
	// });
	// // 强转context为activity
	// final Activity activity2 = (Activity) mContext;
	// btn6.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// showShareSystem(mContext, path);
	// }
	//
	// });
	// return popupWindow;
	// }

	/**
	 * 显示系统分享界面
	 * 
	 * @param mContext
	 * @param path
	 */
	public static void showShareSystem(final Context mContext, final String path) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		File file = new File(path);
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		shareIntent.setType("image/jpeg");
		mContext.startActivity(Intent.createChooser(shareIntent,
				((Activity) mContext).getTitle()));
	}

	public static void dismissPopupwindow(PopupWindow popupWindow) {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	// /***
	// * ��ȡPopupWindowʵ��
	// */
	// public static void getPopupWindow(Context mContext,
	// PopupWindow popupWindow, String path) {
	// if (null != popupWindow) {
	// popupWindow.dismiss();
	// return;
	// } else {
	// initPopupwindwow(mContext, path);
	// }
	// }

	/**
	 * 改变textView字体颜色
	 * 
	 * @param views
	 *            ：textView
	 * @param color
	 *            ：颜色
	 */
	public static void changeTextViewColor(TextView[] views, int color) {
		for (int i = 0; i < views.length; i++) {
			views[i].setTextColor(color);
		}
	}

	/**
	 * // 改变单个textView字体颜色
	 * 
	 * @param view
	 * @param color
	 */
	public static void changeSingleTextViewColor(TextView view, int color) {
		view.setTextColor(color);
	}

	public static boolean isMM(Context mContext) {// ΢�ſͻ����Ƿ����
		boolean isMM = false;
		try {
			PackageInfo mInfo = mContext.getPackageManager().getPackageInfo(
					"com.tencent.mm", 0);
			if (mInfo == null) {
				isMM = false;
			} else {
				isMM = true;
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e("PackageInfo", "PackageInfo error");
			isMM = false;
			e.printStackTrace();
		}
		return isMM;
	}

	/**
	 * 截图，存放目录：/mnt/sdcard/screenShot...png
	 * 
	 * @param context
	 * @param bm
	 */
}
