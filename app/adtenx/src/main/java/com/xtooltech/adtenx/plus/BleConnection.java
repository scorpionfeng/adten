package com.xtooltech.adtenx.plus;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BleConnection implements Communication.CommunicationInterface {
    public static final String TAG = "BleConnection";

    public static final UUID AD10_SERVICE_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    public static final UUID AD10_WRITE_UUID = UUID.fromString("6E400004-B5A3-F393-E0A9-E50E24DCCA9E");
    public static final UUID AD10_READ_UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E");

    public static final UUID ASD60_SERVICE_UUID = UUID.fromString("0000FEE7-0000-1000-8000-00805F9B34FB");
    public static final UUID ASD60_WRITE_UUID = UUID.fromString("000036F5-0000-1000-8000-00805F9B34FB");
    public static final UUID ASD60_READ_UUID = UUID.fromString("000036F6-0000-1000-8000-00805F9B34FB");

    private static final int MSG_CALLBACK_CONNECTED = 0;
    private static final int MSG_CALLBACK_DISCONNECTED = 1;
    private static final int MSG_CALLBACK_FOUND_UUID = 2;
    private static final int MSG_CALLBACK_NOT_FOUND_UUID = 3;
    private static final int MSG_CALLBACK_NOTIFY = 4;
    private static final int MSG_CALLBACK_CONNECT_TIMEOUT = 5;

    private static final int MSG_START = 10;
    private static final int MSG_STOP = 11;
    private static final int MSG_DISCOVER_SERVICES = 12;
    private static final int MSG_WRITE = 13;

    private Context mContext;
    private boolean mAD10;
    private String mMacAddress;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothGatt mBluetoothGatt;
    private BleCallback mBleCallback;

    private BluetoothGattCharacteristic mWriteCharacteristic;
    private BluetoothGattCharacteristic mReadCharacteristic;

    private long mStartTime;
    private LastState mLastState = LastState.STATE_STOP;

    private List<byte[]> mSendDataArray = new ArrayList<>();

    private static class BleHandler extends Handler {
        private final WeakReference<BleConnection> mBleConnection;

        private BleHandler(BleConnection bleConnection) {
            mBleConnection = new WeakReference<>(bleConnection);
        }

        @Override
        public void handleMessage(Message msg) {
            BleConnection bleConnection = mBleConnection.get();
            if (bleConnection != null) {
                switch (msg.what) {
                    case MSG_CALLBACK_CONNECTED:
                        bleConnection.onMsgConnected();
                        break;
                    case MSG_CALLBACK_DISCONNECTED:
                        bleConnection.onMsgDisconnected();
                        break;
                    case MSG_CALLBACK_FOUND_UUID:
                        bleConnection.onMsgFoundUuid(msg.arg1);
                        break;
                    case MSG_CALLBACK_NOT_FOUND_UUID:
                        bleConnection.onMsgNotFoundUuid();
                        break;
                    case MSG_CALLBACK_NOTIFY:
                        bleConnection.onMsgNotify((String) msg.obj);
                        break;
                    case MSG_CALLBACK_CONNECT_TIMEOUT:
                        bleConnection.onMsgConnectTimeout();
                        break;
                    case MSG_START:
                        bleConnection.onMsgStart();
                        break;
                    case MSG_STOP:
                        bleConnection.onMsgStop();
                        break;
                    case MSG_DISCOVER_SERVICES:
                        bleConnection.onMsgDiscoverServices();
                        break;
                    case MSG_WRITE:
                        bleConnection.onMsgWrite((String) msg.obj);
                        break;
                }
            }
        }
    }

    private final BleHandler handler = new BleHandler(this);
    private Runnable runnable = new Runnable(){
        @Override
        public void run() {
            if (mLastState == LastState.STATE_START) {
                handler.sendEmptyMessage(MSG_STOP);
                handler.sendEmptyMessageDelayed(MSG_START, 3000);
            }
        }
    };

    public BleConnection(Context context, boolean ad10, String macAddress) {
        mContext = context;
        mAD10 = ad10;
        mMacAddress = macAddress;
        BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mMacAddress);
    }

    public void addBleCallback(BleCallback callback) {
        mBleCallback = callback;
    }

    public void removeBleCallback() {
        mBleCallback = null;
    }

    public void start() {
        mStartTime = System.currentTimeMillis();
        handler.sendEmptyMessage(MSG_START);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mLastState == LastState.STATE_START) {
                    handler.removeCallbacks(runnable);
                    handler.sendEmptyMessage(MSG_STOP);
                    handler.sendEmptyMessage(MSG_CALLBACK_CONNECT_TIMEOUT);
                }
            }
        }, 35000);
    }

    public boolean isConnecting() {
        return mLastState == LastState.STATE_START;
    }

    public void stop() {
        handler.sendEmptyMessage(MSG_STOP);
    }

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "onConnectionStateChange status=" + status + " newState=" + newState);
            if (mLastState == LastState.STATE_START) {
                boolean isReconnect = true;
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (newState == BluetoothGatt.STATE_CONNECTED) {
                        mLastState = LastState.STATE_CONNECTED;
                        handler.sendEmptyMessage(MSG_CALLBACK_CONNECTED);
                        handler.sendEmptyMessage(MSG_DISCOVER_SERVICES);
                        isReconnect = false;
                    }
                }
                if (isReconnect) {
                    handler.removeCallbacks(runnable);
                    handler.sendEmptyMessage(MSG_STOP);
                    handler.sendEmptyMessageDelayed(MSG_START, 3000);
                }
            } else {
                if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    if (mLastState == LastState.STATE_CONNECTED || mLastState == LastState.STATE_FOUND_UUID) {
                        mLastState = LastState.STATE_DISCONNECTED;
                        handler.sendEmptyMessage(MSG_CALLBACK_DISCONNECTED);
                        handler.sendEmptyMessage(MSG_STOP);
                    }
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "onServicesDiscovered status:" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                boolean isFoundUuid = false;
                BluetoothGattService service = gatt.getService(mAD10 ? AD10_SERVICE_UUID : ASD60_SERVICE_UUID);
                if (service != null) {
                    mWriteCharacteristic = service.getCharacteristic(mAD10 ? AD10_WRITE_UUID : ASD60_WRITE_UUID);
                    mReadCharacteristic = service.getCharacteristic(mAD10 ? AD10_READ_UUID : ASD60_READ_UUID);
                    if (mWriteCharacteristic != null && mReadCharacteristic != null) {
                        gatt.setCharacteristicNotification(mWriteCharacteristic, true);
                        gatt.setCharacteristicNotification(mReadCharacteristic, true);
                        for (BluetoothGattDescriptor dp : mReadCharacteristic.getDescriptors()) {
                            dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            gatt.writeDescriptor(dp);
                        }
                        isFoundUuid = true;
                    }
                }
                if (isFoundUuid) {
                    mLastState = LastState.STATE_FOUND_UUID;
                    Message message = handler.obtainMessage();
                    message.what = MSG_CALLBACK_FOUND_UUID;
                    message.arg1 = (int) (System.currentTimeMillis() - mStartTime);
                    handler.sendMessageDelayed(message, 1000);
                } else {
                    handler.sendEmptyMessage(MSG_CALLBACK_NOT_FOUND_UUID);
                    handler.sendEmptyMessage(MSG_STOP);
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if (characteristic.getUuid().equals(mReadCharacteristic.getUuid())) {
                Message message = handler.obtainMessage();
                message.what = MSG_CALLBACK_NOTIFY;
                message.obj = new String(Base64.encode(characteristic.getValue(), Base64.DEFAULT));
                handler.sendMessage(message);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, "onCharacteristicWrite status=" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (!mSendDataArray.isEmpty()) {
                    Message message = handler.obtainMessage();
                    message.what = MSG_WRITE;
                    message.obj = new String(Base64.encode(mSendDataArray.get(0), Base64.DEFAULT));
                    handler.sendMessage(message);
                    mSendDataArray.remove(0);
                }
            }
        }
    };

    @Override
    public void clear() {
        //Log.d(TAG, "clear buffer data");
    }

    @Override
    public void write(List<byte[]> dataArray) {
        mSendDataArray.clear();
        Message message = handler.obtainMessage();
        message.what = MSG_WRITE;
        message.obj = new String(Base64.encode(dataArray.get(0), Base64.DEFAULT));
        handler.sendMessage(message);
        for (int i = 1; i < dataArray.size(); i++) {
            mSendDataArray.add(dataArray.get(i));
        }
    }

    enum LastState {
        STATE_STOP,
        STATE_START,
        STATE_CONNECTED,
        STATE_FOUND_UUID,
        STATE_DISCONNECTED
    }

    private void onMsgConnected() {
        if (mBleCallback != null) {
            mBleCallback.onConnected();
        }
    }

    private void onMsgDisconnected() {
        if (mBleCallback != null) {
            mBleCallback.onDisconnected();
        }
    }

    private void onMsgFoundUuid(int time) {
        if (mBleCallback != null) {
            mBleCallback.onFoundUuid(time);
        }
    }

    private void onMsgNotFoundUuid() {
        if (mBleCallback != null) {
            mBleCallback.onNotFoundUuid();
        }
    }

    private void onMsgNotify(String data) {
        if (mBleCallback != null) {
            mBleCallback.onCharacteristicChanged(Base64.decode(data, Base64.DEFAULT));
        }
    }

    private void onMsgConnectTimeout() {
        if (mBleCallback != null) {
            mBleCallback.onConnectTimeout();
        }
    }

    private void onMsgStart() {
        Log.d(TAG, "connectGatt");
        mLastState = LastState.STATE_START;
        mBluetoothGatt = mBluetoothDevice.connectGatt(mContext,false, mGattCallback);
        handler.postDelayed(runnable, 12000);
    }

    private void onMsgStop() {
        if (mBluetoothGatt != null) {
            if (mLastState == LastState.STATE_CONNECTED || mLastState == LastState.STATE_FOUND_UUID) {
                Log.d(TAG, "disconnect");
                mBluetoothGatt.disconnect();
            } else {
                Log.d(TAG, "close");
                mLastState = LastState.STATE_STOP;
                mBluetoothGatt.close();
                mBluetoothGatt = null;
            }
        }
    }

    private void onMsgDiscoverServices() {
        if (mBluetoothGatt != null) {
            Log.d(TAG, "discoverServices");
            mBluetoothGatt.discoverServices();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mLastState == LastState.STATE_CONNECTED) {
                        handler.sendEmptyMessage(MSG_CALLBACK_NOT_FOUND_UUID);
                        handler.sendEmptyMessage(MSG_STOP);
                    }
                }
            }, 10000);
        }
    }

    private void onMsgWrite(String data) {
        if (mBluetoothGatt != null) {
            byte[] value = Base64.decode(data, Base64.DEFAULT);
            Log.d(TAG, "发送:" + Utils.debugByteData(value));
            mWriteCharacteristic.setValue(value);
            mBluetoothGatt.writeCharacteristic(mWriteCharacteristic);
        }
    }
}
