package com.xtooltech.adten.util

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