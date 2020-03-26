package com.xtooltech.adtenx.common.obd;

import com.xtooltech.adtenx.common.db.Language;

import java.io.UnsupportedEncodingException;

public class DataBaseBin {
	
    private static byte dataBin[] = null;
	
	private static int shortToInt(short[] btemp){
		int value=0;
		value += (btemp[0]&0xff) * 0x1000000;
		value += (btemp[1]&0xff) * 0x10000;
		value += (btemp[2]&0xff) * 0x100;
		value += btemp[3]&0xff;
		
		return value;
	}
	
	public static TextAndHelp_File searchText(String binID) throws UnsupportedEncodingException {
		if(binID == null){
			return new TextAndHelp_File();
		}
		return searchText(new DataArray(binID));
	}
	
    public static TextAndHelp_File searchText(DataArray binID) throws UnsupportedEncodingException {
    
    	TextAndHelp_File text = new TextAndHelp_File();
    	
		int textPos = searchFile("TEXT");
		if(textPos == -1){
			text.setIsSearch(false);
			return text;
		}
		//读取文件参数
		int iposText = textPos;
		short[] countTemp = new short[4];
		byte[] file_flag = new byte[6];
		byte[] reserved = new byte[7]; 
		byte[] pass_byte = new byte[12];
		boolean pass_flag = false;
		
		for(byte b = 0; b < 6; b++){
			file_flag[b] = dataBin[iposText++];
		}
		
		if( dataBin[iposText++] == 1){
			pass_flag = true;
		} else {
			pass_flag = false;
		}
		
		byte make_ver = dataBin[iposText++];
		for(byte c = 0; c < 7; c++){
			reserved[c] = dataBin[iposText++];
		}
			
		countTemp[0] = dataBin[iposText++];
		countTemp[1] = dataBin[iposText++];
		countTemp[2] = dataBin[iposText++];
		countTemp[3] = dataBin[iposText++];
		int textCount = shortToInt(countTemp);
		byte id_length = dataBin[iposText++];
		
		for(byte p = 0; p < 12; p++) {
			pass_byte[p] = dataBin[iposText++];
		}
		
		//获取搜索项的存储地址
		int itemAddr = 0;
		short[] IDtemp = new short[6];
		
		IDtemp[0] = (byte) (binID.get(0) & 0xff);
		IDtemp[1] = (byte) (binID.get(1) & 0xff);
		IDtemp[2] = (byte) (binID.get(2) & 0xff);
		IDtemp[3] = (byte) (binID.get(3) & 0xff);
		IDtemp[4] = (byte) (binID.get(4) & 0xff);
		IDtemp[5] = (byte) (binID.get(5) & 0xff);
		
		iposText = textPos;
		
		int ret = binary(textCount, IDtemp, pass_flag,pass_byte, iposText);
		if (ret == -1){
			text.setIsSearch(false);
			return text;
		} else {
			short[] addrBuf = new short[4];
			addrBuf[0] = dataBin[iposText + 32 + ret * 10 + 6];
			addrBuf[1] = dataBin[iposText + 32 + ret * 10 + 7];
			addrBuf[2] = dataBin[iposText + 32 + ret * 10 + 8];
			addrBuf[3] = dataBin[iposText + 32 + ret * 10 + 9];
			addrBuf = decypt(pass_flag,addrBuf,pass_byte);
			itemAddr = shortToInt(addrBuf);
		}
		
		short[] lengthBuf = new short[2];
		lengthBuf[0] = dataBin[iposText + itemAddr];
		lengthBuf[1] = dataBin[iposText + itemAddr + 1];
		lengthBuf = decypt(pass_flag,lengthBuf,pass_byte);
		int itemLength = (lengthBuf[0]&0xff)*0x100 + (lengthBuf[1]&0xff);
		short[] DataBuff = new short[itemLength];
		for(int z=0; z<itemLength; z++) {
			DataBuff[z] = dataBin[iposText + itemAddr + + 2 + z];
		}
		DataBuff = decypt(pass_flag,DataBuff,pass_byte);
		int txtLength = (DataBuff[2]&0xff)*0x100 + (DataBuff[3] & 0xff);
		byte[] TxtBuf = new byte[txtLength - 1];
		for(int z = 0; z < txtLength-1; z++){
			TxtBuf[z] = (byte) DataBuff[4 + z];
		}
		try {
			text.setTextOrHelp(new String(TxtBuf, Language.getCodeFormat()));
			text.setIsSearch(true);
		} catch (UnsupportedEncodingException e)
		{e.printStackTrace();}
		
		return text;
    }
    
	public static TextAndHelp_File searchHelp(String binID) throws UnsupportedEncodingException {
		return searchHelp(new DataArray(binID));
	}
	
    public static TextAndHelp_File searchHelp(DataArray binID) throws UnsupportedEncodingException {
    
	TextAndHelp_File help = new TextAndHelp_File();
    	
		int helpPos = searchFile("HELP");
		if(helpPos == -1){
			help.setIsSearch(false);
			return help;
		}
		//读取文件参数
		int iposHelp = helpPos;
		short[] countTemp = new short[4];
		byte[] file_flag = new byte[6];
		byte[] reserved = new byte[7]; 
		byte[] pass_byte = new byte[12];
		boolean pass_flag = false;
		
		for(byte b = 0; b < 6; b++){
			file_flag[b] = dataBin[iposHelp++];
		}
		
		if( dataBin[iposHelp++] == 1){
			pass_flag = true;
		} else {
			pass_flag = false;
		}
		
		byte make_ver = dataBin[iposHelp++];
		for(byte c = 0; c < 7; c++){
			reserved[c] = dataBin[iposHelp++];
		}
			
		countTemp[0] = dataBin[iposHelp++];
		countTemp[1] = dataBin[iposHelp++];
		countTemp[2] = dataBin[iposHelp++];
		countTemp[3] = dataBin[iposHelp++];
		int helpCount = shortToInt(countTemp);
		byte id_length = dataBin[iposHelp++];
		
		for(byte p = 0; p < 12; p++) {
			pass_byte[p] = dataBin[iposHelp++];
		}
		//此句用于把其它车型共用OBD通用help文件
		binID.replaceShort(0, (short)0x00);
		//获取搜索项的存储地址
		int itemAddr = 0;
		short[] IDtemp = new short[6];
		
		IDtemp[0] = (byte) (binID.get(0) & 0xff);
		IDtemp[1] = (byte) (binID.get(1) & 0xff);
		IDtemp[2] = (byte) (binID.get(2) & 0xff);
		IDtemp[3] = (byte) (binID.get(3) & 0xff);
		IDtemp[4] = (byte) (binID.get(4) & 0xff);
		IDtemp[5] = (byte) (binID.get(5) & 0xff);
		
		iposHelp = helpPos;
		
		int ret = binary(helpCount, IDtemp, pass_flag,pass_byte, iposHelp);
		if (ret == -1){
			help.setIsSearch(false);
			return help;
		} else {
			short[] addrBuf = new short[4];
			addrBuf[0] = dataBin[iposHelp + 32 + ret * 10 + 6];
			addrBuf[1] = dataBin[iposHelp + 32 + ret * 10 + 7];
			addrBuf[2] = dataBin[iposHelp + 32 + ret * 10 + 8];
			addrBuf[3] = dataBin[iposHelp + 32 + ret * 10 + 9];
			addrBuf = decypt(pass_flag,addrBuf,pass_byte);
			itemAddr = shortToInt(addrBuf);
		}
		
		short[] lengthBuf = new short[2];
		lengthBuf[0] = dataBin[iposHelp + itemAddr];
		lengthBuf[1] = dataBin[iposHelp + itemAddr + 1];
		lengthBuf = decypt(pass_flag,lengthBuf,pass_byte);
		int itemLength = (lengthBuf[0]&0xff)*0x100 + (lengthBuf[1]&0xff);
		short[] DataBuff = new short[itemLength];
		for(int z=0; z<itemLength; z++) {
			DataBuff[z] = dataBin[iposHelp + itemAddr + + 2 + z];
		}
		DataBuff = decypt(pass_flag,DataBuff,pass_byte);
		int helpLength = (int)((DataBuff[2]&0xff)*0x100) + (int)(DataBuff[3]&0xff);
		byte[] HelpBuf = new byte[helpLength - 1];
		for(int z = 0; z < helpLength-1; z++){
			HelpBuf[z] = (byte) DataBuff[4 + z];
		}
		try {
			help.setTextOrHelp(new String(HelpBuf,Language.getCodeFormat()));
			help.setIsSearch(true);
		} catch (UnsupportedEncodingException e)
		{e.printStackTrace();}
		
		return help;
    }
    
	//解密
	private static short[] decypt(boolean isDecrypt, short[] data,byte[] pass_byte){
	    short[] ret = new short[data.length];
	    if (isDecrypt){
	        String str_password = "All Rights Reserved. \nCopyright (c) 2010, AUTOOL CO.,LTD.";
	        byte[] password = str_password.getBytes();
	        int passwordLen = password.length;
	        int value;
	        for (int i = 0; i < data.length; i++){
	            value = data[i] & 0xff;
	            value ^= password[i%passwordLen]&0xff;
	            value = ~(value >> 4 | value << 4);
	            value -= ((pass_byte[i%12]&0xff) ^ (pass_byte[11-i%12]&0xff));
	            ret[i] = (short)(value & 0xFF);
	        }
	    } else {
	        ret = data;
	    }
	    return ret;
	}
	
	public static void initBin(int length){
		dataBin = new byte[length];		
	}
	
	public static byte[] getdataBin(){
		return dataBin;
	}

    public DataBaseBin() {
    }
    
	public static short[] byteToshort(byte [] temp){
		short tempEven[] = new short[temp.length];
		for(int i = 0;i < tempEven.length;i++){
			tempEven[i] = (short)(temp[i]&0xFF);
		}
		return tempEven;
	}
     
	private static String getBinVersion(){
		int verLength = dataBin[0];
		byte[] verBuffer = new byte[verLength];
		for(int i = 0;i < verLength;i++) {
			verBuffer[i] = dataBin[i+1];
		}
		return new String(verBuffer);
	}	

	public static DS_File searchDS(String strID) throws UnsupportedEncodingException {
		if(strID == null){
			return new DS_File();
		}
		return searchDS(new DataArray(strID));
	}
	
	public static DS_File searchDS(DataArray binID) throws UnsupportedEncodingException {
		
		DS_File ds = new DS_File();	

		int dsPos = searchFile("DS");
		
		if(dsPos == -1){
			ds.setIsSearch(false);
			return ds;
		}
		int iposDS = dsPos;
		
		//读取文件参数
		short[] countTemp = new short[4];
		byte[] file_flag  = new byte[6];
		byte[] reserved   = new byte[7]; 
		byte[] pass_byte  = new byte[12];
		boolean pass_flag = false;
		
		for(int b = 0; b < 6; b++){
			file_flag[b] = dataBin[dsPos++];
		}
		
		if( dataBin[dsPos++] == 1) {
			pass_flag = true;
		} else {
			pass_flag = false;
		}
		
		byte make_ver = dataBin[dsPos++];
		for(int c = 0; c < 7; c++) {
			reserved[c] = dataBin[dsPos++];
		}
			
		countTemp[0] = dataBin[dsPos++];
		countTemp[1] = dataBin[dsPos++];
		countTemp[2] = dataBin[dsPos++];
		countTemp[3] = dataBin[dsPos++];
		int dsCount = shortToInt(countTemp);
		byte id_length = dataBin[dsPos++];
		for(byte p = 0; p < 12; p++){
			pass_byte[p] = dataBin[dsPos++];
		}		
		//获取搜索项的存储地址
		int itemAddr = 0;
		short[] IDtemp = new short[6];
		
		IDtemp[0] = (byte) (binID.get(0) & 0xff);
		IDtemp[1] = (byte) (binID.get(1) & 0xff);
		IDtemp[2] = (byte) (binID.get(2) & 0xff);
		IDtemp[3] = (byte) (binID.get(3) & 0xff);
		IDtemp[4] = (byte) (binID.get(4) & 0xff);
		IDtemp[5] = (byte) (binID.get(5) & 0xff);
		
		int ret = binary(dsCount, IDtemp, pass_flag,pass_byte, iposDS);
		if (ret == -1){
			ds.setIsSearch(false);
			return ds;
		} else {
			short[] addrBuf = new short[4];
			addrBuf[0] = dataBin[iposDS + 32 + ret * 10 + 6];
			addrBuf[1] = dataBin[iposDS + 32 + ret * 10 + 7];
			addrBuf[2] = dataBin[iposDS + 32 + ret * 10 + 8];
			addrBuf[3] = dataBin[iposDS + 32 + ret * 10 + 9];
			addrBuf = decypt(pass_flag,addrBuf,pass_byte);
			itemAddr = shortToInt(addrBuf);
		}
		//获取有效数据长度
		short[] lengthBuf = new short[2];
		lengthBuf[0] = dataBin[iposDS + itemAddr];
		lengthBuf[1] = dataBin[iposDS + itemAddr + 1];
		lengthBuf = decypt(pass_flag,lengthBuf,pass_byte);
		
		int dataLength = (lengthBuf[0]&0xff)*0x100 + (lengthBuf[1]&0xff);
		
		//获取有效数据
		short[] DataBuff = new short[dataLength];
		for(int z=0; z<dataLength; z++) {
			DataBuff[z] = dataBin[iposDS + itemAddr + + 2 + z];
		}
		DataBuff = decypt(pass_flag,DataBuff,pass_byte);
		
		//解析和打包有效数据
		int dataPos = 2;
		int DSNameLength = (DataBuff[dataPos++]&0xff)*0x100 + DataBuff[dataPos++]&0xff;
		byte[] DSName = new byte[DSNameLength-1];
		for(int z = 0; z < DSNameLength-1; z++) {
			DSName[z] = (byte) DataBuff[dataPos + z];
		}
		ds.setDSName(new String(DSName,Language.getCodeFormat()));
		dataPos += DSNameLength ;
		
		int DSUnitLength = (DataBuff[dataPos++]&0xff)*0x100 + DataBuff[dataPos++]&0xff;
		byte[] DSUnit = new byte[DSUnitLength-1];
		for(int z=0; z<DSUnitLength-1; z++) {
			DSUnit[z] = (byte) DataBuff[dataPos + z];
		}
		ds.setDSUnit(new String(DSUnit,Language.getCodeFormat()));
		dataPos += DSUnitLength;
		
		int DSFormatLength = (DataBuff[dataPos++]&0xff)*0x100 + DataBuff[dataPos++]&0xff;
		byte[] DSFormat = new byte[DSFormatLength-1];
		for(int z=0; z<DSFormatLength-1; z++) {
			DSFormat[z] = (byte) DataBuff[dataPos + z];
		}
		ds.setDSFormat(new String(DSFormat,Language.getCodeFormat()));
		dataPos += DSFormatLength;
		
		int DSAlgorithmLength = (DataBuff[dataPos++]&0xff)*0x100 + DataBuff[dataPos++]&0xff;
		byte[] DSAlgorithm = new byte[DSAlgorithmLength-1];
		for(int z=0; z<DSAlgorithmLength-1; z++) {
			DSAlgorithm[z] = (byte) DataBuff[dataPos + z];
		}
		ds.setDSAlgorithm(new String(DSAlgorithm,Language.getCodeFormat()));
		dataPos += DSAlgorithmLength;
		
		if(dataPos < dataLength-1){
			int DSCommandLength = (DataBuff[dataPos++]&0xff)*0x100 + DataBuff[dataPos++]&0xff;
			short[] DSCommand = new short[DSCommandLength];
			for(int z=0; z<DSCommandLength; z++) {
				DSCommand[z] = DataBuff[dataPos + z];
			}
			ds.setDSCommand(new DataArray(DSCommand,DSCommand.length));
		}
		
			ds.setSearchID(binID.binaryToCommand());
			ds.setIsSearch(true);
		return ds;
	}
	/*
	 * parameter �����ļ���� "TEXT"  "DS"  "DTC"  "HELP"
	 * return ����ļ�����ʼ�±�
	 * return -1 δ�ҵ�����ļ�
	 */
	private static int searchFile(String searchFileName){
		int index = getBinVersion().length()+1;
		int fileCount = dataBin[index++];

		int filePos = 0;
		for(filePos = 0;filePos < fileCount;filePos++){
			byte[] fileNameBuffer = new byte[1024];
			byte xorNumber = dataBin[index++];
			int fileNameLength = (dataBin[index++]&0xFF);

			for(int j = 0;j < fileNameLength;j++){
				fileNameBuffer[j] = (byte) (((dataBin[index++]&0xFF))^xorNumber);
			}
			String fileName = new String(fileNameBuffer);
			if(fileName.trim().equals(searchFileName)){
				index += 4;
				break;
			} else {
				int fileSize = 0;
				fileSize += ((dataBin[index++]&0xFF)) <<24;
				fileSize += ((dataBin[index++]&0xFF)) <<16;
				fileSize += ((dataBin[index++]&0xFF)) <<8;
				fileSize += ((dataBin[index++]&0xFF)) ;
				index += fileSize;
			}
		}
		if(filePos == fileCount){
			//"not find file!"
			return -1;
		}
		return index;
	}
	


	public static int binary(int idCount, short[] value,boolean isDecrypt,byte[] pass_byte,int ipos) {
		int low = 0;
		int high = idCount - 1;
		short id[] = new short[value.length];
		while (low <= high) {
			int middle = (low + high) / 2;
			for(int i=0;i<id.length;i++) {
				id[i] = dataBin[ipos + 32 + middle*(value.length + 4) + i];
			}
			
			id = decypt(isDecrypt,id,pass_byte);
			
			if (compareShortArray(value,id) == 0) {
				return middle;
			} 
			if (compareShortArray(value,id) == 1) {
				low = middle + 1;
			}
			if (compareShortArray(value,id) == -1) {
				high = middle - 1;
			}
			if (compareShortArray(value,id) == -2) {
				return -1;
			}
		}
		return -1;
	}
	
	public static int compareShortArray(short[] value1, short[] value2) {
		
		if(value1.length != value2.length) return -2;
		for(int i = 0;i < value1.length;i++) {
			if((value1[i]&0xff) > (value2[i]&0xff)) return 1;
			if((value1[i]&0xff) < (value2[i]&0xff)) return -1;
		}
		return 0;
	}
	
}
