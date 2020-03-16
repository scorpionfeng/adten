package com.xtooltech.adten.util

import com.xtooltech.adten.common.ble.ObdManger
import com.xtooltech.base.util.printMessage
import kotlin.experimental.and

fun  produPid(data:ShortArray):List<Short>{
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
fun  produFreezePid(data:ShortArray):List<Short>{
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


fun supportFreeze(): MutableList<ByteArray?> {
    var query =true
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

            takeIf { data?.size>0 }?.apply {
                var item=data[0]
                datas.add(item)
                var flag = item?.get(3 + item[3])
                var result = flag?.and(0x01)
                if (result == 0x01.toByte()) {
                    pid1 = (pid1 + 0x20).toByte()
                } else {
                    query = false
                }
            }

        }
    }
    return datas
}
fun supportItem(flag:Int): MutableList<ByteArray?> {
    var query:Boolean =true
    var size=if(flag==0) 2 else 376
    var pid1 = 0x00.toByte()
    var datas: MutableList<ByteArray?> = mutableListOf()
    while (query) {
        val obdData = ByteArray(size)
        obdData[0] = 0x01
        obdData[1] = pid1
        if(flag==1){
            obdData[2] = 0x00
        }

        var comboCommand = ObdManger.getIns().comboCommand(obdData)
        var data =
            comboCommand?.let { ObdManger.getIns().sendMultiCommandReceMulti(it, 5000, 10) }
        data?.apply {
            var item = data[0]
            datas.add(item)
            var flag = item?.get(3 + item[3])
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
fun supportItem2(flag:Int): MutableList<ByteArray?> {
    var query:Boolean =true
    var size=if(flag==0) 2 else 3
    var pid1 = 0x00.toByte()
    var datas: MutableList<ByteArray?> = mutableListOf()
    while (query) {
        val obdData = ByteArray(size)
        obdData[0] = 0x02
        obdData[1] = pid1
        if(flag==1){
            obdData[2] = 0x00
        }

        var comboCommand = ObdManger.getIns().comboCommand(obdData)
        var data =comboCommand?.let { ObdManger.getIns().sendMultiCommandReceMulti(it, 5000, 10) }

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

fun hexString(data:Byte):String{
    return String.format("%02X",data)
}

fun mergePid(
    data: List<ByteArray?>,
    maskBuffer: ShortArray,
    offset:Int
) {
    for (index in 0 until data?.size!!) {
        var inItem = data[index]
        if (inItem?.get(0+offset) == 0x41.toByte()) {
            if (inItem.size > 2) {
                maskBuffer[index * 4 + 0] = inItem.get(2+offset).toShort()
            }
            if (inItem.size > 3) {
                maskBuffer[index * 4 + 1] = inItem.get(3+offset).toShort()
            }
            if (inItem.size > 4) {
                maskBuffer[index * 4 + 2] = inItem.get(4+offset).toShort()
            }
            if (inItem.size > 5) {
                maskBuffer[index * 4 + 3] = (inItem.get(5+offset).toShort() and 0xFE)
                if (inItem[5+offset].toShort() and 0x01 === 0.toShort()) break
            }
        }
    }
}
fun mergeFreezePid(
    data: List<ByteArray?>,
    maskBuffer: ShortArray,
    offset:Int
) {
    for (index in 0 until data?.size!!) {
        var inItem = data[index]
        if (inItem?.get(0+offset) == 0x42.toByte()) {
            if (inItem.size > 3) {
                maskBuffer[index * 4 + 0] = inItem.get(3+offset).toShort()
            }
            if (inItem.size > 4) {
                maskBuffer[index * 4 + 1] = inItem.get(4+offset).toShort()
            }
            if (inItem.size > 5) {
                maskBuffer[index * 4 + 2] = inItem.get(5+offset).toShort()
            }
            if (inItem.size > 6) {
                maskBuffer[index * 4 + 3] = inItem.get(6+offset).toShort()
                if (inItem[6+offset].toShort() and 0x01 === 0.toShort()) break
            }
        }
    }
}