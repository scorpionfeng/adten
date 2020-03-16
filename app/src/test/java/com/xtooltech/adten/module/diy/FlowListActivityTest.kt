package com.xtooltech.adten.module.diy
import com.xtooltech.adten.common.obd.DataArray
import com.xtooltech.adten.common.obd.DataStream
import com.xtooltech.adten.util.hexString
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


    /**
    Res:  08 07 E8 21 50 43 39 31 30 36 37
    Res:  08 07 E8 22 57 44 38 38 34 39 35

     */

    //0x00,0x00,0x00,0x57,0x44,0x42,0x32,0x31,0x31,0x30,0x36,0x31,0x35,0x41,0x32,0x36,0x31,0x37,0x35,0x39,
    //WDB2110615A261759
    @Test
    fun reverVin(){

        var data: ByteArray = byteArrayOf(
            0x49.toByte(),
            0x02.toByte(),
            0x01.toByte(),
            0x57.toByte(),
            0x42.toByte(),
            0x41.toByte(),
            0x50.toByte(),
            0x43.toByte(),
            0x39.toByte(),
            0x31.toByte(),
            0x30.toByte(),
            0x36.toByte(),
            0x37.toByte(),
            0x57.toByte(),
            0x44.toByte(),
            0x38.toByte(),
            0x38.toByte(),
            0x34.toByte(),
            0x39.toByte(),
            0x35.toByte()
        )

        data.forEach { print(java.lang.String.format("%c", it)) }

        println("------------------")

        var data2:ByteArray= byteArrayOf(
            0x00.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0x57.toByte(),
            0x44.toByte(),
            0x42.toByte(),
            0x32.toByte(),
            0x31.toByte(),
            0x31.toByte(),
            0x30.toByte(),
            0x36.toByte(),
            0x31.toByte(),
            0x35.toByte(),
            0x41.toByte(),
            0x32.toByte(),
            0x36.toByte(),
            0x31.toByte(),
            0x37.toByte(),
            0x35.toByte(),
            0x39.toByte()
        )
        data2.forEach { print(java.lang.String.format("%c", it)) }

        println(">>>>>>>>>>>>>>>")
        var x=0x09
        println(String.format("%02x",x))


//0x34,0x54,0x31,0x42,0x45,0x34,0x36,0x4b,0x34,0x38,0x55,0x37,0x33,0x39,0x30,0x34,0x33,

        //4T1BE46K48U739043
        var data3:ByteArray= byteArrayOf(
            0x34.toByte(),
            0x54.toByte(),
            0x31.toByte(),
            0x42.toByte(),
            0x45.toByte(),
            0x34.toByte(),
            0x36.toByte(),
            0x4b.toByte(),
            0x34.toByte(),
            0x38.toByte(),
            0x55.toByte(),
            0x37.toByte(),
            0x33.toByte(),
            0x39.toByte(),
            0x30.toByte(),
            0x34.toByte(),
            0x33.toByte()
        )
        println("&&&&&&&&&&&&&&&&&&&")
        data3.forEach { print(java.lang.String.format("%c", it))  }

    }


    @Test
    fun testTake(){
        val numbers = listOf("one", "two", "three", "four", "five", "six")
        println(numbers.take(3))
        println(numbers.takeLast(3))
        println(numbers.drop(1))
        println(numbers.dropLast(5))

        println("------------------------------------")

        val numbers2 = listOf("one", "two", "three", "four", "five", "six")
        println(numbers2.slice(1..3))
        println(numbers2.slice(0..4 step 2))
        println(numbers2.slice(setOf(3, 5, 0)))
    }

    @Test
    fun testParseTroble(){
        var result:ByteArray= byteArrayOf(
            0x08.toByte(),
            0x07.toByte(),
            0xe8.toByte(),
            0x06.toByte(),
            0x43.toByte(),
            0x02.toByte(),
            0x39.toByte(),
            0x39.toByte(),
            0x33.toByte(),
            0x34.toByte(),
            0x00.toByte()
        )
        var listData:MutableList<Byte> = mutableListOf()

        listData.addAll(result.takeLast(6))

        listData.forEach{print(hexString(it)+", ")}

    }

    @Test
    fun testArrayCopy(){
        val sourceArr = arrayOf("k", "o", "t", "l", "i", "n")
        var secArr= arrayOf<String>("a","b")
        var thiArr= arrayOf<String>("x","y","z")

        var targetArr= arrayOfNulls<String>(8)
        sourceArr.copyInto(targetArr)
        secArr.copyInto(targetArr,6)
        println(targetArr.contentToString())


        var newArr = sourceArr.plus(secArr)
        println("new->"+newArr.contentToString())
        var newXy = sourceArr.plus(thiArr.copyOfRange(0, 2))
        println("xy->"+newXy.contentToString())

        println("x"+sourceArr.contentToString())

    }


    @Test
    fun testPrint(){
        var arr= byteArrayOf(0x02,0x03,0x04,0x05,0x06,0x07,0x0c,0x0d,0x0e,0x0f,0x10,0x11,0x15,0x1f,0x20,0x24,0x2e,0x30,0x31,0x33,0x34,0x3c,0x3e,0x40,0x42,0x43,0x44,0x45,0x47,0x49,0x4a,0x4c,0x4e,0x53)
        println(arr.contentToString())
    }

}