package com.xtooltech.adten.common.obd;

import android.content.Context;

import com.xtooltech.adten.R;

import java.io.InputStream;
import java.util.Locale;

public class Language {

	public static int language = 0;
	public static int languageType = -1;

	public static String strCodedFormat = null;
	    
	private static final String CN_FORMAT = "GB2312";//cp936
	private static final String TW_FORMAT = "big5";
	private static final String EN_FORMAT = "cp1252";
	private static final String JA_FORMAT = "Shift_JIS";//sjis
	private static final String KO_FORMAT = "EUC-KR";//GB18030
	private static final String PT_FORMAT = "cp1252";
	private static final String PL_FORMAT = "cp1250";
	private static final String ES_FORMAT = "cp1252";
	private static final String FR_FORMAT = "cp1252";
	private static final String DE_FORMAT = "cp1252";
	private static final String IT_FORMAT = "cp1252";
	private static final String RU_FORMAT = "cp1251";
	private static final String HU_FORMAT = "cp1250";
	private static final String TR_FORMAT = "cp1254";
	
	
	private static void setCodeFormat(String cf){
		strCodedFormat = cf;
	}
	
	public static String getCodeFormat(){
		if(strCodedFormat != null){
			return strCodedFormat;
		}else {
			return EN_FORMAT;
		}
		
	}
	/**
	 * 
	 */
	public static InputStream getCountryLanguage(Context mContext){
		String countryName = Locale.getDefault().getCountry();
		String countryLanguage = Locale.getDefault().getLanguage();
		InputStream is = null;
		if (countryName.equalsIgnoreCase("CN")&& countryLanguage.equalsIgnoreCase("zh")) {
			is = mContext.getResources().openRawResource(R.raw.cnobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForCNtext();
			setCodeFormat(CN_FORMAT);
			language = 1;
			languageType = 0;
			TextString.languageSelect = "语言";
		}
		else if ((countryName.equalsIgnoreCase("TW"))&& countryLanguage.equalsIgnoreCase("zh")) {
			is = mContext.getResources().openRawResource(R.raw.twobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForTWtext();
			setCodeFormat(TW_FORMAT);
			language = 2;
			languageType = 1;
			TextString.languageSelect = "語言";
		}
		else if (countryLanguage.equalsIgnoreCase("JA") ) {
			is = mContext.getResources().openRawResource(R.raw.jaobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForJAtext();
			setCodeFormat(JA_FORMAT);
			language = 3;
			languageType = 2;
			TextString.languageSelect = "言語";
		} else if (countryLanguage.equalsIgnoreCase("KO")) {
			is = mContext.getResources().openRawResource(R.raw.koobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForKOtext();
			setCodeFormat(KO_FORMAT);
			language = 3;
			languageType = 3;
			TextString.languageSelect = "언어";
		}  else if (countryLanguage.equalsIgnoreCase("ES")) {
			is = mContext.getResources().openRawResource(R.raw.esobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForEStext();
			setCodeFormat(ES_FORMAT);
			language = 3;
			languageType = 6;
			TextString.languageSelect = "idioma";
		} else if (countryLanguage.equalsIgnoreCase("FR")) {
			is = mContext.getResources().openRawResource(R.raw.frobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForFRtext();
			setCodeFormat(FR_FORMAT);
			language = 3;
			languageType = 7;
			TextString.languageSelect = "Langue";
		} else if (countryLanguage.equalsIgnoreCase("DE")) {
			is = mContext.getResources().openRawResource(R.raw.deobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForDEtext();
			setCodeFormat(DE_FORMAT);
			language = 3;
			languageType = 8;
			TextString.languageSelect = "Sprache";
		}  else if (countryLanguage.equalsIgnoreCase("IT")) {
			is = mContext.getResources().openRawResource(R.raw.itobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForITtext();
			setCodeFormat(IT_FORMAT);
			language = 3;
			languageType = 9;
			TextString.languageSelect = "lingua";
		}  else if (countryLanguage.equalsIgnoreCase("HU")) {
			is = mContext.getResources().openRawResource(R.raw.huobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForHUtext();
			setCodeFormat(HU_FORMAT);
			language = 3;
			languageType = 12;
			TextString.languageSelect = "nyelv";
			
		}  else if (countryLanguage.equalsIgnoreCase("TR")) {
			is = mContext.getResources().openRawResource(R.raw.trobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForTRtext();
			setCodeFormat(TR_FORMAT);
			language = 3;
			languageType = 13;
			TextString.languageSelect = "dil";
			
		}		
		else {
			is = mContext.getResources().openRawResource(R.raw.enobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForENtext();
			strCodedFormat = EN_FORMAT;
			language = 3;
			languageType = 11;
			TextString.languageSelect = "Language";
		}
		return is;
	}

	public static InputStream getCountryLanguage(Context mContext, int flag) {
		initLanguageSelect(mContext);
		InputStream is = null;
		if (flag == 0) {
			is = mContext.getResources().openRawResource(R.raw.cnobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForCNtext();
			strCodedFormat = CN_FORMAT;
			language = 1;
			TextString.languageSelect = "语言";
		} else if (flag == 1) {
			is = mContext.getResources().openRawResource(R.raw.twobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForTWtext();
			strCodedFormat = TW_FORMAT;
			language = 2;
			TextString.languageSelect = "語言";
		} else if (flag == 2) {
			is = mContext.getResources().openRawResource(R.raw.jaobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForJAtext();
			strCodedFormat = JA_FORMAT;
			language = 3;
			TextString.languageSelect = "言語";
		} else if (flag == 3) {
			is = mContext.getResources().openRawResource(R.raw.koobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForKOtext();
			strCodedFormat = KO_FORMAT;
			language = 3;
			TextString.languageSelect = "언어";
		} else if (flag == 6) {
			is = mContext.getResources().openRawResource(R.raw.esobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForEStext();
			strCodedFormat = ES_FORMAT;
			language = 3;
			TextString.languageSelect = "idioma";
		} else if (flag == 7) {
			is = mContext.getResources().openRawResource(R.raw.frobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForFRtext();
			strCodedFormat = FR_FORMAT;
			language = 3;
			TextString.languageSelect = "langue";
		} else if (flag == 8) {
			is = mContext.getResources().openRawResource(R.raw.deobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForDEtext();
			strCodedFormat = DE_FORMAT;
			language = 3;
			TextString.languageSelect = "Sprache";
		} else if (flag == 9) {
			is = mContext.getResources().openRawResource(R.raw.itobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForITtext();
			strCodedFormat = IT_FORMAT;
			language = 3;
			TextString.languageSelect = "lingua";
		}  else if (flag == 11) {
			is = mContext.getResources().openRawResource(R.raw.enobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForENtext();
			strCodedFormat = EN_FORMAT;
			language = 3;
			TextString.languageSelect = "Language";
		} else if (flag == 12) {
			is = mContext.getResources().openRawResource(R.raw.huobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForHUtext();
			strCodedFormat = HU_FORMAT;
			language = 3;
			TextString.languageSelect = "nyelv";
		} else if (flag == 13) {
			is = mContext.getResources().openRawResource(R.raw.trobd);
			OBDReadAllData.dpDataBaseText = OBDReadAllData.dpDataBaseObj.databaseForTRtext();
			strCodedFormat = TR_FORMAT;
			language = 3;
			TextString.languageSelect = "dil";
		}
		return is;
	}
	
	public static void initLanguageSelect(Context mContext){
		String countryName = Locale.getDefault().getCountry();
		String countryLanguage = Locale.getDefault().getLanguage();
		InputStream is = null;
		if (countryName.equalsIgnoreCase("CN")&& countryLanguage.equalsIgnoreCase("zh")) {
			TextString.languageSelect = "语言";
		} else if ((countryName.equalsIgnoreCase("TW"))
				&& countryLanguage.equalsIgnoreCase("zh")) {
			TextString.languageSelect = "語言";
		} else if (countryLanguage.equalsIgnoreCase("JA")) {
			TextString.languageSelect = "言語";
		} else if (countryLanguage.equalsIgnoreCase("KO")) {
			TextString.languageSelect = "언어";
		} else if (countryLanguage.equalsIgnoreCase("PT")) {
			TextString.languageSelect = "língua";
		} else if (countryLanguage.equalsIgnoreCase("PL")) {
			TextString.languageSelect = "język";
		} else if (countryLanguage.equalsIgnoreCase("ES")) {
			TextString.languageSelect = "idioma";
		} else if (countryLanguage.equalsIgnoreCase("FR")) {
			TextString.languageSelect = "Langue";
		} else if (countryLanguage.equalsIgnoreCase("DE")) {
			TextString.languageSelect = "Sprache";
		} else if (countryLanguage.equalsIgnoreCase("IT")) {
			TextString.languageSelect = "lingua";
		} else if (countryLanguage.equalsIgnoreCase("RU")) {
			TextString.languageSelect = "язык";
		} else if (countryLanguage.equalsIgnoreCase("HU")) {
			TextString.languageSelect = "nyelv";
		} else if (countryLanguage.equalsIgnoreCase("TR")) {
			TextString.languageSelect = "dil";
		}
		else {
			TextString.languageSelect = "Language";
		}
		return ;
	}
}
