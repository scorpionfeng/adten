package com.xtooltech.adten.common.ble

import android.content.Context
import com.xtooltech.ad10.BleCallback
import com.xtooltech.ad10.BleConnection
import com.xtooltech.ad10.Communication
import com.xtooltech.ad10.Utils
import com.xtooltech.adten.common.obd.DataArray
import com.xtooltech.adten.common.obd.DataStream
import com.xtooltech.adten.util.BLE_ADDRESS
import com.xtooltech.adten.util.ProxyPreference
import com.xtooltech.adten.util.trueLet
import com.xtooltech.base.util.printMessage
import java.util.*


const val OBD_STD_CAN=1
const val OBD_EXT_CAN=2
const val OBD_ISO=2
const val OBD_KWP=3
const val OBD_PWM=4
const val OBD_VPW=5


class BleManger :BleCallback{


    private  var communication: Communication? = null
    private lateinit var bleConnection: BleConnection
    private var deviceAddress by ProxyPreference(BLE_ADDRESS,"")

     var diagInitSuccess =false
     var linkConfig=false

    var currProto= OBD_PWM

    fun connect(context: Context,cb:BleListener ){
        deviceAddress.isEmpty().trueLet {
            cb.onBleError("蓝牙地址为空,无法连接")
        }.elseLet {
            bleConnection=BleConnection(context,true,deviceAddress)
            communication=Communication(bleConnection)
            bleConnection.addBleCallback(object:BleCallback{
                override fun onCharacteristicChanged(p0: ByteArray?) {
                    printMessage("received ${Utils.debugByteData(p0)}")
                    communication?.addByteArray(p0)
                }

                override fun onNotFoundUuid() {
                    cb.onNotFoundUuid()
                }

                override fun onConnected() {
                    cb.onConnected()
                }

                override fun onDisconnected() {
                    cb.onDisconnected()
                }

                override fun onConnectTimeout() {
                    cb.onConnectTimeout()
                }

                override fun onFoundUuid(p0: Int) {
                    cb.onFoundUuid(p0)
                }
            })

            bleConnection.start()
        }

    }

     fun enter():Boolean{
       return  communication?.enterPwmVpw(true)?:false
    }


    fun sendMultiCommandReceSingle(
        data: List<ByteArray>,
        timeout: Long
    ): ByteArray?{
        return communication?.sendReceiveCommand(data,timeout,1)?.let {
            if (it.size==1)  it[0] else null
        }
    }

    fun sendSingleReceiveSingleCommand(
        data: ByteArray?,
        timeout: Long
    ): ByteArray? {
        return sendSingleReceiveMultiCommand(data!!, timeout, 1)?.let {
            if (it.size == 1) it[0] else null
        }
    }

    fun sendSingleReceiveMultiCommand(
        data: ByteArray,
        timeout: Long,
        expectReceiveCount: Int
    ): List<ByteArray?>? {
        val dataList: MutableList<ByteArray> = ArrayList(1)
        dataList.add(data)
        return communication?.sendReceiveCommand(dataList, timeout, expectReceiveCount)
    }


    fun sendMultiCommandReceMulti(
        data: ByteArray,
        timeout: Long,
        expectReceiveCount: Int
    ): List<ByteArray?>? {
        val dataList: MutableList<ByteArray> =ArrayList(1)
        dataList.add(data)
        return communication?.sendReceiveCommand(dataList,timeout,expectReceiveCount)
    }


    fun readSpeedByCommon():String{
        var speedValue=""
        val speedCommand = comboCommand(byteArrayOf(0x01, 0x0D))
        val data = speedCommand?.let { sendSingleReceiveSingleCommand(it,3000) }
        if (data != null) {
            val dataArray=DataArray()
            data.filterIndexed { index, _ -> index > 2 }.forEach{
                dataArray.add(it.toShort())
            }

            speedValue=DataStream.commonCalcExpress("0x00,0x00,0x00,0x00,0x00,0x0D",dataArray)
        }
        return speedValue
    }


    fun readCommon(cmd:Byte):String{
        var value=""
        val command = comboCommand(byteArrayOf(0x01,cmd))
        val data = command?.let { sendSingleReceiveSingleCommand(it,3000) }
        if (data != null) {
            val dataArray=DataArray()
            data.filterIndexed { index, _ -> index > 2 }.forEach{
                dataArray.add(it.toShort())
            }
            var cmdString=Integer.toHexString(cmd.toInt())
            (cmdString.length==1).trueLet { cmdString="0"+cmdString }
            value=DataStream.commonCalcExpress("0x00,0x00,0x00,0x00,0x00,0x$cmdString",dataArray)
        }
        return value
    }


    fun readTemperByCommon():String{
        var temperValue=""
        val temperCommand = comboCommand(byteArrayOf(0x01, 0x05))
        val data = temperCommand?.let { sendSingleReceiveSingleCommand(it,3000) }
        if (data != null) {
            val dataArray=DataArray()
            data.filterIndexed { index, _ -> index > 2 }.forEach{
                dataArray.add(it.toShort())
            }

            temperValue=DataStream.commonCalcExpress("0x00,0x00,0x00,0x00,0x00,0x05",dataArray)
        }
        return temperValue
    }



    fun readSpeed():String{
        var speedValue=""
        val speedCommand = comboCommand(byteArrayOf(0x01, 0x0D))
        val data = speedCommand?.let { sendSingleReceiveSingleCommand(it,3000) }
        if (data != null) {
            if (currProto == OBD_STD_CAN && data.size >= 7 && data[4] == 0x41.toByte() && data[5] == 0x0D.toByte()) {
                speedValue = Utils.byte2int(data.get(6)).toString()
            } else if (currProto == OBD_EXT_CAN && data.size >= 9 && data.get(6) == 0x41.toByte() && data.get(7) == 0x0D.toByte() ) {
                speedValue = Utils.byte2int(data.get(8)).toString()
            } else if (data.size >= 6 && data.get(3) == 0x41.toByte() && data.get(4) == 0x0D.toByte()) {
                speedValue = Utils.byte2int(data.get(5)).toString()
            }
        }
        return speedValue
    }
    fun readRpm():String{
        var rmpValue=""
        val rpmCommand = comboCommand(byteArrayOf(0x01, 0x0C))
        val data = rpmCommand?.let { sendSingleReceiveSingleCommand(it,3000) }
        if (data != null) {
            if (currProto == OBD_STD_CAN && data.size >= 8 && data[4] == 0x41.toByte() && data[5] == 0x0C.toByte()) {
                rmpValue = Utils.byteArray2int(Arrays.copyOfRange(data, 6, 8)).toString()
            } else if (currProto == OBD_EXT_CAN && data.size >= 10 && data[6] == 0x41.toByte() && data[7] == 0x0C.toByte()) {
                rmpValue = Utils.byteArray2int(Arrays.copyOfRange(data, 8, 10)).toString();
            } else if (data.size >= 7 && data[3] == 0x41.toByte() && data[4] == 0x0C.toByte()) {
                rmpValue = Utils.byteArray2int(Arrays.copyOfRange(data, 5, 7)).toString();
            }
        }
        return rmpValue
    }



    fun readTemper():String{
        var temperValue=""
        val temperCommand = comboCommand(byteArrayOf(0x01, 0x05))
        val data = temperCommand?.let { sendSingleReceiveSingleCommand(it,3000) }
        if (data != null) {
            if (currProto == OBD_STD_CAN && data.size >= 7 && data[4] == 0x41.toByte() && data[5] == 0x05.toByte()
            ) {
                temperValue = (Utils.byte2int(data[6])-40).toString()
            } else if (currProto == OBD_EXT_CAN && data.size >= 9 && data[6] == 0x41.toByte() && data[7] == 0x05.toByte()
            ) {
                temperValue = (Utils.byte2int(data[8])-40).toString()
            } else if (data.size >= 6 && data[3] == 0x41.toByte() && data[4] == 0x05.toByte()) {
                temperValue = (Utils.byte2int(data[5])-40).toString()
            }
        }
        return temperValue
    }


    companion object{
        private  var instance: BleManger?=null
            get() {
                if (field == null) {
                    field = BleManger()
                }
                return field
            }

        fun getIns(): BleManger {
            return instance!!
        }
    }

    override fun onCharacteristicChanged(p0: ByteArray?) {
    }

    override fun onNotFoundUuid() {
    }

    override fun onConnected() {
    }

    override fun onDisconnected() {
    }

    override fun onConnectTimeout() {
    }

    override fun onFoundUuid(p0: Int) {
    }

    fun comboCommand(obdData: ByteArray): ByteArray? {

        when(currProto){
            OBD_STD_CAN -> return  Utils.comboCanCommand(true,obdData)
            OBD_EXT_CAN->return Utils.comboCanCommand(false, obdData)
            OBD_ISO->return Utils.comboIsoCommand(obdData)
            OBD_KWP->return Utils.comboKwpCommand(obdData)
            OBD_PWM->return Utils.comboPwmVpwCommand(true, obdData)
            OBD_VPW->return Utils.comboPwmVpwCommand(false, obdData)
        }
        return null
    }
}