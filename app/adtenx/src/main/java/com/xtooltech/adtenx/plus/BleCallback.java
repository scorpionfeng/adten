package com.xtooltech.adtenx.plus;

/**
 * 蓝牙状态回调
 */
public interface BleCallback {

    void onConnected();
    void onDisconnected();
    void onConnectTimeout();

    void onFoundUuid(int time);
    void onNotFoundUuid();

    void onCharacteristicChanged(byte[] data);

}
