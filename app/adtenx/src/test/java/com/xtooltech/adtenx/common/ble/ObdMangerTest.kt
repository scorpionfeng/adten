package com.xtooltech.adtenx.common.ble

import com.xtooltech.adtenx.common.destructu.*
import org.junit.Test

import org.junit.Assert.*

class ObdMangerTest {

    @Test
    fun readCanStdVin() {
        val data= listOf<ByteArray>(
            byteArrayOf(0x08,0x07,0xE8.toByte(),0x10,0x14,0x49,0x02,0x01,0x57,0x42,0x41),
            byteArrayOf(0x08,0x07,0xE8.toByte(),0x21,0x50,0x43,0x39,0x31,0x30,0x36,0x37),
            byteArrayOf(0x08,0x07,0xE8.toByte(),0x22,0x57,0x44,0x38,0x38,0x34,0x39,0x35)
        )
        val canDest=DestructCanStd()
        var vinValue = canDest.parseVin(
            data
        )
       assertEquals("WBAPC91067WD88495",vinValue)
    }

    @Test
    fun readCanExtVin(){
        val data= listOf<ByteArray>(
            byteArrayOf(0x88.toByte(),0x18, 0xDA.toByte(), 0xF1.toByte(),0x0E,0x10,0x14,0x49,0x02,0x01,0x57,0x42,0x41),
            byteArrayOf(0x88.toByte(),0x18, 0xDA.toByte(), 0xF1.toByte(),0x0E,0x21,0x50,0x43,0x39,0x31,0x30,0x36,0x37),
            byteArrayOf(0x88.toByte(),0x18, 0xDA.toByte(), 0xF1.toByte(),0x0E,0x22,0x57,0x44,0x38,0x38,0x34,0x39,0x35)
        )
        val dest=DestructCanExt()
        var vinValue = dest.parseVin(
            data
        )
        assertEquals("WBAPC91067WD88495",vinValue)
    }


    @Test
    fun readIsoVin(){
        val data= listOf<ByteArray>(
            byteArrayOf(0x48,0x6B,0x10,0x49,0x01,0x00,0x00,0x00,0x57, 0xC4.toByte()),
            byteArrayOf(0x48,0x6B,0x10,0x49,0x02,0x42,0x41,0x50,0x43, 0xC2.toByte()),
            byteArrayOf(0x48,0x6B,0x10,0x49,0x03,0x39,0x31,0x30,0x36, 0xC3.toByte()),
            byteArrayOf(0x48,0x6B,0x10,0x49,0x04,0x37,0x57,0x44,0x38, 0xC4.toByte()),
            byteArrayOf(0x48,0x6B,0x10,0x49,0x05,0x38,0x34,0x39,0x35, 0xC5.toByte())
        )
        val dest=DestructIso()
        var vinValue = dest.parseVin(
            data
        )
        assertEquals("WBAPC91067WD88495",vinValue)
    }

    @Test
    fun readKwpVin(){
        val data= listOf<ByteArray>(
            byteArrayOf(87, 0xF1.toByte(),0x11,0x49,0x02,0x01,0x00,0x00,0x00,0x4C,0x21),
            byteArrayOf(87, 0xF1.toByte(),0x11,0x49,0x02,0x02,0x53,0x34,0x41,0x53, 0xF1.toByte()),
            byteArrayOf(87, 0xF1.toByte(),0x11,0x49,0x02,0x03,0x42,0x33,0x52,0x37, 0xD5.toByte()),
            byteArrayOf(87, 0xF1.toByte(),0x11,0x49,0x02,0x04,0x45,0x41,0x37,0x31, 0xC6.toByte()),
            byteArrayOf(87, 0xF1.toByte(),0x11,0x49,0x02,0x05,0x31,0x33,0x38,0x30, 0xA5.toByte())
        )
        val dest=DestructKwp()
        var vinValue = dest.parseVin(
            data
        )
        assertEquals("LS4ASB3R7EA711380",vinValue)
    }

    @Test
    fun readPwmVin(){
        val data= listOf<ByteArray>(
            byteArrayOf(0x60,0x0A,0x0B,0x41,0x6B,0x10,0x49,0x02,0x01,0x4C,0x56,0x53,0x46, 0x94.toByte()),
            byteArrayOf(0x60,0x0A,0x0B,0x41,0x6B,0x10,0x49,0x02,0x02,0x42,0x46,0x41,0x43, 0x82.toByte()),
            byteArrayOf(0x60,0x0A,0x0B,0x41,0x6B,0x10,0x49,0x02,0x03,0x58,0x34,0x46,0x30, 0xBA.toByte()),
            byteArrayOf(0x60,0x0A,0x0B,0x41,0x6B,0x10,0x49,0x02,0x04,0x31,0x32,0x35,0x35, 0xFD.toByte()),
            byteArrayOf(0x60,0x0A,0x0B,0x41,0x6B,0x10,0x49,0x02,0x05,0x31,0x00,0x00,0x00,0x3F)
        )
        val dest=DestructPwm()
        var vinValue = dest.parseVin(
            data
        )
        assertEquals("LVSFBFACX4F012551",vinValue)
    }

    @Test
    fun readVpwVin(){
        val data= listOf<ByteArray>(
            byteArrayOf(0x48,0x6B,0x10,0x49,0x02,0x01,0x00,0x00,0x00,0x4C, 0xB1.toByte()),
            byteArrayOf(0x48,0x6B,0x10,0x49,0x02,0x02,0x53,0x47,0x44,0x43, 0x92.toByte()),
            byteArrayOf(0x48,0x6B,0x10,0x49,0x02,0x03,0x38,0x32,0x44,0x36, 0xF1.toByte()),
            byteArrayOf(0x48,0x6B,0x10,0x49,0x02,0x04,0x41,0x45,0x30,0x31, 0x97.toByte()),
            byteArrayOf(0x48,0x6B,0x10,0x49,0x02,0x05,0x30,0x30,0x33,0x32, 0xA9.toByte())
        )
        val dest=DestructVpw()
        var vinValue = dest.parseVin(
            data
        )
        assertEquals("LSGDC82D6AE010032",vinValue)
    }
}