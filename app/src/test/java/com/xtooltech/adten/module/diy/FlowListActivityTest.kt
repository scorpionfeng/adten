package com.xtooltech.adten.module.diy
import com.xtooltech.adten.util.calcx
import com.xtooltech.adtenx.common.ble.ObdItem
import com.xtooltech.adtenx.util.hexString
import com.xtooltech.adtenx.util.toHex
import org.junit.Assert
import org.junit.Test
import kotlin.experimental.and

internal class FlowListActivityTestt{

    @Test
    fun test(){


//        var data1=arrayOf(0x41.toByte(),0x00.toByte(),0xbe.toByte(),0x5f.toByte(),0xb8.toByte(),0x11.toByte()).toByteArray()
//        var data2= arrayOf(0x41.toByte(),0x20.toByte(),0x80.toByte(),0x00.toByte(),0x00.toByte(),0x00.toByte()).toByteArray()
        var data1x=arrayOf(0x60.toByte(), 0x0A.toByte(),0x0A.toByte(),0x41.toByte(),0x6B.toByte(),0x10.toByte(),0x41.toByte(),0x41.toByte(),0x00.toByte(),0xbe.toByte(),0x5f.toByte(),0xb8.toByte(),0x11.toByte()).toByteArray()
        var data2x= arrayOf(0x60.toByte(), 0x0A.toByte(),0x0A.toByte(),0x41.toByte(),0x6B.toByte(),0x10.toByte(),0x41.toByte(),0x41.toByte(),0x20.toByte(),0x80.toByte(),0x00.toByte(),0x00.toByte(),0x00.toByte()).toByteArray()

        var datas= listOf<ByteArray>(
            data1x,data2x
        )

    }


    @Test
    fun testBitMask(){
        var a=1//0000 0001
        var b=2//0000 0010
        var c=4//0000 0100
        var d=8//0000 1000
        var e=16//0001 0000
        var f=32//0010 0000
        var g=64//0100 0000
        var h=128//1000 0000

        var x=6 //0000 0110
        println(x.and(a)>0)
        println(x.and(b)>0)
        println(x and c>0)
        println(x and d>0)
        println(x and e>0)
        println(x and f>0)
        println(x and g>0)
        println(x and h>0)
    }

    @Test
    fun testUnsign(){
        var a:Byte=0xff.toByte()
        var b:UByte=0xff.toUByte()
        println(a)
        println(b)
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
    fun getISOCrc(){

        //68 6A F1 01 00 C4

        var data= byteArrayOf(
           0x68,0x6a,0xf1.toByte(), 0x01,0x00
        )
        var sum: Byte = 0.toByte()
        for (element in data) {
            sum= sum.plus(element).toByte()
        }
        println(hexString(sum))

        Assert.assertEquals(sum,0xc4.toByte())

    }

    @Test
    fun parseVin(){
        var data= byteArrayOf(
            0x4C,0x53,0x47,0x55,0x44,0x38,0x32,0x43,0x37,0x36,0x45,0x30,0x32,0x36,0x37,0x30,0x35
        )
        var buffer=StringBuffer()
        var format = String.format("%s", data)
        println(format)
        data.forEach { buffer.append(String.format("%c",it)) }

        println(buffer.toString())

    }





    @Test
    fun testReadyStatus(){

        var bin_zero:Byte=1//0000 0001
        var bin_one:Byte=2//0000 0010
        var bin_two:Byte=4//0000 0100
        var bin_three:Byte=8//0000 1000
        var bin_four:Byte=16//0001 0000
        var bin_five:Byte=32//0010 0000
        var bin_six:Byte=64//0100 0000
        var bin_seven:Byte=128.toByte()//1000 0000

        var rawData= listOf(
            0x41,0x01, 0x85.toByte(),0x07,0x61,0x61
        )
        println(rawData.toHex())

        //41 01 85 07 61 61

        var data= listOf(
            ReadyItem("MIS_SUP","失火状态 Misfire monitoring",(if(rawData[3].and(bin_zero)>0) "ON" else "OFF"),0),
            ReadyItem("FUEL_SUP","燃油监视 fuel system monitoring",(if(rawData[3].and(bin_one)>0) "ON" else "OFF"),0),
            ReadyItem("CCM_SUP","综合部件监视 CCM",(if(rawData[3].and(bin_two)>0) "ON" else "OFF"),0),
            ReadyItem("TypeIgn","点火类型",(if(rawData[3].and(bin_three)>0) "汽油" else "柴油"),0),
            ReadyItem("MIS_RDY","失火监视",(if(rawData[3].and(bin_four)>0) "ON" else "OFF"),0),
            ReadyItem("FUEL_RDY","燃油系统监视",(if(rawData[3].and(bin_five)>0) "ON" else "OFF"),0),
            ReadyItem("CCM_RDY","综合部件监视",(if(rawData[3].and(bin_six)>0) "ON" else "OFF"),0),
            ReadyItem("Reserved1","Reserved1",(if(rawData[3].and(bin_seven)>0) "ON" else "OFF"),0),
            ReadyItem("CAT_SUP","催化器监视",(if(rawData[4].and(bin_zero)>0) "ON" else "OFF"),1),
            ReadyItem("HCAT_SUP","加热式催化器监视",(if(rawData[4].and(bin_one)>0) "ON" else "OFF"),1),
            ReadyItem("EVAP_SUP","燃油蒸气系统监视",(if(rawData[4].and(bin_two)>0) "ON" else "OFF"),1),
            ReadyItem("AIR_SUP","二次空气喷射监视",(if(rawData[4].and(bin_three)>0) "ON" else "OFF"),1),
            ReadyItem("SIgn.Reserved1","二次空气喷射监视",(if(rawData[4].and(bin_four)>0) "ON" else "OFF"),1),
            ReadyItem("O2S_SUP","氧传感器监视",(if(rawData[4].and(bin_five)>0) "ON" else "OFF"),1),
            ReadyItem("HTR_SUP","氧传感器加热监视",(if(rawData[4].and(bin_six)>0) "ON" else "OFF"),1),
            ReadyItem("EGR_RDY","废气再循环监视",(if(rawData[4].and(bin_seven)>0) "ON" else "OFF"),1),
            ReadyItem("HCCATSUP","催化剂监视",(if(rawData[4].and(bin_zero)>0) "ON" else "OFF"),2),
            ReadyItem("NCAT_SUP","氮氧化物后处理监视",(if(rawData[4].and(bin_one)>0) "ON" else "OFF"),2),
            ReadyItem("CIgn.Reserved1","CIgn.Reserved1",(if(rawData[4].and(bin_two)>0) "ON" else "OFF"),2),
            ReadyItem("BP_SUP","增压系统监视",(if(rawData[4].and(bin_three)>0) "ON" else "OFF"),2),
            ReadyItem("CIgn.Reserved2","CIgn.Reserved2",(if(rawData[4].and(bin_four)>0) "ON" else "OFF"),2),
            ReadyItem("EGS_SUP","废气传感器监视",(if(rawData[4].and(bin_five)>0) "ON" else "OFF"),2),
            ReadyItem("PM_SUP","颗粒物补集器监视",(if(rawData[4].and(bin_six)>0) "ON" else "OFF"),2),
            ReadyItem("EGR_SUP","废气再循环监视",(if(rawData[4].and(bin_seven)>0) "ON" else "OFF"),2),
            ReadyItem("HCCATSUP","NMHC催化剂监视",(if(rawData[5].and(bin_zero)>0) "ON" else "OFF"),2),
            ReadyItem("NCAT_RDY","氮氧化物后处理监视",(if(rawData[5].and(bin_one)>0) "ON" else "OFF"),2),
            ReadyItem("CIgn.Reserved3","---",(if(rawData[5].and(bin_two)>0) "ON" else "OFF"),2),
            ReadyItem("BP_RDY","增压系统监视",(if(rawData[5].and(bin_three)>0) "ON" else "OFF"),2),
            ReadyItem("CIgn.Reserved4","---",(if(rawData[5].and(bin_four)>0) "ON" else "OFF"),2),
            ReadyItem("EGS_RDY","废气传感器监视",(if(rawData[5].and(bin_five)>0) "ON" else "OFF"),2),
            ReadyItem("PM_RDY","颗粒物补集器监视",(if(rawData[5].and(bin_six)>0) "ON" else "OFF"),2),
            ReadyItem("EGR_RDY","废气再循环监视",(if(rawData[5].and(bin_seven)>0) "ON" else "OFF"),2)
        )

        println(data.toString())
    }




    @Test
    fun testI2B(){
        var i=100
        i=3203 //3072 + 131
        var h = (i shr 8).and(0xff)
        var l=i.and(0xff)
        println(h.toString()+">>"+String.format("%02X",h))
        println(l.toString()+">>"+String.format("%02X",l))
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
    fun parsePosition(){
        var a=0x01.toByte()
        var b=0x22.toByte()

        var c=  (a.toInt() shl 8).or(b.toInt())
        println(Integer.toHexString(c))
    }

    @Test
    fun parsetPCBU(){
        var tempCodeStr=""
        var codeLists= mutableListOf<String>()
      var data= listOf<Byte>(
          0x10,0x24,0x10,0x25,0x10,0x26,0x10,0x27
      )
        for (i in data.indices step 2){
            var codeHex= (data[i].toInt() shl 8).or(data[i+1].toInt())
            if (codeHex < 0x4000) { // P
                tempCodeStr = String.format("P%04X", codeHex)
            } else if (codeHex < 0x8000) { // C
                tempCodeStr = String.format("C%04X", codeHex - 0x4000)
            } else if (codeHex < 0xC000) { // B
                tempCodeStr = String.format("B%04X", codeHex - 0x8000)
            } else { // U
                tempCodeStr = String.format("U%04X", codeHex - 0xC000)
            }
            codeLists.add(tempCodeStr)
        }

        println(codeLists.toString())


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
        targetArr.dropLast(3)

    }


    @Test
    fun testArrayDrop(){
        val sourceArr = arrayOf("k", "o", "t", "l", "i", "n")
        var secArr= arrayOf<String>("a","b")
        var thiArr= arrayOf<String>("x","y","z")
        var droped = sourceArr.drop(3)

        thiArr.drop(1).apply { dropLast(1).apply { println(this) }}

        println(droped.toString())


    }

    @Test
    fun testPwmSlice(){
        val numbers2 = arrayOf(0x41,0x6B,0x10,0x41,0x00,0xBE,0x5F,0xB8,0x11,0x4F)
        println(numbers2.slice(3..numbers2.size-2))
    }

    @Test
    fun testCanSlice(){
        val data= arrayOf(0x08,0x07,0xE8,0x07,0x42,0x20,0x00,0x10,0x05,0xB0,0x15)
        println(data.contentToString())
        println(data.slice(4 .. data[3]+3))

    }

    @Test
    fun testAnd(){

        var flagA=30.toByte()
        var flagB= (-30).toByte()



        println(flagA and 0xff.toByte())

        println(flagB and 0xff.toByte())
    }



    @Test
    fun testPrint(){
        var arr= byteArrayOf(0x02,0x03,0x04,0x05,0x06,0x07,0x0c,0x0d,0x0e,0x0f,0x10,0x11,0x15,0x1f,0x20,0x24,0x2e,0x30,0x31,0x33,0x34,0x3c,0x3e,0x40,0x42,0x43,0x44,0x45,0x47,0x49,0x4a,0x4c,0x4e,0x53)
        println(arr.contentToString())
    }



    @Test
    fun testCalculation(){
        var method=::calcu1
        var data= byteArrayOf(0x41,0x04,0x00)
        var value = method.invoke(data)
        println(value)

    }

    fun calculation(item: ObdItem):String{
        var value=""
        var method:Any

        when(item.index){
            "0x00,0x00,0x01,0x10"-> method=::calcu1
            "0x00,0x00,0x01,0x11"-> method=::calcu2
        }

        return value
    }

    fun calcu1(data:ByteArray):String{
        return (data[2]*100.0/255).toString()
    }


    fun calcu3(data:ByteArray):String{
       return  String.format("%d", data[2] and 0x7F)
    }
    fun calcu4(data:ByteArray):String{
       return  String.format("%d",  data[2]*199.2/255-100)
    }
    fun calcu5(data:ByteArray):String{
       return  String.format("%d",  (data[2]*256+data[3])*2.0/65536)
    }
    fun calcu2(data:ByteArray):String{
         return if(data[2] and 0x80.toByte() ==0x01.toByte()) "MIL ON" else "MIL OFF"
    }

    @Test
    fun cacl6(){

        var content="(data[2]*256+data[3])?1:0"
        if(content.contains("&")){
            content=content.replace("&"," and ")
        }

        var data= byteArrayOf(0x03,0x03,0x03,0x05)

        var c = if((data[2]*256+data[3])==0x01) "a" else "b"
        println(c)

        var spliteContent = content.split("?")
        println(spliteContent.toString())
    }

    @Test
    fun printTemplte(){

        /** 构造默认值方法 */

        println("import com.xtooltech.adten.module.diy.ObdItem")
        println("import kotlin.experimental.and")
        println("import com.xtooltech.adtenx.common.ble.ObdItem")
        println("import com.xtooltech.adtenx.util.b2i")

        println("fun calcuEmpty(item:ObdItem):String{ return \"\"}")


        calcx.forEachIndexed{
            index,triple->

            var format=triple.second
            var data=triple.third

            if (data.contains("]")) {
                data=data.replace("]","].b2i() ")
            }

            if (data.contains(";")) {
                data=data.replace(";","")
            }
            if (data.contains("CHAR")) {
                data=data.replace("CHAR","data")
            }
            if (data.contains("BYTE")) {
                data=data.replace("BYTE","data")
            }
            println("// index= ${triple.first}")
            println("// raw =$data")
            if(data.contains("?")){
                //(data[2]*256+data[3])?1:0

                    println("fun calcu$index(data:List<Byte>):String{")
                    var contents = triple.second.split("|")

//                    //取index
//                    var indexValue=data.get(data.indexOf("[")+1).toString()
//                    //取& 下标
//                    var indexOfAnd = data.indexOf("&")
//                    //取位计算值
//                    var andValue = data.substring(indexOfAnd+1, indexOfAnd + 5)
//
//                    data.contains("&").trueLet {
//                        data =data.replace("&"," and ")
//                    }
//
//                    println("return if(data[$indexValue] and $andValue.toByte() == 0x01.toByte())  \"${contents[0]}\" else \"${contents[1]}\" ")
                if(data.contains("&")){
                    data=data.replace("&"," and ")
                }

                /** 替换0x** */
                if(data.contains("0x")){
                    var start=data.indexOf("0x")
                    var end=start+3
                    var hexValue=data.substring(start ..end)
                    data=data.replace(hexValue,"$hexValue")
                }
                var convertFlag=data.contains("*")
//                var convertContent=if(convertFlag) "" else ".toByte()"
                var head=if(convertFlag) "(" else ""

                var spliteContent = data.split(")?")

                println("return if $head ${spliteContent[0]} ==0x01 ) \"${contents[0]}\" else \"${contents[1]}\"")

                    println("}")
            }else if(data.contains("&")){


                println("fun calcu$index(data:List<Byte>):String{")
                //取index
                var indexValue=data.get(data.indexOf("[")+1).toString()


                //取& 下标
                var indexOfAnd = data.indexOf("&")

                //取位计算值
                var andValue = data.substring(indexOfAnd+1, indexOfAnd + 5)

                println("return  String.format(\"$format\", data[$indexValue] and $andValue) ")

                println("}")

            }else if(data.contains("-")){
                println("fun calcu$index(data:List<Byte>):String{")
                //BYTE[2]*199.2/255-100
                //(BYTE[3]*256+BYTE[4])*128.0/65536-128
                data = data.replace("BYTE", "data")
                println("return  String.format(\"$format\", $data)")
                println("}")

            }else if (data.contains("/")){

                //(BYTE[2]*256+BYTE[3])*2.0/65536;

                println("fun calcu$index(data:List<Byte>):String{")
                //BYTE[2]*199.2/255-100
                //(BYTE[3]*256+BYTE[4])*128.0/65536-128
                data = data.replace("BYTE", "data")
                println("return  String.format(\"$format\", $data)")
                println("}")

            }else if (data==""){
                println("fun calcu$index(data:List<Byte>):String{")
                //BYTE[2]*199.2/255-100
                //(BYTE[3]*256+BYTE[4])*128.0/65536-128
                println("return  \"\"")
                println("}")
            }else{
                //BYTE[2]*256+BYTE[3];
                println("fun calcu$index(data:List<Byte>):String{")
                //BYTE[2]*199.2/255-100
                //(BYTE[3]*256+BYTE[4])*128.0/65536-128
                data = data.replace("BYTE", "data")
                println("return  String.format(\"$format\", $data)")
                println("}")
            }

        }

        /** 构造入口方法 */
            println("fun calculation(item:ObdItem):String{")
            println("var value=\"\"")
            println("try{")
            println("when(item.index){")

        /** when构造 */
            calcx.forEachIndexed{ index,triple->
                println("\"${triple.first}\" -> value= calcu$index(item.obd)")
            }
            println("else -> { ::calcuEmpty}")
            println("}")
        println("}catch(e:Exception){")
        println("}")
        println("return value")
            println("}")

    }

    @Test
    fun getIndex(){
        var content="(BYTE[2]&0x80)?0:1;"
        var replace = content.replace("&", "and")
        println(content)
        println(replace)
    }


    @Test
    fun readDV(){
        //2F C6
        var a= byteArrayOf(0x2f,0xc6.toByte())
        a.forEach { println(String.format("%c",it)) }

    }


    @Test
    fun testMapIndex4(){
        var raw:Short=3065

        var i = raw.toInt() shr 8

        println(i.toByte())
    }

    @Test
    fun testConver(){

        var raw:Short=3065

        var format = String.format("%04x", raw)
        var first = format.substring(0, 2)
        var second=format.substring(2,4)

        println("0x$first,0x$second")



    }




}