package com.xtooltech.adtenx.common.ble

import android.text.TextUtils.indexOf
import com.xtooltech.adtenx.common.destructu.*
import com.xtooltech.adtenx.util.toHex
import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception

class TrobleCodeTest{


    @Test
    fun readCanStd(){
        val destruct=DestructCanStd()
        val resp_single: List<ByteArray?> = listOf<ByteArray>(
            byteArrayOf(0x60,0x0A,0x0B,0x08,0x07, 0xE8.toByte(),0x04,0x43,0x01,0x00,0x26,0x00,0x00,0x00)
        )
        val resp_mutil:List<ByteArray?> = listOf(
            byteArrayOf(0x60,0x0A,0x0B,0x08,0x07, 0xE8.toByte(),0x10,0x0E,0x43,0x06,0x01,0x01,0x02,0x02),
            byteArrayOf(0x60,0x0A,0x0B,0x08,0x07, 0xE8.toByte(),0x21,0x03,0x03,0x04,0x04,0x05,0x05,0x06),
            byteArrayOf(0x60,0x0A,0x0B,0x08,0x07, 0xE8.toByte(),0x22,0x06,0x07,0x00,0x00,0x00,0x00,0x00)
        )
        val result_single=destruct.parseTrobCode(0x03,resp_single)
        println(result_single.second)
        assertEquals(1,result_single.first)

        val result_mutil=destruct.parseTrobCode(0x03,resp_mutil)
        assertEquals(6,result_mutil.first)
        println(result_mutil.second)
    }


    @Test
    fun readIso(){
        //48 6B 10 43 18 47 00 00 00 00
        var cmd:Byte=0x03
        ObdManger.getIns().currProto= OBD_ISO
        var data= listOf<ByteArray>(
            // 47 01 93 12 33 00 00
            byteArrayOf(0x48,0x6B,0x10,0x43,0x18,0x47,0x00,0x00,0x00,0x00)
        )
        var deser= DestructIso()
        var result = deser.parseTrobCode(cmd, data)
        println(result.first.toString() +">>>"+result.second.toString())
        assertEquals(result.first,1)
    }


    @Test
    fun readCanExt(){

        var cmd:Byte=0x07
        ObdManger.getIns().currProto= OBD_EXT_CAN
        val destruct=DestructCanExt()
//        val resp_single: List<ByteArray?> = listOf<ByteArray>(
//            byteArrayOf(0x60,0x0A,0x0B,0x08,0x07, 0xE8.toByte(),0x04,0x43,0x01,0x00,0x26,0x00,0x00,0x00)
//        )
        val resp_mutil:List<ByteArray?> = listOf(
            byteArrayOf(0x88.toByte(),0x18, 0xDA.toByte(), 0xF1.toByte(),0x00,0x10,0x0E,0x47,0x06,0x12,0x19,0x12,0x1A),
            byteArrayOf(0x88.toByte(),0x18, 0xDA.toByte(), 0xF1.toByte(),0x00,0x21,0x12,0x16,0x12,0x18,0x12,0x06,0x12),
            byteArrayOf(0x88.toByte(),0x18, 0xDA.toByte(), 0xF1.toByte(),0x00,0x22,0x38,0x00,0x00,0x00,0x00,0x00,0x00)
        )
//        val result_single=destruct.parseTrobCode(0x03,resp_single)
//        println(result_single.second)
//        assertEquals(1,result_single.first)

        val result_mutil=destruct.parseTrobCode(0x07,resp_mutil)
        assertEquals(6,result_mutil.first)
        println(result_mutil.second)
    }

    @Test
    fun readVpw(){
        //48 6B 10 47 01 02 03 04 05 06 D9
        var cmd:Byte=0x07
        ObdManger.getIns().currProto= OBD_VPW
        var data= listOf<ByteArray>(
            // 47 01 93 12 33 00 00
            byteArrayOf(0x48,0x6B,0x10,0x47,0x01,0x02,0x03,0x04,0x05,0x06, 0xD9.toByte())
        )
        var deser= DestructVpw()
        var result = deser.parseTrobCode(cmd, data)
        println(result.first.toString() +">>>"+result.second.toString())
        assertEquals(result.first,3)

        var cmd2:Byte=0x03


        val data2= listOf<ByteArray>(
            byteArrayOf(0x48,0x6B,0x10,0x43,0x01,0x07,0x03,0x04,0x05,0x06, 0xD9.toByte()),
            byteArrayOf(0x48,0x6B,0x10,0x43,0x02,0x08,0x07,0x02,0x00,0x31,0x69),
            byteArrayOf(0x48,0x6B,0x10,0x43,0x03,0x09,0x47,0x4B,0x46,0x4B, 0x83.toByte()),
            byteArrayOf(0x48,0x6B,0x10,0x43,0x04,0x0A,0x36,0x36,0x55,0x34, 0xC0.toByte()),
            byteArrayOf(0x48,0x6B,0x10,0x43,0x05,0x0B,0x32,0x4A,0x31,0x30,0x01),
            byteArrayOf(0x48,0x6B,0x10,0x43,0x06,0x0C,0x37,0x30,0x36,0x38, 0xEE.toByte())
        )

        var result2 = deser.parseTrobCode(cmd2, data2)
        println(result2.first.toString() +">>>"+result2.second.toString())
        assertEquals(result2.first,18)
    }

    @Test
    fun readPwm(){
        /**
         *
        send: 60 09 05 61 6A F1 07 B8
        recv: 60 0A 0B 41 6B 10 47 01 93 12 33 00 00 0C
         */
        var cmd:Byte=0x07
        ObdManger.getIns().currProto= OBD_PWM
        var data= listOf<ByteArray>(
            // 47 01 93 12 33 00 00
            byteArrayOf(0x41,0x6B,0x10,0x47,0x01, 0x93.toByte(),0x12,0x33,0x00,0x00,0x0C)
        )
        var deser= DestructPwm()
        var result = deser.parseTrobCode(cmd, data)
        println(result.first.toString() +">>>"+result.second.toString())
        assertEquals(result.first,2)

    }

    @Test
    fun readKwp(){
        /**
        req=C1 33 F1 07 EC
        res=87 F1 10 47 06 38 01 13 06 86 AD
        res=87 F1 10 47 C1 55 01 08 01 08 F7"""

**/

        var cmd:Byte=0x07
        ObdManger.getIns().currProto= OBD_KWP
        var data= listOf<ByteArray>(
            byteArrayOf(0x87.toByte(), 0xF1.toByte(),0x10,0x47,0x06,0x38,0x01,0x13,0x06, 0x86.toByte(), 0xAD.toByte()),
            byteArrayOf(0x87.toByte(), 0xF1.toByte(),0x10,0x47, 0xC1.toByte(),0x55,0x01,0x08,0x01,0x08, 0xF7.toByte())
        )
        var deser= DestructKwp()
        var result = deser.parseTrobCode(cmd, data)
        println(result.first.toString() +">>>"+result.second.toString())
        assertEquals(result.first,6)
    }
}