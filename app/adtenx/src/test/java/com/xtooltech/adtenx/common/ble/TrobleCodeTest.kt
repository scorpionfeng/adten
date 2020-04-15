package com.xtooltech.adtenx.common.ble

import com.xtooltech.adtenx.util.toHex
import org.junit.Assert.*
import org.junit.Test

class TrobleCodeTest{
    @Test
    fun readStd(){

        /**
            60 09 0B 08 07 DF 01 03 00 00 00 00 00 00
            60 0A 0B 08 07 E8 02 43 00 AA AA AA AA AA
         */
        val trobleCode= byteArrayOf(
            0x60,0x0A,0x0B,0x08,0x07, 0xE8.toByte(),0x02,0x43,0x00, 0xAA.toByte(), 0xAA.toByte(), 0xAA.toByte(), 0xAA.toByte(), 0xAA.toByte()
        )

        val obdManger=ObdManger.getIns()
        val command =obdManger. comboCommand(byteArrayOf(0x03))
        println(command?.toHex()) //08 07 DF 01 03 00 00 00 00 00 00


        var codes= mutableListOf<Byte>()
        // 60 0A 0B 08 07 E8 02 43 00 AA AA AA AA AA
        var amount=0
        trobleCode?.forEach {
            it?.apply {
                val sureAnsIndex =trobleCode.indexOf(0x43.toByte())
                if (sureAnsIndex > -1) {
                    amount = trobleCode[sureAnsIndex + 1].toInt()
                    var elements = trobleCode.slice((if (sureAnsIndex > 0) sureAnsIndex + 2 else 4) until trobleCode.size)
                    elements = elements.filter { item -> (item != 0xaa.toByte()) and (item != 0x00.toByte()) }
                    codes.addAll(elements)
                } else {
                    var elements = trobleCode.slice(4 until trobleCode.size)
                    elements = elements.filter { item -> (item != 0xaa.toByte()) and (item != 0x00.toByte()) }
                    codes.addAll(elements)
                }
            }
        }

        println("amount=$amount")
    }

    @Test
    fun readStdExt(){
        //60 0A 0D 88 18 DA F1 58 04 41 03 00 00 AA AA AA
    }
}