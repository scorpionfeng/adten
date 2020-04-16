package com.xtooltech.adtenx.common.ble

import android.text.TextUtils.indexOf
import com.xtooltech.adtenx.util.toHex
import org.junit.Assert.*
import org.junit.Test

class TrobleCodeTest{

    fun parseTrobCode(cmd:Byte,data:List<ByteArray?>?):Pair<Int,List<String>>{
        var isSingle=true
        var amount=0
        val troCodeDataList= mutableListOf<Byte>()
        data?.forEachIndexed {
            index,item->
            item?.apply {
                var flowFlagIndex = item.indexOf(0x10)

                takeIf { flowFlagIndex>-1 }?.let { isSingle=false }

                takeIf { isSingle }?.apply {
                    val sureAnsIndex = indexOf(cmd.plus(0x40.toByte()).toByte())
                    if (sureAnsIndex > -1) {
                        amount = this[sureAnsIndex + 1].toInt()
                        var elements = this.slice((if (sureAnsIndex > 0) sureAnsIndex + 2 else 4) until this.size)
                        elements = elements.filter { item -> (item != 0xaa.toByte()) and (item != 0x00.toByte()) }
                        troCodeDataList.addAll(elements)
                    } else {
                        var elements = this.slice(4 until this.size)
                        elements = elements.filter { item -> (item != 0xaa.toByte()) }
                        troCodeDataList.addAll(elements)
                    }
                } ?:apply {

                    /**
                     *  60 0A 0B 08 07 E8 10 0E 43 06 01 01 02 02
                        60 0A 0B 08 07 E8 21 03 03 04 04 05 05 06
                        60 0A 0B 08 07 E8 22 06 07 00 00 00 00 00
                     */
                    takeIf { index==0 }?.apply {

                       amount= get(indexOf(0x10)+1).toInt()

                    }?:apply {

                    }


                }
            }

        }

        return Pair(0, listOfNotNull())
    }

    @Test
    fun readStd(){
        val resp_single: List<ByteArray?>? = listOf<ByteArray>(
            byteArrayOf(0x60,0x0A,0x0B,0x08,0x07, 0xE8.toByte(),0x04,0x43,0x01,0x00,0x26,0x00,0x00,0x00)
        )

        val resp_mutil:List<ByteArray?>?= listOf(
            byteArrayOf(0x60,0x0A,0x0B,0x08,0x07, 0xE8.toByte(),0x10,0x0E,0x43,0x06,0x01,0x01,0x02,0x02),
            byteArrayOf(0x60,0x0A,0x0B,0x08,0x07, 0xE8.toByte(),0x21,0x03,0x03,0x04,0x04,0x05,0x05,0x06),
            byteArrayOf(0x60,0x0A,0x0B,0x08,0x07, 0xE8.toByte(),0x22,0x06,0x07,0x00,0x00,0x00,0x00,0x00)
        )




    }

    @Test
    fun readStdExt(){
        //60 0A 0D 88 18 DA F1 58 04 41 03 00 00 AA AA AA
    }
}