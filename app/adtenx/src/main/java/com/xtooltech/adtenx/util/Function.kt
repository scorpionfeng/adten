package com.xtooltech.adtenx.util

import com.xtooltech.adten.util.trueLet
import com.xtooltech.adtenx.common.ble.*
import com.xtooltech.adtenx.plus.Utils
import java.util.*
import kotlin.collections.ArrayList
import kotlin.experimental.and


fun produPid(data: ShortArray): List<Short> {
    val array = ArrayList<Short>()
    for (i in 0..31) {
        for (j in 1..8) {
            if (data[i] and 0x80 != 0.toShort()) {
                val temp = (i * 8 + j).toShort()
                if (temp.toInt() != 0x20 && temp.toInt() != 0x40  && temp < 0x4F
                ) {
                    array.add(temp)
                }
            }
            data[i] = (data[i].toInt() shl 1).toShort()
        }
    }
    return array
}

operator fun Array<Any>.get(i:Int):Int{
    if (this[i] is Int){
        return this[i] as Int
    }else if (this[i] is String){
        try {
            return this[i].toString().toInt()
        }catch (e:Exception){

        }
    }
    return 0
}

/**
 * 肯定 应答
 */
fun List<Byte>.isPositive ():Boolean{
   return  this[0]!=0x7f.toByte()
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


fun IsRange(x:Byte,x1:Byte,x2:Byte):Boolean{
    return ((x)>=(x1)&&(x)<=(x2))
//    return    (raw in (a1 + 1) until a2)
}

fun dataFlow4KeyList(pidData: List<Short>,sid:Byte): List<Short>{
    var vsTids:MutableList<Short> = mutableListOf()
    var O2S_PID: Byte = 0
    for (j in pidData.indices) {
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
    for (i in pidData.indices) {
        val pid: Byte = pidData.get(i).toByte()
        if (IsRange(pid, 0x01, 0x01)) { //01
            vsTids.add(0x0110)
            vsTids.add(0x0111)
        }
        if (IsRange(pid, 0x41, 0x41)) { //41
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



fun ByteArray.toHex():String{
    return  Utils.debugByteData(this)
}
fun List<Byte>.toHex():String{
    return  Utils.debugByteData(this.toByteArray())
}

fun Short.toObdIndex():String{
    val format = String.format("%04x", this)
    val first = format.substring(0, 2).toUpperCase(Locale.ROOT)
    val second=format.substring(2,4).toUpperCase(Locale.ROOT)
    return "0x00,0x00,0x$first,0x$second"
}
fun Short.toObdPid():Byte{
    var pid = this.toInt() shr 8
    return pid.toByte()
}


/**
 * 将数据解构成单帧数据
 */
fun parse2BizSingle(obdData:ByteArray):List<Byte>{

    var bizData= arrayListOf<Byte>()

    when(ObdManger.getIns().currProto){
        OBD_STD_CAN ->  return obdData.slice(4 .. obdData[3]+3)
        OBD_EXT_CAN -> return obdData.slice(6 .. obdData[5]+5)
        OBD_ISO -> return obdData.slice(3 .. obdData.size-2)
        OBD_KWP -> {
            /**
             * kwp   1)单独的一个长度字节,第一个字节包含长度信息  , 第一位等于80时,有单独的长度信息,肯定应答位 前一位即是有效长度
            2)如果大于80,则后一位为长度,即 0x86 (6 为长度), 不能大于C0
            3)C* 格式,  *代表长度,  例: C3即3为长度
            86 F1 10 41 00 BE 3E B8 11 8D
             */
            if(obdData.first()==0x80.toByte()){
                //80  F1 10 06 41 00 BE 3E B8 11 8D  ->06不长度

                return obdData.slice(4.. obdData.size-2)

            }else if(obdData.first()<0xc0){//obdData.first()<0xc0 and obdData.first()>0x80
                //86 F1 10 41 00 BE 3E B8 11 8D  ->06不长度
                var length=obdData.first()
                return obdData.slice(3.. obdData.size-2)

            }else if(obdData.first()>0xc0){//0xc*
                return obdData.slice(3.. obdData.size-2)
            }else{
                //错误帧
                return listOf()
            }

        }
        OBD_VPW -> return obdData.slice(3 .. obdData.size-2)
        OBD_PWM -> return obdData.slice(3 .. obdData.size-2)
        OBD_UNKNOWN -> "未知"
    }

   return bizData

}

/**
 * 字节输出 16进制字符
 */
fun hexString(data: Byte): String {
    return String.format("%02X", data)
}

/**
 * 列表转换成 16进制字符
 */
fun List<Byte>.hexString():String{
    return this.map { hexString(it) }.toString()
}

/**
 * 计算PID
 */
fun mergePid(
    data: List<ByteArray?>,
    maskBuffer: ShortArray,
    offset: Int
) {
    data.isNotEmpty().trueLet {
        for (index in data.indices) {
            val inItem = data[index]
            inItem?.apply {
                takeIf { this.size>5+offset }?.apply {
                    if (this[0 + offset] == 0x41.toByte() && this.size>5) {
                        if (this.size > 2) {
                            maskBuffer[index * 4 + 0] = this[2 + offset].toShort()
                        }
                        if (this.size > 3) {
                            maskBuffer[index * 4 + 1] = this[3 + offset].toShort()
                        }
                        if (this.size > 4) {
                            maskBuffer[index * 4 + 2] = this[4 + offset].toShort()
                        }
                        if (this.size > 5) {
                            maskBuffer[index * 4 + 3] = (this[5 + offset].toShort() and 0xFE)
                            if (this[5 + offset].toShort() and 0x01 == 0.toShort()) return
                        }
                    }
                }
            }
        }
    }
}

/**
 * 合并冻结帧PID
 */
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
 * 计算瞬时油耗
 */
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


/**
 * byte转整型
 */
fun Byte.b2i():Int{
    return  if (this < 0) {
        256 + this
    } else {
        this.toInt()
    }
}