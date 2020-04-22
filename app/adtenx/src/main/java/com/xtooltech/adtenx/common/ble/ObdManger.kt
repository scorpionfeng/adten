package com.xtooltech.adtenx.common.ble

import android.content.Context
import android.util.Log
import calculation
import com.xtooltech.adten.common.ble.BleListener
import com.xtooltech.adten.util.ds
import com.xtooltech.adten.util.trueLet
import com.xtooltech.adtenx.common.BoxInfo
import com.xtooltech.adtenx.common.destructu.*
import com.xtooltech.adtenx.common.obd.DataArray
import com.xtooltech.adtenx.common.obd.DataStream
import com.xtooltech.adtenx.plus.BleCallback
import com.xtooltech.adtenx.plus.BleConnection
import com.xtooltech.adtenx.plus.Communication
import com.xtooltech.adtenx.plus.Utils
import com.xtooltech.adtenx.util.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.lang.Exception
import java.util.*
import kotlin.experimental.and


const val OBD_STD_CAN = 1
const val OBD_EXT_CAN = 2
const val OBD_ISO = 3
const val OBD_KWP = 4
const val OBD_PWM = 5
const val OBD_VPW = 6
const val OBD_UNKNOWN = 7


enum class InitState{
    UPDATE,RESUME,TIMEOUT
}

val nameMap = mapOf(
    OBD_STD_CAN to "标准CAN",
    OBD_EXT_CAN to "扩展CAN",
    OBD_ISO to "ISO",
    OBD_KWP to "KWP",
    OBD_VPW to "VPW",
    OBD_PWM to "PWM",
    OBD_UNKNOWN to "未知"
)


class ObdManger : BleCallback {


    /** 设备名 */
    var deviceName: String=""
    /** 通讯层 */
    private var communication: Communication? = null
    /** 蓝牙连接 */
    private lateinit var bleConnection: BleConnection
    /** 地址 */
    var deviceAddress = ""

    /** 解构类 */
    var destructor:DestructBiz=DestructUnknow()

    var currProto = OBD_UNKNOWN
    fun connect(context: Context, cb: BleListener) {
        deviceAddress.isEmpty().trueLet {
            cb.onBleError("蓝牙地址为空,无法连接")
        }.elseLet {
            bleConnection = BleConnection(context, true, deviceAddress)
            communication = Communication(bleConnection)
            bleConnection.addBleCallback(object : BleCallback {
                override fun onCharacteristicChanged(p0: ByteArray?) {
                    communication?.addByteArray(p0)
                }

                override fun onNotFoundUuid() {
                    cb.onNotFoundUuid()
                }

                override fun onConnected() {
                    deviceName="AD10"
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


    /**
     * 发单帧收单帧
     */
    private fun sendSingleReceiveSingleCommand(
        data: ByteArray?,
        timeout: Long
    ): ByteArray? {
        return sendSingleReceiveMultiCommand(data!!, timeout, 1)?.let {
            if (it.size == 1) it[0] else null
        }
    }

    /**
     * 发单帧收多帧
     */
    private fun sendSingleReceiveMultiCommand(
        data: ByteArray,
        timeout: Long,
        expectReceiveCount: Int
    ): List<ByteArray?>? {
        val dataList: MutableList<ByteArray> = ArrayList(1)
        dataList.add(data)
        return communication?.sendReceiveCommand(dataList, timeout, expectReceiveCount)
    }


    /**
     * 发多收多
     */
     fun sendMultiCommandReceMulti(
        data: ByteArray,
        timeout: Long,
        expectReceiveCount: Int
    ): List<ByteArray?>? {
        val dataList: MutableList<ByteArray> = ArrayList(1)
        dataList.add(data)
        return communication?.sendReceiveCommand(dataList, timeout, expectReceiveCount)
    }




    fun readCommon(cmd: Byte): String {
        var value = ""
        val command = comboCommand(byteArrayOf(0x01, cmd))
        val data = command?.let { sendSingleReceiveSingleCommand(it, 3000) }
        if (data != null) {
            val dataArray = DataArray()
            data.filterIndexed { index, _ -> index > 2 }.forEach {
                dataArray.add(it.toShort())
            }
            var cmdString = Integer.toHexString(cmd.toInt())
            (cmdString.length == 1).trueLet { cmdString = "0" + cmdString }
            value = DataStream.commonCalcExpress("0x00,0x00,0x00,0x00,0x00,0x$cmdString", dataArray)
        }
        return value
    }


    fun readVin(): String {
        var value = ""
        val command = comboCommand(byteArrayOf(0x09, 0x02))
            val data = command?.let { sendSingleReceiveMultiCommand(it, 3000, 10) }
            data?.apply {
                value=destructor.parseVin(data)
            }
        Log.i("Communication","vin= "+value+" and size="+value.length)
        return value
    }


    /**
     * 读取通用数据
     */
    fun readCommonRaw(cmd: Byte): List<Byte> {
        var list = listOf<Byte>()
        val command = comboCommand(byteArrayOf(0x01, cmd))
        val data = command?.let { sendSingleReceiveSingleCommand(it, 3000) }
        if (data != null) {
            list = parse2BizSingle(data)
        }
        return list
    }




    fun readTemper(): String {
        var temperValue = ""
        val temperCommand = comboCommand(byteArrayOf(0x01, 0x05))
        val data = temperCommand?.let { sendSingleReceiveSingleCommand(it, 3000) }
        if (data != null) {
            if (currProto == OBD_STD_CAN && data.size >= 7 && data[4] == 0x41.toByte() && data[5] == 0x05.toByte()
            ) {
                temperValue = (Utils.byte2int(data[6]) - 40).toString()
            } else if (currProto == OBD_EXT_CAN && data.size >= 9 && data[6] == 0x41.toByte() && data[7] == 0x05.toByte()
            ) {
                temperValue = (Utils.byte2int(data[8]) - 40).toString()
            } else if (data.size >= 6 && data[3] == 0x41.toByte() && data[4] == 0x05.toByte()) {
                temperValue = (Utils.byte2int(data[5]) - 40).toString()
            }
        }
        return temperValue
    }


    companion object {
        private var instance: ObdManger? = null
            get() {
                if (field == null) {
                    field = ObdManger()
                }
                return field
            }

        fun getIns(): ObdManger {
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

    /**
     * 组装命令
     */
    fun comboCommand(obdData: ByteArray): ByteArray? {

        when (currProto) {
            OBD_STD_CAN -> return Utils.comboCanCommand(true, obdData)
            OBD_EXT_CAN -> return Utils.comboCanCommand(false, obdData)
            OBD_ISO -> return Utils.comboIsoCommand(obdData)
            OBD_KWP -> return Utils.comboKwpCommand(obdData)
            OBD_PWM -> return Utils.comboPwmVpwCommand(true, obdData)
            OBD_VPW -> return Utils.comboPwmVpwCommand(false, obdData)
        }
        return null
    }

    /**
     * 扫描
     */
    fun scan(): Int {
        return if (scanSystem()) currProto else OBD_UNKNOWN
    }

    /**
     * 扫描操作
     */
    private fun scanSystem(): Boolean {
        var ret: Boolean = enterCan()
        if (!ret) {
            currProto = OBD_ISO
            Log.i("Communication", "try iso enter....")
            ret = communication?.enterIso() ?: false
        }
        if (!ret) {
            currProto = OBD_KWP
            Log.i("Communication", "try kwp enter....")
            ret = communication?.enterKwp() ?: false
        }
        if (!ret) {
            currProto = OBD_PWM
            Log.i("Communication", "try pwm enter....")
            ret = communication?.enterPwmVpw(true) ?: false
        }
        if (!ret) {
            currProto = OBD_VPW
            Log.i("Communication", "try vpw enter....")
            ret = communication?.enterPwmVpw(false) ?: false
        }
        if (!ret) currProto = OBD_UNKNOWN

        takeIf { currProto!= OBD_UNKNOWN }?.apply {
            destructor=selectDestroctor(currProto)
        }

        return ret
    }

    /**
     * 匹配解构器
     */
    private fun selectDestroctor(currProto: Int): DestructBiz {
        return when (currProto) {
            OBD_STD_CAN -> DestructCanStd()
            OBD_EXT_CAN -> DestructCanExt()
            OBD_ISO     -> DestructIso()
            OBD_KWP     -> DestructKwp()
            OBD_PWM     -> DestructPwm()
            OBD_VPW     -> DestructVpw()
            else -> DestructUnknow()
        }
    }


    private fun enterCan(): Boolean {
        currProto = OBD_STD_CAN
        Log.i("Communication", "try can std 500000 enter....")
        var ret: Boolean = communication?.enterCanStd(500000) ?: false
        if (!ret) {
            currProto = OBD_EXT_CAN
            Log.i("Communication", "try can ext 500000 enter....")
            ret = communication?.enterCanExt(500000) ?: false
        }
        if (!ret) {
            currProto = OBD_STD_CAN
            Log.i("Communication", "try can std 250000 enter....")
            ret = communication?.enterCanStd(250000) ?: false
        }
        if (!ret) {
            currProto = OBD_EXT_CAN
            Log.i("Communication", "try can ext 250000 enter....")
            ret = communication?.enterCanExt(250000) ?: false
        }
        if (!ret) currProto = OBD_UNKNOWN
        return ret
    }


    fun computerOffset(): Int {
        return when (currProto) {
            OBD_STD_CAN -> 4
            OBD_EXT_CAN -> 6
            OBD_ISO -> 3
            OBD_KWP -> 3
            OBD_PWM -> 3
            OBD_VPW -> 3
            else -> 0

        }
    }

    fun readTrobleCodeAmount(cmd: Byte): Pair<Int,List<String>> {
        var amount=0
        var codeLists= listOf<String>()
        val command = comboCommand(byteArrayOf(cmd))
                val data = command?.let { sendSingleReceiveMultiCommand(it, 3000, 10) }

                data?.apply {
                    var trobleCode = destructor.parseTrobCode(cmd, data)
                    amount=trobleCode.first
                    codeLists=trobleCode.second
                }
        Log.i("Communication","读取故障码："+ amount +" codeLists= "+codeLists.toString())
        return Pair(amount,codeLists.toList())
    }

    fun clearCode(): Boolean {
        var flag = false
        val command = comboCommand(byteArrayOf(0x04))

        if (currProto == OBD_STD_CAN) {
            val data = command?.let { sendSingleReceiveSingleCommand(it, 3000) }
            //08 07 E8 06 43 02 39 39 33 34 00
            data?.apply {
                var biz = parse2BizSingle(data)
                flag = biz.first() != 0x7f.toByte()
            }
        } else if (currProto == OBD_VPW) {
            val data = command?.let { sendSingleReceiveSingleCommand(it, 3000) }
            data?.apply {
                var biz = parse2BizSingle(data)
                flag = biz.first() != 0x7f.toByte()
            }

        } else {
            val data = command?.let { sendSingleReceiveSingleCommand(it, 3000) }
            data?.apply {
                var biz = parse2BizSingle(data)
                flag = biz.first() != 0x7f.toByte()

            }
        }
        return flag
    }

    /**
     * 查询 冻结帧状态
     */
    fun queryMilState(): Pair<Boolean, List<Byte>> {
        var milState = false
        var value = readCommonRaw(0x01)

        value.isNotEmpty().trueLet {
            var flag = value[2]
            var statue = flag.toInt().shr(7)
            if (statue == 1) milState = true
        }
        return Pair(milState, value)
    }

    /**
     * 读取冻结帧项
     */
    fun readFreezeItem(item: ObdItem): String {
        var value = ""
        val command = comboCommand(byteArrayOf(0x02, item.kind, 0x00))
        val data = command?.let { sendSingleReceiveSingleCommand(it, 3000) }
        if (data != null) {
            item.obd = parse2BizSingle(data) // 42 02 00 00 00
            calc[item.index]?.apply {
                value = calculation(item)
            }
        }
        return value
    }

    /**
     * 读取数据流项
     */
    fun readFlowItem(item: ObdItem): String {
        var value = ""
        val command = comboCommand(byteArrayOf(0x01, item.kind))
        val data = command?.let { sendSingleReceiveSingleCommand(it, 3000) }
        if (data != null) {
            item.obd = parse2BizSingle(data)
            calc[item.index]?.apply {
                value = calculation(item)
            }

        }
        item.content=value
        Log.i("Communication",item.toString())
        return value
    }

    /**
     * 获取 电压
     */
    fun readDv(): String {
        return communication?.readDv() ?: "电压读取不到"
    }

    fun readBoxInfo(): BoxInfo {
        return communication?.readBinVersion() ?:BoxInfo("","","","")
    }

    /** 读油耗 */
    fun fuelCons(): String {
        var value = ""
        var fuelAdj=1.0
        var bizWind = listOf<Byte>()
        val command = comboCommand(byteArrayOf(0x01, 0x10))
        val data = command?.let { sendSingleReceiveSingleCommand(it, 3000) }
        if (data != null) {
            /** 获取到 空气流量 */
            bizWind = parse2BizSingle(data)
            //"%3.2f"	"(BYTE[2]*256+BYTE[3])/100.0;"
            var airFlow = 1
            try {
                airFlow = (String.format("%3.2f", (bizWind[2] * 256 + bizWind[3]) / 100.0)).toInt()
            } catch (e: ArrayIndexOutOfBoundsException) {
            } catch (e2: NumberFormatException) {
            }
            value = calculationWithAirFlow(airFlow, 0, 1)
        } else {
            var displace=2.0 /** 排量 */
            val airPress = readFlowItem(ObdItem(0x0b,"进气压力",false,"","pa","", listOf()))
            val airTemp = readFlowItem(ObdItem(0x0f,"空气温度",false,"","c","", listOf()))
            val engineRpm = readFlowItem(ObdItem(0x0c,"转速",false,"","","", listOf()))

            var valueAirPress=airPress.toFloat()
            var valueAirTemp:Float=airTemp.toFloat()
            var valueRpm:Float=engineRpm.toFloat()
            value= (fuelAdj*8.513/1000*valueRpm*valueAirPress/(valueAirTemp+273.15)*displace*0.85).toString()
        }
        return value
    }

    /**
     * 获取数据流所有PID
     */
    private fun supportFlowPids(): MutableList<ByteArray?> {
        var query = true
        var size = 2
        var pid1 = 0x00.toByte()
        var datas: MutableList<ByteArray?> = mutableListOf()
        while (query) {
            val obdData = ByteArray(size)
            obdData[0] = 0x01
            obdData[1] = pid1

            var comboCommand = getIns().comboCommand(obdData)
            var data = comboCommand?.let { getIns().sendSingleReceiveSingleCommand(it, 5000) }
            data?.apply {
                if (data.isEmpty()) {
                    query = false
                } else {
                    data.apply {
                        datas.add(this)
                        val bizData = parse2BizSingle(this)
                        if ((bizData[0] == 0x41.toByte()) and (bizData[1] == pid1)) {
                            if (bizData.last().and(0x01) == 0x01.toByte()) {
                                pid1 = (pid1 + 0x20).toByte()
                            } else {
                                query = false
                            }
                        } else {
                            query = false
                        }
                    }

                }
            } ?:apply { query=false }
        }
        return datas
    }

    /** 查询数据流支持项 */
    fun queryFlowListItem(): List<ObdItem> {
        val maskBuffer = ShortArray(32)
        val obdList = mutableListOf<ObdItem>()
        var freezeKeyList = listOf<Short>()
        val pids: MutableList<ByteArray?> = supportFlowPids()

        takeIf { pids.isNotEmpty() }?.apply {
            mergePid(pids, maskBuffer, computerOffset())
            val produPid = produPid(maskBuffer)
            freezeKeyList = dataFlow4KeyList(produPid, 0x01)
            takeIf { freezeKeyList.isNotEmpty() }?.apply {
                freezeKeyList.forEach {
                    val element = ds[it.toObdIndex()]
                    element?.apply {
                        obdList.add(ObdItem(it.toObdPid(), element.first, false, "", element.second, it.toObdIndex()))
                    } ?:apply {
                        Log.i("Communication", "error")
                    }
                }
            }
        }
        return obdList.toList()
    }

    /**
     * 冻结帧支持项查询
     */
    fun supportFreeze(): MutableList<ByteArray?> {
        var query = true
        var pid1 = 0x00.toByte()
        var datas: MutableList<ByteArray?> = mutableListOf()
        while (query) {
            val obdData = ByteArray(3)
            obdData[0] = 0x02
            obdData[1] = pid1
            obdData[2] = 0x00

            var comboCommand = getIns().comboCommand(obdData)
            var data = comboCommand?.let { getIns().sendMultiCommandReceMulti(it, 5000, 10) }
            Log.i("Communication", "查询支持项= " + comboCommand?.toHex())
            data?.apply {

                if (data.isEmpty()) {
                    query = false
                } else {
                    data[0]?.apply {
                        datas.add(this)
                        var bizData = parse2BizSingle(this)
                        bizData.isNotEmpty().trueLet {
                            if (bizData[0] == 0x42.toByte() && bizData[1] == pid1 ) {
                                if (bizData.last().and(0x01) == 0x01.toByte()) {
                                    pid1 = (pid1 + 0x20).toByte()
                                } else {
                                    query = false
                                }
                            }else{
                                query = false
                            }

                        }.elseLet {
                            query = false
                        }
                    }

                }
            }
        }
        return datas
    }

    /** 查询 冻结帧列表 */
    fun queryFreezeList(): List<ObdItem> {
        var obditms = mutableListOf<ObdItem>( )
        var maskBuffer = ShortArray(32)
        var freezeKeyList = listOf<Short>()
        var datas: MutableList<ByteArray?> = supportFreeze()
        (datas.size > 0).trueLet {
            mergeFreezePid(datas, maskBuffer, computerOffset())
            var pid = produFreezePid(maskBuffer)
            freezeKeyList = dataFlow4KeyList(pid, 0x02)
        }

        freezeKeyList.isNotEmpty().trueLet {
            freezeKeyList.forEach {
                    var element = ds[it.toObdIndex()]
                    element?.apply {
                        obditms.add(ObdItem(it.toObdPid(), element.first, false, "", element.second, it.toObdIndex()))
                    }
                }
            }


        return obditms.toList()
    }

    /**
     * 初始化固件升级
     */
    fun initFirmwareUpdate(file: File): InitState {
        return communication?.initFirmwareUpdate(file) ?:InitState.RESUME
    }

    /**
     * 升级固件
     */
    fun updateOneFrameFirmware(buffer: ByteArray, i: Int, len: Int): Boolean {
        return communication?.updateOneFrameFirmware(buffer,i,len) ?:false
    }



    fun burnBin(file:File,callback : OnBurnCallBack) {
        var `in`: FileInputStream? = null
        val buffer = ByteArray(252)
        val total: Long = if (file.length() % 252 == 0L) file.length() / 252 else file.length() / 252 + 1
        var len: Int
        var i: Int
        var count = 0
        var success = false
        try {
            `in` = FileInputStream(file)
            while (`in`.read(buffer, 0, 252).also { len = it } != -1) {
                val strProgress =
                    String.format(Locale.getDefault(), "%.2f%%", count * 1.0 / total)
                callback.progress(count * 1.0 / total)
                success = ObdManger.getIns().updateOneFrameFirmware(buffer, 0, len)
                if (!success) break
                count++
            }
        } catch (e: Exception) {
            e.printStackTrace()
            success = false
        } finally {
            try {
                `in`?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            file.delete()
        }
        callback.isSuccess(success)
    }

    fun readBinFileVersion(absolutePath: String): String {
        var raf = RandomAccessFile(absolutePath, "rw");
        var data=ByteArray(19)
        for (i in 348L .. 366L){
            raf.seek(i)
            var readByte = raf.readByte()
            data[i.toInt()-348]=readByte
        }
        return String(data)
    }

    fun reset() {
        communication?.controlBox(3)
    }


    interface OnBurnCallBack {

    fun progress(progress:Double)

    fun isSuccess(isSucced:Boolean)
}


}