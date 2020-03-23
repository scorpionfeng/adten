package com.xtooltech.adten.util

import com.xtooltech.ad10.Utils
import com.xtooltech.adten.common.ble.*
import com.xtooltech.adten.common.obd.*
import com.xtooltech.adten.common.obd.DataStream.engineType
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.experimental.and


const val DS_SUPPORT = 0x01 // ֧�ָ������

// ����������
private val supportDSItems = HashMap<String, DataStreamItem_DataType_STD>()
var isSupportAirFlow = false
var isSuppDataStream = false
private var dsSupportMap: MutableMap<Short, java.util.ArrayList<DataStreamItem_DataType_STD>> =
    HashMap()

fun produPid(data: ShortArray): List<Short> {
    val array = ArrayList<Short>()
    for (i in 0..31) {
        for (j in 1..8) {
            if (data[i] and 0x80 != 0.toShort()) {
                val temp = (i * 8 + j).toShort()
                if (temp.toInt() != 0x01 && temp.toInt() != 0x20 && temp.toInt() != 0x40
                    && temp < 0x4F
                ) {
                    array.add(temp)
                }
            }
            data[i] = (data[i].toInt() shl 1).toShort()
        }
    }
    return array
}

fun produFreezePid(data: ShortArray): List<Short> {
    val array = ArrayList<Short>()

    for (p in 0..31) {
        for (pj in 1..8) {
            if (data[p].toInt() and 0x80 != 0) {
                array.add((p * 8 + pj).toShort())
            }
            data[p] = (data.get(p).toInt() shl 1).toShort()
        }
    }
    return array
}


fun IsRange(raw:Byte,a1:Byte,a2:Byte):Boolean{
    return    (raw in (a1 + 1) until a2)
}

fun dataFlow4KeyList(pidData: List<Short>,sid:Byte): List<Short>{
//[3, 4, 5, 6, 7, 11, 12, 13, 14, 15, 16, 17, 19, 20, 21, 28, 31, 33, 46, 47, 48, 49, 50, 51, 60, 65, 66, 67, 68, 69, 70, 71, 73, 74, 76]
    var vsTids:MutableList<Short> = mutableListOf()
    var binFrame: ByteArray= byteArrayOf()
//        for (k in binPids.size - 1 downTo 0) {
//            if (SID === 0x02) binFrame =
//                SendRecvByte(DEFAULT, 3, 0x02, binPids.get(k), 0x00) else binFrame =
//                SendRecvByte(DEFAULT, 2, 0x01, binPids.get(k))
//            if (binFrame.IsEmpty() || binFrame.get(0) !== 0x40 + SID) binPids.RemoveAt(k)
//        }
    var O2S_PID: Byte = 0
    for (j in 0 until pidData.size) {
        val pid: Byte = pidData.get(j).toByte()
        if (pid == 0x13.toByte()) {
            O2S_PID = 0x13
            break
        }
        if (pid == 0x1d.toByte()) {
            O2S_PID = 0x1D
            break
        }
    }
    for (i in 0 until pidData.size) {
        val pid: Byte = pidData.get(i).toByte()
        if (IsRange(pid, 0x01, 0x01)) { //01
            vsTids.add(0x0110)
            vsTids.add(0x0111)
            /*			BYTE k;
        for(k=0x20;k<=0x22;k++)vsTids.Add(0x0100|k);
        for(k=0x24;k<=0x26;k++)vsTids.Add(0x0100|k);
        for(k=0x30;k<=0x37;k++)vsTids.Add(0x0100|k);
        for(k=0x40;k<=0x47;k++)vsTids.Add(0x0100|k);
*/
        }
        if (IsRange(pid, 0x41, 0x41)) { //41
/*			BYTE k;
			for(k=0x20;k<=0x22;k++)vsTids.Add(0x4100|k);
			for(k=0x24;k<=0x26;k++)vsTids.Add(0x4100|k);
			for(k=0x30;k<=0x37;k++)vsTids.Add(0x4100|k);
			for(k=0x40;k<=0x47;k++)vsTids.Add(0x4100|k);
*/
        } else if (IsRange(pid, 0x03, 0x03)) { //03
            vsTids.add((pid.toInt() shl 8 or 0x00).toShort())
            vsTids.add((pid.toInt() shl 8 or 0x10).toShort())
        } else if (IsRange(pid, 0x06, 0x09)) { //06~09
            if (O2S_PID == 0x1D.toByte()) vsTids.add((pid.toInt() shl 8 or 0x10).toShort()) else vsTids.add(
                (pid.toInt() shl 8 or 0x00).toShort()
            )
        } else if (IsRange(pid, 0x14, 0x1B)) { //14~1B
            if (O2S_PID == 0x1D.toByte()) {
                vsTids.add((pid.toInt() shl 8 or 0x01).toShort())
                vsTids.add((pid.toInt() shl 8 or 0x11).toShort())
            } else {
                vsTids.add((pid.toInt() shl 8 or 0x00).toShort())
                vsTids.add((pid.toInt() shl 8 or 0x10).toShort())
            }
        } else if (IsRange(pid, 0x24, 0x2B) ||  //24~2B
            IsRange(pid, 0x34, 0x3B)
        ) { //34~3B
            if (O2S_PID == 0x1D.toByte()) {
                vsTids.add((pid.toInt() shl 8 or 0x01).toShort())
                vsTids.add((pid.toInt() shl 8 or 0x21).toShort())
            } else {
                vsTids.add((pid.toInt() shl 8 or 0x00).toShort())
                vsTids.add((pid.toInt() shl 8 or 0x20).toShort())
            }
        } else {
            vsTids.add((pid.toInt() shl 8 or 0x00).toShort())
        }
    }
    return vsTids
}


fun dataFlowKeyList(pidData: List<Short>): MutableList<DataStreamItem_DataType_STD> {
    var result = mutableListOf<DataStreamItem_DataType_STD>()
    var O2SLocId: Short = 0
    val pidLength: Int = pidData.size
    for (p in 0 until pidLength) {
        if (pidData[p] === 0x13.toShort()) {
            O2SLocId = 0x13
            break
        }
        if (pidData[p] === 0x1D.toShort()) {
            O2SLocId = 0x1D
            break
        }
    }
    // dsidArray init
    // dsidArray init
    val length2: Int = pidData.size
    val dsIDArray = Frame()
    for (p in 0 until length2) {
        val pid = (pidData[p] and 0xFF)
        if (pid >= 0x14 && pid <= 0x1B
            || pid >= 0x24 && pid <= 0x2B
            || pid >= 0x34 && pid <= 0x3B
        ) {
            if (O2SLocId.toInt() == 0x13) {
                dsIDArray.add(
                    String.format(
                        "0x00,0x00,0x00,0x00,0x00,0x%02X", pid
                    )
                )
            } else if (O2SLocId.toInt() == 0x1D) {
                dsIDArray.add(
                    String.format(
                        "0x00,0x00,0x00,0x00,0x02,0x%02X", pid
                    )
                )
            }
        } else {
            dsIDArray.add(
                String.format(
                    "0x00,0x00,0x00,0x00,0x00,0x%02X", pid
                )
            )
        }
        if (pid >= 0x14 && pid <= 0x1B
            || pid >= 0x24 && pid <= 0x2B
            || pid >= 0x34 && pid <= 0x3B
        ) {
            if (O2SLocId.toInt() == 0x13) {
                dsIDArray.add(
                    String.format(
                        "0x00,0x00,0x00,0x00,0x01,0x%02X", pid
                    )
                )
            } else if (O2SLocId.toInt() == 0x1D) {
                dsIDArray.add(
                    String.format(
                        "0x00,0x00,0x00,0x00,0x03,0x%02X", pid
                    )
                )
            }
        } else if (pid.toInt() == 0x03) {
            dsIDArray.add(
                String.format(
                    "0x00,0x00,0x00,0x00,0x01,0x%02X", pid
                )
            )
        } else if (pid.toInt() == 0x41) {
            for (n in 1..21) {
                dsIDArray.add(
                    String.format(
                        "0x00,0x00,0x00,0x00,0x%02X,0x%02X", n + 0x70,
                        pid
                    )
                )
            }
        }
    }

    TimeUnit.SECONDS.sleep(1)
    // pidData初始化完成

    // pidData初始化完成
    for (a in 0 until dsIDArray.count()) {
        val dsIDDataArray = dsIDArray[a]
        val dsItem = DataStreamItem_DataType_STD(
            dsIDDataArray
        )
        dsItem.supportType = DS_SUPPORT
        val ds_file = DataBaseBin.searchDS(dsIDDataArray)
        if (ds_file != null) {
            dsItem.dsName = ds_file.dsName()
            val dsUnit = ds_file.dsUnit()
            dsItem.dsUnit = CurrentData.unitChoose(dsUnit)
            dsItem.dsCMD = ds_file.dsCommand()
            result.add(dsItem)
            result.add(dsItem)
            supportDSItems.put(dsIDDataArray.binaryToCommand(), dsItem)
        }
        if ("0x00,0x00,0x00,0x00,0x00,0x10".equals(
                dsIDDataArray.binaryToCommand(),
                ignoreCase = true
            )
        ) {
            isSupportAirFlow = true
        }
    }
    if (result.size > 0) {
        isSuppDataStream = true
    }

    return result
}


fun freezeKeyList(pidData: List<Short>): MutableList<Freeze_DataType_STD> {

    var freeseItem: MutableList<Freeze_DataType_STD> = mutableListOf()

    var O2SLocId: Short = 0
    for (o in 0 until pidData.size) {
        if (pidData[o] === 0x13.toShort()) {
            O2SLocId = 0x13
            break
        }
        if (pidData[o] === 0x1D.toShort()) {
            O2SLocId = 0x1D
            break
        }
    }

    val tempArray = java.util.ArrayList<String>()
    for (element in pidData) {
        val tempPidData = element
        if (tempPidData >= 0x14 && tempPidData <= 0x1B
            || tempPidData >= 0x24 && tempPidData <= 0x2B
            || tempPidData >= 0x34 && tempPidData <= 0x3B
        ) {
            if (O2SLocId.toInt() == 0x13) {
                tempArray.add(String.format("0x00,0x00,0x00,0x00,0x00,0x%02X", tempPidData))
            } else if (O2SLocId.toInt() == 0x1D) {
                tempArray.add(String.format("0x00,0x00,0x00,0x00,0x02,0x%02X", tempPidData))
            }
        } else {
            tempArray.add(String.format("0x00,0x00,0x00,0x00,0x00,0x%02X", tempPidData))
        }
        if (tempPidData >= 0x14 && tempPidData <= 0x1B
            || tempPidData >= 0x24 && tempPidData <= 0x2B
            || tempPidData >= 0x34 && tempPidData <= 0x3B
        ) {
            if (O2SLocId.toInt() == 0x13) {
                tempArray.add(String.format("0x00,0x00,0x00,0x00,0x01,0x%02X", tempPidData))
            }
            if (O2SLocId.toInt() == 0x1D) {
                tempArray.add(String.format("0x00,0x00,0x00,0x00,0x03,0x%02X", tempPidData))
            }
        } else if (tempPidData.toInt() == 0x03) {
            tempArray.add(String.format("0x00,0x00,0x00,0x00,0x01,0x%02X", tempPidData))
        } else if (tempPidData.toInt() == 0x41) {
            for (pid41 in 1..21) {
                tempArray.add(
                    String.format(
                        "0x00,0x00,0x00,0x00,0x%02X,0x%02X",
                        pid41 + 0x70,
                        tempPidData
                    )
                )
            }
        }
    }
    for (ta in tempArray.indices) {
        val ds: DS_File = DataBaseBin.searchDS(tempArray[ta])
        if (ds.isSearch()) {
            val searchData: DataArray = ds.dsCommand()
            val cmdBuffer = ShortArray(3)
            cmdBuffer[0] = 0x02
            cmdBuffer[1] = searchData.get(1)
            cmdBuffer[2] = 0x00
            val unit: String = CurrentData.unitChoose(ds.dsUnit())
            val freezeItem = Freeze_DataType_STD()
            freezeItem.setFreezeID(tempArray[ta])
            freezeItem.setFreezeName(ds.dsName())
            freezeItem.setFreezeUnit(unit)
            freezeItem.setFreezeCommand(DataArray(cmdBuffer, 3))
            freeseItem.add(freezeItem)
        }
    }

    return freeseItem

}


fun ByteArray.toHex():String{
    return  Utils.debugByteData(this)
}
fun List<Byte>.toHex():String{
    return  Utils.debugByteData(this.toByteArray())
}

fun Short.toObdIndex():String{
    var format = String.format("%04x", this)
    var first = format.substring(0, 2)
    var second=format.substring(2,4)
    return "0x00,0x00,0x$first,0x$second"
}
fun Short.toObdPid():Byte{
    var pid = this.toInt() shr 8
    return pid.toByte()
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


fun parse2BizSingle(obdData:ByteArray):List<Byte>{



    var bizData= arrayListOf<Byte>()

    when(ObdManger.getIns().currProto){
        OBD_STD_CAN ->  return obdData.slice(4 .. obdData[3]+3)
        OBD_EXT_CAN -> return obdData.slice(4 .. obdData[3]+3)
        OBD_ISO -> return obdData.slice(3 .. obdData.size-2)
        OBD_KWP -> "KWP"
        OBD_VPW -> return obdData.slice(3 .. obdData.size-2)
        OBD_PWM -> return obdData.slice(3 .. obdData.size-2)
        OBD_UNKNOWN -> "未知"
    }

   return bizData
    // pwm //41 6B 10 [ 41 00  BE 5F B8 11] 4F
    // can 08 07 E8 07 [42 20 00 10 05 B0 15]
    // can 08 07 E8 04 [42 20 00 10] 00 00 00
}

fun supportItem2(flag: Int): MutableList<ByteArray?> {
    var query: Boolean = true
    var size = if (flag == 0) 2 else 3
    var pid1 = 0x00.toByte()
    var datas: MutableList<ByteArray?> = mutableListOf()
    while (query) {
        val obdData = ByteArray(size)
        obdData[0] = 0x02
        obdData[1] = pid1
        if (flag == 1) {
            obdData[2] = 0x00
        }

        var comboCommand = ObdManger.getIns().comboCommand(obdData)
        var data = comboCommand?.let { ObdManger.getIns().sendMultiCommandReceMulti(it, 5000, 10) }

        data?.apply {
            var item = data[0]
            datas.add(item)
            var flag = item?.get(3 + item[3]) //取有效数据的最后一位
            var result = flag?.and(0x01)
            if (result == 0x01.toByte()) {
                pid1 = (pid1 + 0x20).toByte()
            } else {
                query = false
            }
        }
    }
    return datas
}

fun hexString(data: Byte): String {
    return String.format("%02X", data)
}

fun List<Byte>.hexString():String{
    return this.map { hexString(it) }.toString()
}

fun mergePid(
    data: List<ByteArray?>,
    maskBuffer: ShortArray,
    offset: Int
) {
    data.isNotEmpty().trueLet {
        for (index in data.indices) {
            var inItem = data[index]
            if (inItem?.get(0 + offset) == 0x41.toByte()) {
                if (inItem.size > 2) {
                    maskBuffer[index * 4 + 0] = inItem.get(2 + offset).toShort()
                }
                if (inItem.size > 3) {
                    maskBuffer[index * 4 + 1] = inItem.get(3 + offset).toShort()
                }
                if (inItem.size > 4) {
                    maskBuffer[index * 4 + 2] = inItem.get(4 + offset).toShort()
                }
                if (inItem.size > 5) {
                    maskBuffer[index * 4 + 3] = (inItem.get(5 + offset).toShort() and 0xFE)
                    if (inItem[5 + offset].toShort() and 0x01 == 0.toShort()) break
                }
            }
        }

    }
}

fun mergeFreezePid(
    data: List<ByteArray?>,
    maskBuffer: ShortArray,
    offset: Int
) {
    for (index in 0 until data?.size!!) {
        var inItem = data[index]
        if (inItem?.get(0 + offset) == 0x42.toByte()) {
            if (inItem.size > 3) {
                maskBuffer[index * 4 + 0] = inItem.get(3 + offset).toShort()
            }
            if (inItem.size > 4) {
                maskBuffer[index * 4 + 1] = inItem.get(4 + offset).toShort()
            }
            if (inItem.size > 5) {
                maskBuffer[index * 4 + 2] = inItem.get(5 + offset).toShort()
            }
            if (inItem.size > 6) {
                maskBuffer[index * 4 + 3] = inItem.get(6 + offset).toShort()
                if (inItem[6 + offset].toShort() and 0x01 === 0.toShort()) break
            }
        }
    }
}

/**
 *
 *
 * fuelAdjustment, speedAdjustment, weight, displacement;  //油耗系数，速度系数，车辆总重，加速度，排量
 *
 * isSupportAirFlow
 * airFlow
 * fuelUnit
 * engineValue
 * displacement
 * airPressure
 * rpm
 * fuelAdjustment
 * */

fun calculationWithAirFlow(airFlowValue:Int,engineKind:Int,fuelUnitValue:Int):String{
    var rec=""
    if (engineKind == 0)  //汽油
    {
        var value = airFlowValue/14.7/0.725/1000*3600        //L／h
        if (fuelUnitValue == 1)           //gal(us)/h
            value = value/3.785;
        else if (fuelUnitValue == 2)      //gal(uk)/h
            value = value/4.546;
        rec = String.format("%1.2f", value)
    }
    else if (engineKind == 1)  //柴油
    {
        var value:Double=0.0
            value = (airFlowValue/14.3/0.86/1000*3600);        //L／h
        if (fuelUnitValue == 1)           //gal(us)/h
            value = value/3.785;
        else if (fuelUnitValue == 2)      //gal(uk)/h
            value = value/4.546;
        rec = String.format("%1.2f", value)
    }
    return rec
}

fun calcSpecialDataItem(isSupportAirFlow:Boolean,airFlow:Int,fuelUnit:Int,engineValue:Int,displacement:Int,airTemperature:Int,airPressure:Int,rpm:Int,fuelAdjustment:Int=1):String{
    var rec=""
    if (engineValue == 0)  //汽油
    {
        var value:Double=0.0
        if (isSupportAirFlow)
            value = fuelAdjustment*(airFlow/14.7/0.725/1000*3600);        //L／h
        else
            value = fuelAdjustment*(9.6898/1000*rpm*airPressure/(airTemperature+273.15)*displacement*0.85);    //L／h
        if (fuelUnit == 1)           //gal(us)/h
            value = value/3.785;
        else if (fuelUnit == 2)      //gal(uk)/h
            value = value/4.546;
        rec = String.format("%0.2f", value)
    }
    else if (engineValue == 1)  //柴油
    {
        var value:Double=0.0
        if (isSupportAirFlow)
            value = fuelAdjustment*(airFlow/14.3/0.86/1000*3600);        //L／h
        else
            value = fuelAdjustment*(8.513/1000*rpm*airPressure/(airTemperature+273.15)*displacement*0.85);    //L／h
        if (fuelUnit == 1)           //gal(us)/h
            value = value/3.785;
        else if (fuelUnit == 2)      //gal(uk)/h
            value = value/4.546;
        rec = String.format("%0.2f", value)
    }
    return rec
}