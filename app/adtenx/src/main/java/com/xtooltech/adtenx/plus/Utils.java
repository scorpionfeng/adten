package com.xtooltech.adtenx.plus;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    private static final String TAG = "Utils";

    public static final String DETECT_RSSI = "DETECT_RSSI";           //是否开启靠一靠功能
    public static final String SCAN_RSSI = "SCAN_RSSI";                //信号强度阈值
    public static final String DEFAULT_SCAN_RSSI = "-50";              //默认信号强度阈值-50db

    public static boolean isValidMac(final String str) {
        if (str == null) return false;
        if (str.length() != 12) return false;
        boolean ret = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!(c >= '0' && c <= '9') && !(c >= 'A' && c <= 'F')) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public static String getVersionName(Context context) {
        String ret = "";
        try {
            ret = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String debugByteData(byte[] data) {
        String ret = new String();
        for (int i = 0; i < data.length; i++) {
            ret += String.format("%02X ", data[i]);
        }
        return ret;
    }

    public static byte[] ipString2Bytes(String strIp) {
        byte[] ret = null;
        if (!strIp.isEmpty()) {
            String[] ss = strIp.split("\\.");
            if (ss.length == 4) {
                ret = new byte[4];
                boolean correct = true;
                for (int i = 0; i < 4; i++) {
                    try {
                        int port = Integer.parseInt(ss[i]);
                        if (port >= 256 || port < 0) {
                            correct = false;
                            break;
                        } else {
                            ret[i] = (byte) ((port >= 128) ? (port - 256) : port);
                        }
                    } catch (NumberFormatException e) {
                        correct = false;
                        break;
                    }
                }
                if (!correct) {
                    ret = null;
                }
            }
        }
        return ret;
    }

    public static String calcMd5(String str) {
        String ret = "";
        if (str != null && !str.isEmpty()) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] bytes = md5.digest(str.getBytes());
                StringBuilder result = new StringBuilder();
                for (byte b : bytes) {
                    result.append(String.format("%02X", b));
                }
                ret = result.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static int byteArray2int(byte[] bs) {
        int i = 0;
        for (int m = 0; m < bs.length; m++) {
            i |= ((bs[m] < 0) ? (bs[m] + 256) : bs[m]) << ((bs.length - m - 1) * 8);
        }
        return i;
    }

    public static byte int2byte(int i) {
        byte b;
        if (i < 0x80) {
            b = (byte) i;
        } else {
            b = (byte) ((i & 0xFF) - 256);
        }
        return b;
    }

    public static int byte2int(byte b) {
        int i;
        if (b < 0) {
            i = 256 + b;
        } else {
            i = b;
        }
        return i;
    }

    public static byte getCrc(byte val, byte  crc) {
        int ii = 8;
        while (ii-- > 0) {
            if (((val ^ crc) & 0x80) != 0) {
                crc ^= 0x0e;
                crc = (byte) ((crc << 1) | 1);
            }
            else crc = (byte) (crc << 1);
            val = (byte) (val << 1);
        }
        return crc;
    }

    public static byte[] comboCanCommand(boolean stdCan, byte[] data) {
        byte[] ret;
        int length = data.length > 7 ? 7 : data.length;
        if (stdCan) {
            ret = new byte[11];
            ret[0] = 0x08;
            ret[1] = 0x07;
            ret[2] = 0xDF - 256;
            ret[3] = Utils.int2byte(length);
            System.arraycopy(data, 0, ret, 4, length);
        } else {
            ret = new byte[13];
            ret[0] = 0x88 - 256;
            ret[1] = 0x18;
            ret[2] = 0xDB - 256;
            ret[3] = 0x33;
            ret[4] = 0xF1 - 256;
            ret[5] = Utils.int2byte(length);
            System.arraycopy(data, 0, ret, 6, length);
        }
        return ret;
    }

    public static byte[] comboIsoCommand(byte[] data) {
        byte[] ret;
        ret = new byte[data.length + 4];
        ret[0] = 0x68;
        ret[1] = 0x6A;
        ret[2] = 0xF1 - 256;
        System.arraycopy(data, 0, ret, 3, data.length);
        byte sum = 0;
        for (int i = 0; i < ret.length - 1; i++) {
            sum += ret[i];
        }
        ret[ret.length - 1] = sum;
        return ret;
    }

    public static byte[] comboKwpCommand(byte[] data) {
        byte[] ret;
        ret = new byte[data.length + 4];
        ret[0] = Utils.int2byte(0xC0 + data.length);
        ret[1] = 0x33;
        ret[2] = 0xF1 - 256;
        System.arraycopy(data, 0, ret, 3, data.length);
        byte sum = 0;
        for (int i = 0; i < ret.length - 1; i++) {
            sum += ret[i];
        }
        ret[ret.length - 1] = sum;
        return ret;
    }

    public static byte[] comboPwmVpwCommand(boolean pwm, byte[] data) {
        byte[] ret;
        ret = new byte[data.length + 4];
        ret[0] = Utils.int2byte(pwm ? 0x61 : 0x68);
        ret[1] = 0x6A;
        ret[2] = 0xF1 - 256;
        System.arraycopy(data, 0, ret, 3, data.length);
        byte crc = 0xff - 256;
        for (int i = 0; i < ret.length - 1; i++) {
            crc = Utils.getCrc(ret[i], crc);
        }
        ret[ret.length - 1] = (byte) (~crc);
        return ret;
    }

    public static void showKeyboard(Context context, EditText editText) {
        if (editText != null){
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.showSoftInput(editText, 0);
            }
        }
    }

    public static byte[] getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return digest.digest();
    }
}
