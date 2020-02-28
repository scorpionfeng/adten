package com.xtooltech.adten.common.ble

import android.content.Context
import com.xtooltech.ad10.BleCallback
import com.xtooltech.ad10.BleConnection
import com.xtooltech.ad10.Communication
import com.xtooltech.ad10.Utils
import com.xtooltech.adten.util.BLE_ADDRESS
import com.xtooltech.adten.util.ProxyPreference
import com.xtooltech.adten.util.trueLet
import com.xtooltech.base.util.printMessage
import kotlinx.coroutines.CoroutineScope
import kotlin.properties.Delegates

class BleManger :BleCallback{


    private  var communication: Communication? = null
    private lateinit var bleConnection: BleConnection
    private var deviceAddress by ProxyPreference(BLE_ADDRESS,"")

     var diagInitSuccess =false
     var linkConfig=false


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

    fun append(byteArr:ByteArray?){
        communication?.addByteArray(byteArr)
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
}