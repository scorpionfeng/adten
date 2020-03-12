package com.xtooltech.adten.module.diy
import com.xtooltech.adten.common.obd.DataArray
import com.xtooltech.adten.common.obd.DataStream
import com.xtooltech.adten.util.mergePid
import com.xtooltech.adten.util.produPid
import org.junit.Assert
import org.junit.Test
import kotlin.experimental.and

internal class FlowListActivityTest{

    @Test
    fun test(){


//        var data1=arrayOf(0x41.toByte(),0x00.toByte(),0xbe.toByte(),0x5f.toByte(),0xb8.toByte(),0x11.toByte()).toByteArray()
//        var data2= arrayOf(0x41.toByte(),0x20.toByte(),0x80.toByte(),0x00.toByte(),0x00.toByte(),0x00.toByte()).toByteArray()
        var data1x=arrayOf(0x60.toByte(), 0x0A.toByte(),0x0A.toByte(),0x41.toByte(),0x6B.toByte(),0x10.toByte(),0x41.toByte(),0x41.toByte(),0x00.toByte(),0xbe.toByte(),0x5f.toByte(),0xb8.toByte(),0x11.toByte()).toByteArray()
        var data2x= arrayOf(0x60.toByte(), 0x0A.toByte(),0x0A.toByte(),0x41.toByte(),0x6B.toByte(),0x10.toByte(),0x41.toByte(),0x41.toByte(),0x20.toByte(),0x80.toByte(),0x00.toByte(),0x00.toByte(),0x00.toByte()).toByteArray()

        var datas= listOf<ByteArray>(
            data1x,data2x
        )

        var maskBuffer = ShortArray(32)
        mergePid(datas,maskBuffer,7)

        maskBuffer.forEach { println(it.toString()) }

        var produPid = produPid(maskBuffer)

        produPid.forEach {
            println(it.toString())
        }

    }


    @Test
    fun testConvert(){

//        val li:ArrayList<Short> = arrayListOf(0x41.toShort(), 0x05.toShort(),0x44.toShort())
//        val li:ArrayList<Short> = arrayListOf(0x41.toShort(), 0x0c.toShort(),0x00.toShort())
        val li:ArrayList<Short> = arrayListOf(0x41.toShort(), 0x0d.toShort(),0x00.toShort())

        val daAr=DataArray()
        daAr.array=li
        var temp = DataStream.commonCalcExpress("0x00,0x00,0x00,0x00,0x00,0x05,", daAr)
        println("temp=$temp")

    }


    @Test
    fun testConvert2(){

    }

    /**
     * 16进制转换成为string类型字符串
     * @param s
     * @return
     */
//    fun hexStringToString(s: String?): String? {
//        var s = s
//        if (s == null || s == "") {
//            return null
//        }
//        s = s.replace(" ", "")
//        val baKeyword = ByteArray(s.length / 2)
//        for (i in baKeyword.indices) {
//            try {
//                baKeyword[i] = (0xff and s.substring(i * 2, i * 2 + 2).toInt(16)).toByte()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//        try {
//            s = String(baKeyword, "UTF-8".toCharArray())
//            String()
//        } catch (e1: Exception) {
//            e1.printStackTrace()
//        }
//        return s
//    }

    @Test
    fun testSplite(){
        val data:Array<Int> = arrayOf(1,2,3,4,5,6,7,8)
        data.filterIndexed{
            index,_ -> index>3
        }.forEach { println(it) }
        data.filter { it>3 }.forEach { print(it) }
    }

    @Test
    fun testMIl(){
        var value=0x82 shr 7
        println(value)
    }


    /** 故障灯亮行驶距离  0x21 */
    @Test
    fun testDistance(){
//        0x06,0x07
        var v1=0x01
        var v2=0x01
        var dis = v1 * 256 + v2
        println(dis)
        Assert.assertEquals(dis,257)

    }

    /** PCBU转换
     *
     * */
    @Test
    fun testPcbu(){

        var header="P"

        var v1=0xd1
        var v2=0x00

        if(v1 in 0..64){
            header="P"
        }

        /** 0x40 ~ 0x80 */
        if(v1 in 63..128){
            header="C"
            v1= v1-0x40
        }

        if(v1 in 127..192){
            header="B"
            v1= v1-0x80
        }

        if(v1 >=0xc0){
            header="U"
            v1 -= 0xC0
        }

        println(header+" "+ String.format("%02X ",v1)+String.format("%02X ",v2))



    }


    @Test
    fun testAdd(){
        var a=0x01
        var result = a + 0x20
        println(String.format("%02X",result))
    }


    @Test
    fun testParseCan(){
           /** 08 07 E8 06 41 00 BE 1F A8 13 00  */
           var data: ByteArray = byteArrayOf(
               0x08,
               0x07,
               0xE8.toByte(),
               0x06,
               0x41,
               0x00,
               0xBE.toByte(),
               0x1f,
               0xA8.toByte(),
               0x13,
               0x00
           )


        var index=3+data[3]

        println(String.format("%02X ", data[index]))
        var flag = data[index] and 0x01
        println(String.format("%02X ", flag))

        Assert.assertEquals(flag,0x01.toByte())



    }

}