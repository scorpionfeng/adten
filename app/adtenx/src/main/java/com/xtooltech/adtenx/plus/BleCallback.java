package com.xtooltech.adtenx.plus;

public interface BleCallback {

    void onConnected();
    void onDisconnected();
    void onConnectTimeout();

    void onFoundUuid(int time);
    void onNotFoundUuid();

    void onCharacteristicChanged(byte[] data);

}
