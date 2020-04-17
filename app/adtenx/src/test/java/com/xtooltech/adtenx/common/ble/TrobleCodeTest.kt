package com.xtooltech.adtenx.common.ble

import android.text.TextUtils.indexOf
import com.xtooltech.adtenx.util.toHex
import org.junit.Assert.*
import org.junit.Test

class TrobleCodeTest{

    fun parseTrobCode(cmd:Byte,data:List<ByteArray?>?):Pair<Int,List<Byte>>{
        var isSingle=true
        var amount_code=0
        var index_flag=0
        var troCodeDataList= mutableListOf<Byte>()
        data?.forEachIndexed loop@{
            index,item->
            item?.apply {

                takeIf { index == 0 }?.apply {
                    index_flag= item.indexOf(0x10)
                    takeIf { index_flag>-1 }?.let { isSingle=false }
                }

                takeIf { isSingle }?.apply {/** 单帧 */
                    val sureAnsIndex = indexOf(cmd.plus(0x40.toByte()).toByte())
                    takeIf { sureAnsIndex == -1 }?.apply { return@loop }/** 非肯定应答 */
                    amount_code = this[sureAnsIndex + 1].toInt()/** 故障数 */
                    var elements = this.slice(sureAnsIndex+2 .. size-1) /** 取故障码数据及填充数据*/
                    troCodeDataList.addAll(elements)/** 添加到列表 */
                    return@loop
                } ?:apply {/** 长帧 */
                    takeIf { index==0 }?.apply {
                         index_flag=indexOf(0x10)
                        val index_sure = indexOf(cmd.plus(0x40.toByte()).toByte())
                        takeIf { index_sure == -1 }?.apply { return@loop }
                        amount_code=this[index_sure+1].toInt()
                        var flow_first = slice(index_sure + 2..size-1)
                        troCodeDataList.addAll(flow_first)

                    }?:apply {
                        var flow_else=slice(index_flag+1 .. size-1)
                        troCodeDataList.addAll(flow_else)
                    }
                }
            }
        }

        return Pair(amount_code, troCodeDataList.toList().take(amount_code*2))
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
        val result_single=parseTrobCode(0x03,resp_single)
        assertEquals(1,result_single.first)

        val result_mutil=parseTrobCode(0x03,resp_mutil)
        assertEquals(6,result_mutil.first)
    }

    @Test
    fun readStdExt(){
        //60 0A 0D 88 18 DA F1 58 04 41 03 00 00 AA AA AA
    }
}