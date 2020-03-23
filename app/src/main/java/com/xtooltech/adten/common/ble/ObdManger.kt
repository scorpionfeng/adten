package com.xtooltech.adten.common.ble

import android.content.Context
import calculation
import com.xtooltech.ad10.BleCallback
import com.xtooltech.ad10.BleConnection
import com.xtooltech.ad10.Communication
import com.xtooltech.ad10.Utils
import com.xtooltech.adten.common.obd.DataArray
import com.xtooltech.adten.common.obd.DataStream
import com.xtooltech.adten.module.diy.ObdItem
import com.xtooltech.adten.util.*
import com.xtooltech.base.util.printMessage
import java.util.*
import kotlin.experimental.and


const val OBD_STD_CAN=1
const val OBD_EXT_CAN=2
const val OBD_ISO=3
const val OBD_KWP=4
const val OBD_PWM=5
const val OBD_VPW=6
const val OBD_UNKNOWN=7



val nameMap= mapOf(
    OBD_STD_CAN to "标准CAN",
    OBD_EXT_CAN to "扩展CAN",
    OBD_ISO to "ISO",
    OBD_KWP to "KWP",
    OBD_VPW to "VPW",
    OBD_PWM to "PWM",
    OBD_UNKNOWN to "未知"
)


class ObdManger :BleCallback{


    private  var communication: Communication? = null
    private lateinit var bleConnection: BleConnection
    private var deviceAddress by ProxyPreference(BLE_ADDRESS,"")

     var diagInitSuccess =false
     var linkConfig=false

    var currProto= OBD_STD_CAN
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

     fun enter(): Boolean? {

         return when(currProto){
             OBD_STD_CAN -> communication?.enterCanStd(500000)
             OBD_EXT_CAN-> communication?.enterCanExt(500000)
             OBD_ISO-> communication?.enterIso()
             OBD_KWP-> communication?.enterKwp()
             OBD_PWM-> communication?.enterPwmVpw(true)
             OBD_VPW-> communication?.enterPwmVpw(false)
             else-> false
         }

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


    fun readVin():String{
        var value=""
        val command = comboCommand(byteArrayOf(0x09,0x02))
        var vinHexList:MutableList<Byte> = mutableListOf()
        if(currProto == OBD_STD_CAN ){
            val data = command?.let { sendSingleReceiveMultiCommand(it,3000,10) }
            data?.forEach {
                it?.slice(4 until it.size)?.let { it2->
                    vinHexList.addAll(it2)
                    printMessage("data= >"+it2.toHex())
                }
            }
            vinHexList.isNotEmpty().trueLet {
                vinHexList.forEach{
                    value+=String.format("%c",it)
                }
            }

        }else{
            val data = command?.let { sendSingleReceiveSingleCommand(it,3000) }
            data?.apply {
                var bizData = parse2BizSingle(data)             //41 6B 10 49 02 01 FF FF FF FF 55
                bizData.forEach{value+=String.format("%c",it)}           //49 02 01 FF FF FF FF
                printMessage("data = "+data?.toHex() ?: "N/A")
                printMessage("bizdata = "+bizData.toHex())
                printMessage("value = "+value)
            }

        }

        return value
    }



    fun comboCanCommandStream(stdCan: Boolean, data: ByteArray,ecuid:Byte=-33 ): ByteArray? {
        val length = if (data.size > 7) 7 else data.size
        val ret: ByteArray
        if (stdCan) {
            ret = ByteArray(11)
            ret[0] = 8
            ret[1] = 7
            ret[2] = ecuid
            System.arraycopy(data, 0, ret, 3, length)
        } else {
            ret = ByteArray(13)
            ret[0] = -120
            ret[1] = 24
            ret[2] = -37
            ret[3] = 51 // 替换成 ecuid
            ret[4] = -15
            ret[5] = Utils.int2byte(length)
            System.arraycopy(data, 0, ret, 6, length)
        }
        return ret
    }




    fun readCommonRaw(cmd:Byte):List<Byte>?{
        val command = comboCommand(byteArrayOf(0x01,cmd))
        val data = command?.let { sendSingleReceiveSingleCommand(it,3000) }
        if (data != null) {
            val dataArray=DataArray() //48 6B 10 41 01 00 07 E1 E1 A3
            return  parse2BizSingle(data)
        }
        return null
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
        private  var instance: ObdManger?=null
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

    fun scan():Int {
      return   if (scanSystem()) currProto else OBD_UNKNOWN
    }


    private fun scanSystem(): Boolean {
       printMessage("scanning CAN...")
        var ret: Boolean =  enterCan()?:false
        if (!ret) {
           printMessage("scanning ISO...")
            currProto = OBD_ISO
            ret = communication?.enterIso() ?: false
        }
        if (!ret) {
           printMessage("scanning KWP...")
            currProto = OBD_KWP
            ret = communication?.enterKwp()?:false
        }
        if (!ret) {
           printMessage("scanning PWM...")
            currProto = OBD_PWM
            ret = communication?.enterPwmVpw(true)?:false
        }
        if (!ret) {
           printMessage("scanning VPW...")
            currProto = OBD_VPW
            ret = communication?.enterPwmVpw(false)?:false
        }
        if (!ret) currProto = OBD_UNKNOWN
        return ret
    }


    private fun enterCan(): Boolean {
        printMessage("STD CAN 500K...")
        currProto = OBD_STD_CAN
        var ret: Boolean = communication?.enterCanStd(500000)?:false
        if (!ret) {
            printMessage("EXT CAN 500K...")
            currProto = OBD_EXT_CAN
            ret = communication?.enterCanExt(500000)?:false
        }
        if (!ret) {
            printMessage("STD CAN 250K...")
            currProto = OBD_STD_CAN
            ret = communication?.enterCanStd(250000)?:false
        }
        if (!ret) {
            printMessage("EXT CAN 250K...")
            currProto = OBD_EXT_CAN
            ret = communication?.enterCanExt(250000)?:false
        }
        if (!ret) currProto = OBD_UNKNOWN
        return ret
    }


    fun fetObdData(bytes: ByteArray?):ByteArray? {
        when(currProto){
            OBD_STD_CAN -> return  parseData_std_can(bytes)
        }
        return null
    }

    private fun parseData_std_can(bytes: ByteArray?):ByteArray? {
        return null
    }

    fun computerOffset(): Int {
        when(currProto){
            OBD_STD_CAN -> return  4
            OBD_EXT_CAN->return 6
            OBD_ISO->return 3
            OBD_KWP->return 4
            OBD_PWM->return 3
            OBD_VPW->return 3
            else->return 0

        }
    }

    fun readTrobleCodeAmount(cmd:Byte): Int {
        var value=0
        val command = comboCommand(byteArrayOf(cmd))
        var vinHexList:MutableList<Byte> = mutableListOf()
        if(currProto == OBD_STD_CAN ){
            val data = command?.let { sendSingleReceiveMultiCommand(it,3000,10) }
            //08 07 E8 06 43 02 39 39 33 34 00
            data?.forEach {
                it?.slice(4 until it.size)?.let { it2->
                    vinHexList.addAll(it2)
                    printMessage("data= >"+it2.toHex())
                }
            }
            vinHexList.isNotEmpty().trueLet {
                value= vinHexList.first().toInt()
            }
        }else{
            val data = command?.let { sendSingleReceiveSingleCommand(it,3000) }
            data?.apply {//41 6B 10 47 01 93 12 33 00 00 0C
                var biz = parse2BizSingle(data)//47 01 93 12 33 00 00
               value= biz[2].toInt()
            }
        }
            return value
    }
    fun clearCode(): Boolean {
        var flag=false
        val command = comboCommand(byteArrayOf(0x04))

        if(currProto == OBD_STD_CAN ){
            val data = command?.let { sendSingleReceiveSingleCommand(it,3000) }
            //08 07 E8 06 43 02 39 39 33 34 00
            data?.apply {
                var biz = parse2BizSingle(data)
             flag=   biz.first()!=0x7f.toByte()
            }
        }else if (currProto== OBD_VPW){
            val data = command?.let { sendSingleReceiveSingleCommand(it,3000) }
            data?.apply {
                var biz = parse2BizSingle(data)
                flag = biz.first()!=0x7f.toByte()
            }

        }else{
            val data = command?.let { sendSingleReceiveSingleCommand(it,3000) }
            data?.apply {
                var biz = parse2BizSingle(data)
                flag = biz.first()!=0x7f.toByte()

            }
        }
            return flag
    }

    fun queryMilState():Pair<Boolean,Byte> {
        var milState=false
        var readyStatus=0x00.toByte()
        var value =readCommonRaw(0x01)
        value?.apply {
            var flag = value[2]
            var statue = flag.toInt().shr(7)
            if (statue==1) milState=true
            readyStatus= value[3]
        }
        return Pair(milState,readyStatus)
    }

    fun readFreezeItem(item:ObdItem) :String{
        var value=""
        val command = comboCommand(byteArrayOf(0x02,item.kind,0x00))
        val data = command?.let { sendSingleReceiveSingleCommand(it,3000) }
        if (data != null) {
            item.obd = parse2BizSingle(data) // 42 02 00 00 00
            calc.get(item.index)?.apply {
                value= calculation(item)
                printMessage("value = $value")
            }
        }
        return value
    }

    fun readFlowItem(item:ObdItem) :String{
        var value=""
        val command = comboCommand(byteArrayOf(0x01,item.kind))
        val data = command?.let { sendSingleReceiveSingleCommand(it,3000) }
        if (data != null) {
            item.obd= parse2BizSingle(data)
            calc[item.index]?.apply {
                value= calculation(item)
                printMessage("value = $value")
            }

        }
        return value
    }

    fun readDv(): String {
        return communication?.readDv()?:"电压读取不到"
    }

    /** 读油耗 */
    fun fuelCons(): String {
        var value=""
        var bizWind= listOf<Byte>()
        val command = comboCommand(byteArrayOf(0x01,0x10))
        val data = command?.let { sendSingleReceiveSingleCommand(it,3000) }
        if (data != null) {
            /** 获取到 空气流量 */
            bizWind= parse2BizSingle(data)
            printMessage("bizWind= "+bizWind.toHex())
            //"%3.2f"	"(BYTE[2]*256+BYTE[3])/100.0;"
            var airFlow = 1
            try {
                airFlow= (String.format("%3.2f", (bizWind[2] * 256 + bizWind[3]) / 100.0)).toInt()
            }catch (e:ArrayIndexOutOfBoundsException){
                printMessage("数组越界")
            }catch(e2:NumberFormatException){
                printMessage("格式错误")
            }
            value = calculationWithAirFlow(airFlow,0,1)
            printMessage("读油耗 ->空气流量 = "+value)
        }else{
            val press = comboCommand(byteArrayOf(0x01,0x12))
            val preData = press?.let { sendSingleReceiveSingleCommand(it,3000) }
            preData?.apply {
                var bizPress = parse2BizSingle(preData)
                printMessage("press data= "+bizPress.toHex())
            }
        }
        return value
    }

    fun supportFlowPids(): MutableList<ByteArray?> {
        var query = true
        var size = 2
        var pid1 = 0x00.toByte()
        var datas: MutableList<ByteArray?> = mutableListOf()
        while (query) {
            val obdData = ByteArray(size)
            obdData[0] = 0x01
            obdData[1] = pid1

            var comboCommand = ObdManger.getIns().comboCommand(obdData)
            var data =
                comboCommand?.let { ObdManger.getIns().sendMultiCommandReceMulti(it, 5000, 10) }
            data?.apply {

                when(ObdManger.getIns().currProto){
                    OBD_STD_CAN->{

                        if(data.isEmpty()){
                            query=false
                        }else{
                            data[0]?.apply {
                                datas.add(this)
                                var bizData = parse2BizSingle(this)
                                if(bizData.get(0) == 0x41.toByte()){
                                    if (bizData.last().and(0x01) == 0x01.toByte()) {
                                        pid1 = (pid1 + 0x20).toByte()
                                    }else{
                                        query=false
                                    }
                                }
                            }

                        }
                    }
                    OBD_PWM->{
                        if (data.isEmpty()) {
                            query = false
                        } else {
                            var item = data[0]
                            datas.add(item)
                            item?.let {
                                //41 00  BE 5F B8 11
                                var bizData = parse2BizSingle(item)
                                if (bizData.get(0)==0x41.toByte()) {
                                    if (bizData.last().and(0x01)==0x01.toByte()) {
                                        pid1 = (pid1 + 0x20).toByte()
                                    }else{
                                        query=false
                                    }
                                }
                            }
                        }
                    }
                    OBD_VPW->{
                        if (data.isEmpty()) {
                            query = false
                        } else {
                            var item = data[0]
                            item?.apply {
                                datas.add(item)
                                var bizData = parse2BizSingle(item)
                                if (bizData.get(0)== 0x41.toByte()) {
                                    var flag = bizData.last()
                                    var result = flag?.and(0x01)
                                    if (result == 0x01.toByte()) {
                                        pid1 = (pid1 + 0x20).toByte()
                                    } else {
                                        query = false
                                    }
                                }

                            }

                        }
                    }

                    else-> {
                        if (data.isEmpty()) {
                            query = false
                        } else {
                            var item = data[0]
                            datas.add(item)
                            item?.let {
                                //41 00  BE 5F B8 11
                                var bizData = parse2BizSingle(item)
                                bizData.isNotEmpty().trueLet {
                                    if (bizData[0] ==0x41.toByte()) {
                                        if (bizData.last().and(0x01)==0x01.toByte()) {
                                            pid1 = (pid1 + 0x20).toByte()
                                        }else{
                                            query=false
                                        }
                                    }

                                }.elseLet {
                                    query=false
                                }
                            }
                        }

                    }
                }

            }
        }
        return datas
    }

    /** 查询数据流支持项 */
    fun queryFlowList():List<Short> {
        var maskBuffer = ShortArray(32)
        var freezeKeyList:List<Short> = mutableListOf()
        var pids: MutableList<ByteArray?> = supportFlowPids()

        pids?.apply {

            mergePid(pids, maskBuffer, computerOffset())
            var produPid = produPid(maskBuffer)
             freezeKeyList = dataFlow4KeyList(produPid,0x01)
        }
        return freezeKeyList
    }

    fun supportFreeze(): MutableList<ByteArray?> {
        var query = true
        var pid1 = 0x00.toByte()
        var datas: MutableList<ByteArray?> = mutableListOf()
        while (query) {
            val obdData = ByteArray(3)
            obdData[0] = 0x02
            obdData[1] = pid1
            obdData[2] = 0x00

            var comboCommand = ObdManger.getIns().comboCommand(obdData)
            var data =
                comboCommand?.let { ObdManger.getIns().sendMultiCommandReceMulti(it, 5000, 10) }
            data?.apply {

                if (data.isEmpty()) {
                    query = false
                } else {
                    data[0]?.apply {
                        datas.add(this)
                        var bizData = parse2BizSingle(this)
                        bizData.isNotEmpty().trueLet {
                            if (bizData[0] == 0x42.toByte()) {
                                if (bizData.last().and(0x01) == 0x01.toByte()) {
                                    pid1 = (pid1 + 0x20).toByte()
                                } else {
                                    query = false
                                }
                            }

                        }.elseLet {
                            query=false
                        }
                    }

                }
            }
        }
        return datas
    }

    /** 查询 冻结帧列表 */
    fun queryFreezeList(): List<Short> {
        var maskBuffer = ShortArray(32)
        var freezeKeyList = listOf<Short>()
        var datas: MutableList<ByteArray?> = supportFreeze()
        (datas.size > 0).trueLet {
            mergeFreezePid(datas, maskBuffer, computerOffset())
            var pid = produFreezePid(maskBuffer)
            printMessage(pid.toString())
            freezeKeyList = dataFlow4KeyList(pid, 0x02)
        }

        return freezeKeyList
    }


}