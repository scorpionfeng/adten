package com.xtooltech.adten.common.ble;

public class UtilData {

    public static boolean isSupported(byte pid,byte[] data){
        byte bitPos=0;
        byte bytePos=0;
        bitPos= (byte) ((pid%8)>0?(pid/8):(pid/8-1));
        bytePos= (byte) ((pid%8)>0?(pid/8):(pid/8-1));
        byte bVal= (byte) (0x01<<bitPos);
        if((data[bytePos] & bVal)>0) {
            return true;
        }else{
            return false;
        }
    }
}
