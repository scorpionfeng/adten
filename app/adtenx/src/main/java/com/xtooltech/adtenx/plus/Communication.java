package com.xtooltech.adtenx.plus;

import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Communication {
    private static final String TAG = "Communication";

    private CommunicationInterface mInstance;
    private ConcurrentLinkedQueue<Byte> mByteQueue = new ConcurrentLinkedQueue<>();
    private final Object mSyncObject = new Object();
    private byte[] mReceiveData;
    private int mPackageId = 0;
    private boolean mSendReceiving = false;

    private Set<Integer> mEcuIds = new HashSet<>();

    public Communication(CommunicationInterface instance) {
        mInstance = instance;
    }

    public boolean isSendReceiving() {
        return mSendReceiving;
    }

    public void addByte(byte data) {
        mByteQueue.add(data);
    }

    public void addByteArray(byte[] bytes) {
        List<Byte> list = new ArrayList<>();
        for (byte aByte : bytes) {
            list.add(aByte);
        }
        mByteQueue.addAll(list);
    }

    private short getByte(long timeout) {
        short ret = 256;
        long start = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - start > timeout) break;
            if (!mByteQueue.isEmpty()) {
                ret = mByteQueue.poll();
                break;
            } else {
                SystemClock.sleep(10);
            }
        }
        return ret;
    }
    private byte[] getBytes(long timeout) {
        List<Byte> list = new ArrayList<>();
        short b, b1;
        while (true) {
            b = getByte(timeout); if (b == 256) return null; if (b != 0x55) continue; list.add((byte) b);
            while (true) {
                b = getByte(timeout); if (b == 256) return null;
                if (b == 0x56/* || b == 0xAB - 256*/) {
                    b1 = getByte(timeout); if (b1 == 256) return null;
                    if (b1 == 0x01) {
                        list.add((byte) b);
                    } else if (b1 == 0x02) {
                        list.add((byte) 0x55);
                    } else {
                        list.add((byte) b); list.add((byte) b1);
                    }
                } else {
                    list.add((byte) b);
                }
                if (list.size() > 3 && Utils.byte2int(list.get(2)) + 5 == list.size()) {
                    byte cs = 0;
                    for (int i = 1; i < list.size() - 1; i++) {
                        cs ^= list.get(i);
                    }
                    if (cs == list.get(list.size() - 1)) {
                        byte[] ret = new byte[list.size() - 4];
                        for (int i = 3; i < list.size() - 1; i++) {
                            ret[i - 3] = list.get(i);
                        }
                        return ret;
                    } else {
                        list.clear();
                        break;
                    }
                }
            }
        }
    }

    private byte[] byte2bytes(byte b) {
        if (b == 0xAA - 256) {
            return new byte[] { 0xAB - 256, 0x02 };
        } else if (b == 0xAB - 256) {
            return new byte[] { 0xAB - 256, 0x01 };
        }
        return new byte[] { b };
    }

    private void sendDataList(List<byte[]> dataList) {
        for (byte[] data : dataList) {
            Log.d(TAG, "send: " + Utils.debugByteData(data));
            List<Byte> list = new ArrayList<>();
            byte cs = 0;
            cs ^= Utils.int2byte(mPackageId);
            cs ^= Utils.int2byte(data.length - 2 + 1);
            for (byte b : data) {
                cs ^= b;
            }
            list.add(Utils.int2byte(0xAA));
            byte[] bs;
            bs = byte2bytes(Utils.int2byte(mPackageId));
            for (byte b: bs) {
                list.add(b);
            }
            bs = byte2bytes(Utils.int2byte(data.length - 2 + 1));
            for (byte b: bs) {
                list.add(b);
            }
            for (byte datum : data) {
                bs = byte2bytes(datum);
                for (byte b : bs) {
                    list.add(b);
                }
            }
            bs = byte2bytes(cs);
            for (byte b : bs) {
                list.add(b);
            }
            int count = (list.size() % 20  == 0) ? (list.size() / 20) : (list.size() / 20 + 1);
            List<byte[]> sendArray = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                byte[] frameData = new byte[(i == count - 1) ? (list.size() - 20 * i) : 20];
                for (int j = 0; j < frameData.length; j++) {
                    frameData[j] = list.get(j + 20 * i);
                }
                sendArray.add(frameData);
            }
            mInstance.write(sendArray);
            mPackageId++;
            if (mPackageId == 256) mPackageId = 0;
        }
    }

    private List<byte[]> receiveDataList(long timeout, int expectReceiveCount) {
        final List<byte[]> dataList = new ArrayList<>(expectReceiveCount);
        while (dataList.size() < expectReceiveCount) {
            synchronized (mSyncObject) {
                try {
                    mReceiveData = null;
                    final long finalTimeout = timeout;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
//                            Log.d(TAG, "timeout=" + finalTimeout);
                            mReceiveData = getBytes(finalTimeout);
                            synchronized (mSyncObject) {
//                                Log.d(TAG, "mSyncObject notifyAll");
                                mSyncObject.notifyAll();
                            }
                        }
                    }).start();
//                    Log.d(TAG, "mSyncObject wait");
                    mSyncObject.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
            if (mReceiveData != null) {
                Log.d(TAG, "recv: " + Utils.debugByteData(mReceiveData));
                dataList.add(Arrays.copyOf(mReceiveData, mReceiveData.length));
                timeout = 1500;  //当收多帧的时候，将后面帧的等待时间减短
                continue;
            }
            Log.d(TAG, "recv: timeout");
            break;
        };
        return dataList;
    }

    private synchronized List<byte[]> sendReceiveDataList(List<byte[]> dataList, long timeout, int expectReceiveCount) {
        mSendReceiving = true;
        mInstance.clear();
        mByteQueue.clear();
        sendDataList(dataList);
        List<byte[]> ret = receiveDataList(timeout, expectReceiveCount);
        mSendReceiving = false;
        return ret;
    }

    private byte[] sendReceiveData(byte[] data, long timeout) {
        List<byte[]> dataList = new ArrayList<>(1);
        dataList.add(data);
        List<byte[]> ret = sendReceiveDataList(dataList, timeout, 1);
        if (ret.size() == 1) {
            return ret.get(0);
        }
        return null;
    }

    public String readBoxInfo() {
        byte[] data = new byte[7];
        data[0] = Utils.int2byte(0x60);
        data[1] = Utils.int2byte(0x02);
        data[2] = Utils.int2byte(0x04);
        data[3] = Utils.int2byte(0x80);
        data[4] = Utils.int2byte(0x81);
        data[5] = Utils.int2byte(0x82);
        data[6] = Utils.int2byte(0x83);
        byte[] recv = sendReceiveData(data, 5000);
        if (recv != null && recv.length > 3 && recv[0] == 0x60 && recv[1] == 0x02) {
            int len = Utils.byte2int(recv[2]), pos = 3;
            byte[] tmp;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                int kid = Utils.byte2int(recv[pos]);
                if (kid == 0x80) {
                    tmp = Arrays.copyOfRange(recv, pos + 1, pos + 3);
                    sb.append("电压:");
                    sb.append(String.format(Locale.getDefault(), "%.2f A", Utils.byteArray2int(tmp) * 1.0 / 1000));
                    sb.append("\n");
                    pos += 2;
                } else if (kid == 0x81) {
                    tmp = Arrays.copyOfRange(recv, pos + 1, pos + 11);
                    sb.append("固件版本:");
                    sb.append(new String(tmp));
                    sb.append("\n");
                    pos += 10;
                } else if (kid == 0x82) {
                    tmp = Arrays.copyOfRange(recv, pos + 1, pos + 21);
                    sb.append("序列号:");
                    sb.append(new String(tmp));
                    sb.append("\n");
                    pos += 20;
                } else if (kid == 0x83) {
                    tmp = Arrays.copyOfRange(recv, pos + 1, pos + 2);
                    sb.append("状态:");
                    sb.append(Utils.byte2int(tmp[0]));
                    sb.append("\n");
                    pos += 1;
                }
                pos += 1;
            }
            return sb.toString();
        }
        return null;
    }

    public boolean controlBox(int n) {
        byte[] data = new byte[4];
        data[0] = Utils.int2byte(0x60);
        data[1] = Utils.int2byte(0x03);
        data[2] = Utils.int2byte(0x01);
        data[3] = Utils.int2byte(n);
        byte[] recv = sendReceiveData(data, 3000);
        if (recv != null && recv.length > 4 && recv[0] == 0x60 && recv[1] == 0x0B) {
            return recv[4] == 1;
        }
        return false;
    }

    public boolean initFirmwareUpdate(File file) {
        byte[] fileMd5 = Utils.getFileMD5(file);
        if (fileMd5 == null) return false;

        List<Byte> list = new ArrayList<>();
        list.add(Utils.int2byte(0x60));
        list.add(Utils.int2byte(0x07));

        list.add(Utils.int2byte(fileMd5.length));
        for (byte b : fileMd5) {
            list.add(b);
        }

        String fileName = file.getName();
        list.add(Utils.int2byte(fileName.length()));
        for (int i = 0; i < fileName.length(); i++) {
            list.add(Utils.int2byte(fileName.codePointAt(i)));
        }

        int fileSize = (int) file.length();
        list.add(Utils.int2byte((fileSize >> 24) & 0xFF));
        list.add(Utils.int2byte((fileSize >> 16) & 0xFF));
        list.add(Utils.int2byte((fileSize >> 8) & 0xFF));
        list.add(Utils.int2byte(fileSize & 0xFF));

        byte[] data = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        byte[] recv = sendReceiveData(data, 3500);
        if (recv != null && recv.length > 4 && recv[0] == 0x60 && recv[1] == 0x0B) {
            return recv[4] == 1;
        }
        return false;
    }

    public boolean updateOneFrameFirmware(byte[] oneFrame, int offset, int length) {
        byte[] data = new byte[length + 3];
        data[0] = Utils.int2byte(0x60);
        data[1] = Utils.int2byte(0x08);
        data[2] = Utils.int2byte(length);
        System.arraycopy(oneFrame, offset, data, 3, length);
        byte[] recv = sendReceiveData(data, 4000);
        if (recv != null && recv.length > 4 && recv[0] == 0x60 && recv[1] == 0x0B) {
            return recv[4] == 1;
        }
        return false;
    }

    private boolean configPwmVpw(boolean pwm) {
        List<Byte> list = new ArrayList<>();
        list.add(Utils.int2byte(0x60));
        list.add(Utils.int2byte(0x01));
        list.add(Utils.int2byte(0x08));  //参数个数
        list.add(Utils.int2byte(0x01));  //协议类型
        list.add(Utils.int2byte(pwm ? 0x04 : 0x05));
        int P1 = 200;
        list.add(Utils.int2byte(0x03));  //P1
        list.add(Utils.int2byte((P1 >> 8) & 0xFF));
        list.add(Utils.int2byte(P1 & 0xFF));
        int P2 = 3000;
        list.add(Utils.int2byte(0x04));  //P2
        list.add(Utils.int2byte((P2 >> 8) & 0xFF));
        list.add(Utils.int2byte(P2 & 0xFF));
        int P3 = 55;
        list.add(Utils.int2byte(0x05));  //P3
        list.add(Utils.int2byte((P3 >> 8) & 0xFF));
        list.add(Utils.int2byte(P3 & 0xFF));
        int P4 = 0;
        list.add(Utils.int2byte(0x06));  //P4
        list.add(Utils.int2byte((P4 >> 8) & 0xFF));
        list.add(Utils.int2byte(P4 & 0xFF));
        list.add(Utils.int2byte(0x0D));  //过滤帧偏移
        list.add(Utils.int2byte(0x00));
        list.add(Utils.int2byte(0x0E));  //过滤帧长度
        list.add(Utils.int2byte(0x02));
        list.add(Utils.int2byte(0x0F));  //过滤帧
        list.add(Utils.int2byte(pwm ? 0x41 : 0x48));
        list.add(Utils.int2byte(0x6B));
        byte[] data = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        byte[] recv = sendReceiveData(data, 3000);
        if (recv != null && recv.length > 4 && recv[0] == 0x60 && recv[1] == 0x0B) {
            return recv[4] == 1;
        }
        return false;
    }

    private boolean configKwp(boolean addressCode) {
        List<Byte> list = new ArrayList<>();
        list.add(Utils.int2byte(0x60));
        list.add(Utils.int2byte(0x01));
        list.add(Utils.int2byte(addressCode ? 0x07 : 0x06));  //参数个数
        list.add(Utils.int2byte(0x01));  //协议类型
        list.add(Utils.int2byte(addressCode ? 0x03 : 0x02));
        list.add(Utils.int2byte(0x02));  //设置波特率
        int bps = 10400;
        list.add(Utils.int2byte((bps >> 24) & 0xFF));
        list.add(Utils.int2byte((bps >> 16) & 0xFF));
        list.add(Utils.int2byte((bps >> 8) & 0xFF));
        list.add(Utils.int2byte(bps & 0xFF));
        int P1 = 200;
        list.add(Utils.int2byte(0x03));  //P1
        list.add(Utils.int2byte((P1 >> 8) & 0xFF));
        list.add(Utils.int2byte(P1 & 0xFF));
        int P2 = 3000;
        list.add(Utils.int2byte(0x04));  //P2
        list.add(Utils.int2byte((P2 >> 8) & 0xFF));
        list.add(Utils.int2byte(P2 & 0xFF));
        int P3 = 55;
        list.add(Utils.int2byte(0x05));  //P3
        list.add(Utils.int2byte((P3 >> 8) & 0xFF));
        list.add(Utils.int2byte(P3 & 0xFF));
        int P4 = 5;
        list.add(Utils.int2byte(0x06));  //P4
        list.add(Utils.int2byte((P4 >> 8) & 0xFF));
        list.add(Utils.int2byte(P4 & 0xFF));
        if (addressCode) {
            list.add(Utils.int2byte(0x0C));  //5波特率
            list.add(Utils.int2byte(0x33));
        }
        byte[] data = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        byte[] recv = sendReceiveData(data, 3000);
        if (recv != null && recv.length > 4 && recv[0] == 0x60 && recv[1] == 0x0B) {
            return recv[4] == 1;
        }
        return false;
    }

    private boolean configIso() {
        List<Byte> list = new ArrayList<>();
        list.add(Utils.int2byte(0x60));
        list.add(Utils.int2byte(0x01));
        list.add(Utils.int2byte(0x07));  //参数个数
        list.add(Utils.int2byte(0x01));  //协议类型
        list.add(Utils.int2byte(0x01));
        list.add(Utils.int2byte(0x02));  //设置波特率
        int bps = 10400;
        list.add(Utils.int2byte((bps >> 24) & 0xFF));
        list.add(Utils.int2byte((bps >> 16) & 0xFF));
        list.add(Utils.int2byte((bps >> 8) & 0xFF));
        list.add(Utils.int2byte(bps & 0xFF));
        int P1 = 80;
        list.add(Utils.int2byte(0x03));  //P1
        list.add(Utils.int2byte((P1 >> 8) & 0xFF));
        list.add(Utils.int2byte(P1 & 0xFF));
        int P2 = 1000;
        list.add(Utils.int2byte(0x04));  //P2
        list.add(Utils.int2byte((P2 >> 8) & 0xFF));
        list.add(Utils.int2byte(P2 & 0xFF));
        int P3 = 55;
        list.add(Utils.int2byte(0x05));  //P3
        list.add(Utils.int2byte((P3 >> 8) & 0xFF));
        list.add(Utils.int2byte(P3 & 0xFF));
        int P4 = 5;
        list.add(Utils.int2byte(0x06));  //P4
        list.add(Utils.int2byte((P4 >> 8) & 0xFF));
        list.add(Utils.int2byte(P4 & 0xFF));
        list.add(Utils.int2byte(0x0C));  //5波特率地址
        list.add(Utils.int2byte(0x33));
        byte[] data = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        byte[] recv = sendReceiveData(data, 3000);
        if (recv != null && recv.length > 4 && recv[0] == 0x60 && recv[1] == 0x0B) {
            return recv[4] == 1;
        }
        return false;
    }

    private boolean configCan(boolean stdCan, int bps) {
        List<Byte> list = new ArrayList<>();
        list.add(Utils.int2byte(0x60));
        list.add(Utils.int2byte(0x01));
        list.add(Utils.int2byte(0x0c));  //参数个数
        list.add(Utils.int2byte(0x01));  //协议类型
        list.add(Utils.int2byte(0x00));
        list.add(Utils.int2byte(0x02));  //设置波特率
        list.add(Utils.int2byte((bps >> 24) & 0xFF));
        list.add(Utils.int2byte((bps >> 16) & 0xFF));
        list.add(Utils.int2byte((bps >> 8) & 0xFF));
        list.add(Utils.int2byte(bps & 0xFF));
        int P1 = 200;
        list.add(Utils.int2byte(0x03));  //P1
        list.add(Utils.int2byte((P1 >> 8) & 0xFF));
        list.add(Utils.int2byte(P1 & 0xFF));
        int P2 = 200;
        list.add(Utils.int2byte(0x04));  //P2
        list.add(Utils.int2byte((P2 >> 8) & 0xFF));
        list.add(Utils.int2byte(P2 & 0xFF));
        int P3 = 55;
        list.add(Utils.int2byte(0x05));  //P3
        list.add(Utils.int2byte((P3 >> 8) & 0xFF));
        list.add(Utils.int2byte(P3 & 0xFF));
        int P4 = 5;
        list.add(Utils.int2byte(0x06));  //P4
        list.add(Utils.int2byte((P4 >> 8) & 0xFF));
        list.add(Utils.int2byte(P4 & 0xFF));
        list.add(Utils.int2byte(0x10));  //CAN过滤：标准/扩展/掩码标准/掩码扩展
        list.add(Utils.int2byte(stdCan ? 0x02 : 0x03));
        list.add(Utils.int2byte(0x11));  //CAN过滤ID个数
        list.add(Utils.int2byte(0x02));
        list.add(Utils.int2byte(0x12));  //CAN过滤ID
        int canId1 = stdCan ? 0x0700 : 0x18DAF100;
        list.add(Utils.int2byte((canId1 >> 24) & 0xFF));
        list.add(Utils.int2byte((canId1 >> 16) & 0xFF));
        list.add(Utils.int2byte((canId1 >> 8) & 0xFF));
        list.add(Utils.int2byte(canId1 & 0xFF));
        int canId2 = 0xFF;
        list.add(Utils.int2byte((canId2 >> 24) & 0xFF));
        list.add(Utils.int2byte((canId2 >> 16) & 0xFF));
        list.add(Utils.int2byte((canId2 >> 8) & 0xFF));
        list.add(Utils.int2byte(canId2 & 0xFF));

        list.add(Utils.int2byte(0x13));  //PAFC
        list.add(Utils.int2byte(0x01));  //PAFC
        list.add(Utils.int2byte(0x14));  //PAFC
        list.add(Utils.int2byte(0x08));  //PAFC
        list.add(Utils.int2byte(0x15));  //PAFC
        list.add(Utils.int2byte(0x30));  //PAFC
        list.add(Utils.int2byte(0x00));  //PAFC
        list.add(Utils.int2byte(0x00));  //PAFC
        list.add(Utils.int2byte(0x00));  //PAFC
        list.add(Utils.int2byte(0x00));  //PAFC
        list.add(Utils.int2byte(0x00));  //PAFC
        list.add(Utils.int2byte(0x00));  //PAFC
        list.add(Utils.int2byte(0x00));  //PAFC
        byte[] data = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        byte[] recv = sendReceiveData(data, 3000);
        if (recv != null && recv.length > 2 && recv[0] == 0x60 && recv[1] == 0x0B) {
            return recv[4] == 1;
        }
        return false;
    }

    public String readDv(){
        {
            List<Byte> list = new ArrayList<>();
            list.add(Utils.int2byte(0x60));
            list.add(Utils.int2byte(0x02));
            list.add(Utils.int2byte(0x01));  //参数个数
            list.add(Utils.int2byte(0x80));  //参数个数
            byte[] data = new byte[list.size()];
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);
            }
            byte[] recv = sendReceiveData(data, 3000);
            return (String.format("%.2f A", Utils.byteArray2int(Arrays.copyOfRange(recv,4,6)) * 1.0 / 1000));
        }
    }


    public boolean runKeepLink(byte[] linkData, int interval) {
        List<Byte> list = new ArrayList<>();
        list.add(Utils.int2byte(0x60));
        list.add(Utils.int2byte(0x01));
        list.add(Utils.int2byte(0x05));  //参数个数

        list.add(Utils.int2byte(0x07));  //链路保持开启/关闭
        list.add(Utils.int2byte(0x01));

        list.add(Utils.int2byte(0x08));  //链路保持指令长度
        list.add(Utils.int2byte(linkData.length));
        list.add(Utils.int2byte(0x09));  //链路保持指令
        for (byte b : linkData) {
            list.add(b);
        }
        list.add(Utils.int2byte(0x0A));  //链路保持间隔
        list.add(Utils.int2byte((interval >> 8) & 0xFF));
        list.add(Utils.int2byte(interval & 0xFF));
        list.add(Utils.int2byte(0x0B));  //链路保持帧数量
        list.add(Utils.int2byte(0xff));
        list.add(Utils.int2byte(0xff));
        byte[] data = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }



        byte[] recv = sendReceiveData(data, 3500);
        if (recv != null && recv.length > 4 && recv[0] == 0x60 && recv[1] == 0x0B) {
            return recv[4] == 1;
        }
        return false;
    }

    public boolean stopKeepLink() {
        List<Byte> list = new ArrayList<>();
        list.add(Utils.int2byte(0x60));
        list.add(Utils.int2byte(0x01));
        list.add(Utils.int2byte(0x01));  //参数个数
        list.add(Utils.int2byte(0x07));  //链路保持开启/关闭
        list.add(Utils.int2byte(0x00));
        byte[] data = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        byte[] recv = sendReceiveData(data, 2000);
        if (recv != null && recv.length > 4 && recv[0] == 0x60 && recv[1] == 0x0B) {
            return recv[4] == 1;
        }
        return false;
    }

    public byte[] sendSingleReceiveSingleCommand(byte[] data, long timeout) {
        List<byte[]> dataList = sendSingleReceiveMultiCommand(data, timeout, 1);
        if (dataList.size() == 1) {
            return dataList.get(0);
        }
        return null;
    }

    public List<byte[]> sendSingleReceiveMultiCommand(byte[] data, long timeout, int expectReceiveCount) {
        List<byte[]> dataList = new ArrayList<>(1);
        dataList.add(data);
        return sendReceiveCommand(dataList, timeout, expectReceiveCount);
    }

    public List<byte[]> sendReceiveCommand(List<byte[]> dataList, long timeout, int expectReceiveCount) {
        List<byte[]> newDataList = new ArrayList<>(dataList.size());
        for (byte[] data : dataList) {
            byte[] newData = new byte[data.length + 3];
            newData[0] = 0x60;
            newData[1] = 0x09;
            newData[2] = Utils.int2byte(data.length);
            System.arraycopy(data, 0, newData, 3, data.length);
            newDataList.add(newData);
        }
        List<byte[]> ret = sendReceiveDataList(newDataList, timeout, expectReceiveCount);
        newDataList = new ArrayList<>(ret.size());
        for (byte[] data : ret) {
            if (data.length > 2 && data[0] == 0x60 && data[1] == 0x0A) {
                newDataList.add(Arrays.copyOfRange(data, 3, 3 + Utils.byte2int(data[2])));
            }
        }
        return newDataList;
    }

    public boolean enterIso() {
        if (!configIso()) return false;
        byte[] sendData = Utils.comboIsoCommand(new byte[] {0x01, 0x00});
        byte[] data = new byte[sendData.length + 3];
        data[0] = 0x60;
        data[1] = 0x06;
        data[2] = Utils.int2byte(sendData.length);
        System.arraycopy(sendData, 0, data, 3, sendData.length);
        List<byte[]> dataList = new ArrayList<>(1);
        dataList.add(data);
        dataList = sendReceiveDataList(dataList, 5000, 20);
        if (dataList.size() == 0) return false;
        mEcuIds.clear();
        for (byte[] d : dataList) {
            if (d.length > 2 && d[0] == 0x60 && d[1] == 0x0A) {
                data = Arrays.copyOfRange(d, 3, 3 + Utils.byte2int(d[2]));
                if (data.length < 6 || data[3] != 0x41) continue;
                mEcuIds.add(Utils.byteArray2int(Arrays.copyOfRange(data, 1, 3)));
            }
        }
        if (mEcuIds.isEmpty()) return false;
        runKeepLink(sendData, 600);
        return true;
    }

    public boolean enterKwp() {
        Log.d(TAG, "KWP FAST...");
        if (!configKwp(false)) return false;
        byte[] sendData = Utils.comboKwpCommand(new byte[] {0x81 - 256});
        byte[] data = new byte[sendData.length + 3];
        data[0] = 0x60;
        data[1] = 0x04;
        data[2] = Utils.int2byte(sendData.length);
        System.arraycopy(sendData, 0, data, 3, sendData.length);
        data = sendReceiveData(data, 3000);
        List<byte[]> dataList;
        if (data == null) {
            Log.d(TAG, "KWP ADDRESS...");
            if (!configKwp(true)) return false;
            sendData = Utils.comboKwpCommand(new byte[] {0x01, 0x00});
            data = new byte[sendData.length + 3];
            data[0] = 0x60;
            data[1] = 0x05;
            data[2] = Utils.int2byte(sendData.length);
            System.arraycopy(sendData, 0, data, 3, sendData.length);
            dataList = new ArrayList<>(1);
            dataList.add(data);
            dataList = sendReceiveDataList(dataList, 5000, 20);
            if (dataList.size() == 0) return false;
            mEcuIds.clear();
            for (byte[] d : dataList) {
                if (d.length > 2 && d[0] == 0x60 && d[1] == 0x0A) {
                    data = Arrays.copyOfRange(d, 3, 3 + Utils.byte2int(d[2]));
                    if (data.length < 6 || data[3] != 0x41) continue;
                    mEcuIds.add(Utils.byteArray2int(Arrays.copyOfRange(data, 1, 3)));
                }
            }
        } else {
            data = Utils.comboKwpCommand(new byte[] {0x01, 0x00});
            dataList = sendSingleReceiveMultiCommand(data, 3000, 20);
            if (dataList.size() == 0) return false;
            mEcuIds.clear();
            for (byte[] d : dataList) {
                if (d.length < 6 || d[3] != 0x41) continue;
                mEcuIds.add(Utils.byteArray2int(Arrays.copyOfRange(d, 1, 3)));
            }
        }
        if (mEcuIds.isEmpty()) return false;
        runKeepLink(Utils.comboKwpCommand(new byte[] {0x3E, 0x01}), 600);
        return true;
    }

    public boolean enterPwmVpw(boolean pwm) {
        if (!configPwmVpw(pwm)) return false;

        List<byte[]> dataList = sendSingleReceiveMultiCommand(Utils.comboPwmVpwCommand(pwm, new byte[] {0x01, 0x00}), 3000, 20);
        if (dataList.size() == 0) return false;
        mEcuIds.clear();
        for (byte[] data : dataList) {
            if (data.length < 6 || data[3] != 0x41) continue;
            mEcuIds.add(Utils.byteArray2int(Arrays.copyOfRange(data, 1, 3)));
        }
        return !mEcuIds.isEmpty();
    }

    public boolean enterCanStd(int bps) {
        if (!configCan(true, bps)) return false;
        byte[] sendData = Utils.comboCanCommand(true, new byte[] {0x01, 0x00});
        List<byte[]> dataList = sendSingleReceiveMultiCommand(sendData, 3000, 20);
        if (dataList.size() == 0) return false;
        mEcuIds.clear();
        for (byte[] data : dataList) {
            if (data.length < 6 || data[4] != 0x41) continue;
            mEcuIds.add(Utils.byteArray2int(Arrays.copyOfRange(data, 1, 3)));
        }
        if (mEcuIds.isEmpty()) return false;
        runKeepLink(sendData, 600);
        return true;
    }

    public boolean enterCanExt(int bps) {
        if (!configCan(false, bps)) return false;
        byte[] sendData = Utils.comboCanCommand(false, new byte[] {0x01, 0x00});
        List<byte[]> dataList = sendSingleReceiveMultiCommand(sendData, 3000, 20);
        if (dataList.size() == 0) return false;
        mEcuIds.clear();
        for (byte[] data : dataList) {
            if (data.length < 8 || data[6] != 0x41) continue;
            mEcuIds.add(Utils.byteArray2int(Arrays.copyOfRange(data, 1, 5)));
        }
        if (mEcuIds.isEmpty()) return false;
        runKeepLink(sendData, 600);
        return true;
    }

    public interface CommunicationInterface {
        void clear();
        void write(List<byte[]> dataArray);
    }
}
