package com.xtooltech.adtenx.common.ble

import android.text.TextUtils.indexOf
import com.xtooltech.adtenx.common.destructu.DestructCanStd
import com.xtooltech.adtenx.util.toHex
import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception

class TrobleCodeTest{


    @Test
    fun readStd(){
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
    fun readStdExt(){
        //60 0A 0D 88 18 DA F1 58 04 41 03 00 00 AA AA AA
    }
}