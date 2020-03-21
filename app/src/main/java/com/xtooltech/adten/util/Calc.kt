import com.xtooltech.adten.module.diy.ObdItem
import kotlin.experimental.and
fun calcuEmpty(item:ObdItem):String{ return ""}
// index= 0x00,0x00,0x01,0x10
// raw =data[2]&0x7F
fun calcu0(data:List<Byte>):String{
    return  String.format("%d", data[2] and 0x7F)
}
// index= 0x00,0x00,0x01,0x11
// raw =(data[2]&0x80)?0:1
fun calcu1(data:List<Byte>):String{
    return if  (data[2] and 0x80.toByte() ==0x01.toByte() ) "MIL ON" else "MIL OFF"
}
// index= 0x00,0x00,0x01,0x20
// raw =(data[3]&0x01)?0:1
fun calcu2(data:List<Byte>):String{
    return if  (data[3] and 0x01.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x01,0x21
// raw =(data[3]&0x02)?0:1
fun calcu3(data:List<Byte>):String{
    return if  (data[3] and 0x02.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x01,0x22
// raw =(data[3]&0x04)?0:1
fun calcu4(data:List<Byte>):String{
    return if  (data[3] and 0x04.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x01,0x24
// raw =(data[3]&0x10)?0:1
fun calcu5(data:List<Byte>):String{
    return if  (data[3] and 0x10.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x01,0x25
// raw =(data[3]&0x20)?0:1
fun calcu6(data:List<Byte>):String{
    return if  (data[3] and 0x20.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x01,0x26
// raw =(data[3]&0x40)?0:1
fun calcu7(data:List<Byte>):String{
    return if  (data[3] and 0x40.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x01,0x30
// raw =(data[4]&0x01)?0:1
fun calcu8(data:List<Byte>):String{
    return if  (data[4] and 0x01.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x01,0x31
// raw =(data[4]&0x02)?0:1
fun calcu9(data:List<Byte>):String{
    return if  (data[4] and 0x02.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x01,0x32
// raw =(data[4]&0x04)?0:1
fun calcu10(data:List<Byte>):String{
    return if  (data[4] and 0x04.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x01,0x33
// raw =(data[4]&0x08)?0:1
fun calcu11(data:List<Byte>):String{
    return if  (data[4] and 0x08.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x01,0x34
// raw =(data[4]&0x10)?0:1
fun calcu12(data:List<Byte>):String{
    return if  (data[4] and 0x10.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x01,0x35
// raw =(data[4]&0x20)?0:1
fun calcu13(data:List<Byte>):String{
    return if  (data[4] and 0x20.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x01,0x36
// raw =(data[4]&0x40)?0:1
fun calcu14(data:List<Byte>):String{
    return if  (data[4] and 0x40.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x01,0x37
// raw =(data[4]&0x80)?0:1
fun calcu15(data:List<Byte>):String{
    return if  (data[4] and 0x80.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x01,0x40
// raw =(data[5]&0x01)?0:1
fun calcu16(data:List<Byte>):String{
    return if  (data[5] and 0x01.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x01,0x41
// raw =(data[5]&0x02)?0:1
fun calcu17(data:List<Byte>):String{
    return if  (data[5] and 0x02.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x01,0x42
// raw =(data[5]&0x04)?0:1
fun calcu18(data:List<Byte>):String{
    return if  (data[5] and 0x04.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x01,0x43
// raw =(data[5]&0x08)?0:1
fun calcu19(data:List<Byte>):String{
    return if  (data[5] and 0x08.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x01,0x44
// raw =(data[5]&0x10)?0:1
fun calcu20(data:List<Byte>):String{
    return if  (data[5] and 0x10.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x01,0x45
// raw =(data[5]&0x20)?0:1
fun calcu21(data:List<Byte>):String{
    return if  (data[5] and 0x20.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x01,0x46
// raw =(data[5]&0x40)?0:1
fun calcu22(data:List<Byte>):String{
    return if  (data[5] and 0x40.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x01,0x47
// raw =(data[5]&0x80)?0:1
fun calcu23(data:List<Byte>):String{
    return if  (data[5] and 0x80.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x02,0x00
// raw =
fun calcu24(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x00,0x03,0x00
// raw =
fun calcu25(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x00,0x03,0x10
// raw =
fun calcu26(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x00,0x04,0x00
// raw =data[2]*100.0/255
fun calcu27(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*100.0/255)
}
// index= 0x00,0x00,0x05,0x00
// raw =data[2]-40
fun calcu28(data:List<Byte>):String{
    return  String.format("%d", data[2]-40)
}
// index= 0x00,0x00,0x06,0x00
// raw =data[2]*199.2/255-100
fun calcu29(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*199.2/255-100)
}
// index= 0x00,0x00,0x06,0x10
// raw =data[2]*199.2/255-100
fun calcu30(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*199.2/255-100)
}
// index= 0x00,0x00,0x07,0x00
// raw =data[2]*199.2/255-100
fun calcu31(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*199.2/255-100)
}
// index= 0x00,0x00,0x07,0x10
// raw =data[2]*199.2/255-100
fun calcu32(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*199.2/255-100)
}
// index= 0x00,0x00,0x08,0x00
// raw =data[2]*199.2/255-100
fun calcu33(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*199.2/255-100)
}
// index= 0x00,0x00,0x08,0x10
// raw =data[2]*199.2/255-100
fun calcu34(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*199.2/255-100)
}
// index= 0x00,0x00,0x09,0x00
// raw =data[2]*199.2/255-100
fun calcu35(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*199.2/255-100)
}
// index= 0x00,0x00,0x09,0x10
// raw =data[2]*199.2/255-100
fun calcu36(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*199.2/255-100)
}
// index= 0x00,0x00,0x0A,0x00
// raw =data[2]*3
fun calcu37(data:List<Byte>):String{
    return  String.format("%d", data[2]*3)
}
// index= 0x00,0x00,0x0B,0x00
// raw =data[2]
fun calcu38(data:List<Byte>):String{
    return  String.format("%d", data[2])
}
// index= 0x00,0x00,0x0C,0x00
// raw =(data[2]*256+data[3])/4
fun calcu39(data:List<Byte>):String{
    return  String.format("%d", (data[2]*256+data[3])/4)
}
// index= 0x00,0x00,0x0D,0x00
// raw =data[2]
fun calcu40(data:List<Byte>):String{
    return  String.format("%d", data[2])
}
// index= 0x00,0x00,0x0E,0x00
// raw =data[2]/2.0-64
fun calcu41(data:List<Byte>):String{
    return  String.format("%2.1f", data[2]/2.0-64)
}
// index= 0x00,0x00,0x0F,0x00
// raw =data[2]-40
fun calcu42(data:List<Byte>):String{
    return  String.format("%d", data[2]-40)
}
// index= 0x00,0x00,0x10,0x00
// raw =(data[2]*256+data[3])/100.0
fun calcu43(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])/100.0)
}
// index= 0x00,0x00,0x11,0x00
// raw =data[2]*100.0/255
fun calcu44(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*100.0/255)
}
// index= 0x00,0x00,0x12,0x00
// raw =
fun calcu45(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x00,0x13,0x00
// raw =
fun calcu46(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x00,0x14,0x00
// raw =data[2]*0.005
fun calcu47(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x14,0x01
// raw =data[2]*0.005
fun calcu48(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x14,0x10
// raw =data[3]*199.2/255-100
fun calcu49(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x14,0x11
// raw =data[3]*199.2/255-100
fun calcu50(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x15,0x00
// raw =data[2]*0.005
fun calcu51(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x15,0x01
// raw =data[2]*0.005
fun calcu52(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x15,0x10
// raw =data[3]*199.2/255-100
fun calcu53(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x15,0x11
// raw =data[3]*199.2/255-100
fun calcu54(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x16,0x00
// raw =data[2]*0.005
fun calcu55(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x16,0x01
// raw =data[2]*0.005
fun calcu56(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x16,0x10
// raw =data[3]*199.2/255-100
fun calcu57(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x16,0x11
// raw =data[3]*199.2/255-100
fun calcu58(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x17,0x00
// raw =data[2]*0.005
fun calcu59(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x17,0x01
// raw =data[2]*0.005
fun calcu60(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x17,0x10
// raw =data[3]*199.2/255-100
fun calcu61(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x17,0x11
// raw =data[3]*199.2/255-100
fun calcu62(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x18,0x00
// raw =data[2]*0.005
fun calcu63(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x18,0x01
// raw =data[2]*0.005
fun calcu64(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x18,0x10
// raw =data[3]*199.2/255-100
fun calcu65(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x18,0x11
// raw =data[3]*199.2/255-100
fun calcu66(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x19,0x00
// raw =data[2]*0.005
fun calcu67(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x19,0x01
// raw =data[2]*0.005
fun calcu68(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x19,0x10
// raw =data[3]*199.2/255-100
fun calcu69(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x19,0x11
// raw =data[3]*199.2/255-100
fun calcu70(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x1A,0x00
// raw =data[2]*0.005
fun calcu71(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x1A,0x01
// raw =data[2]*0.005
fun calcu72(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x1A,0x10
// raw =data[3]*199.2/255-100
fun calcu73(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x1A,0x11
// raw =data[3]*199.2/255-100
fun calcu74(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x1B,0x00
// raw =data[2]*0.005
fun calcu75(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x1B,0x01
// raw =data[2]*0.005
fun calcu76(data:List<Byte>):String{
    return  String.format("%1.3f", data[2]*0.005)
}
// index= 0x00,0x00,0x1B,0x10
// raw =data[3]*199.2/255-100
fun calcu77(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x1B,0x11
// raw =data[3]*199.2/255-100
fun calcu78(data:List<Byte>):String{
    return  String.format("%3.1f", data[3]*199.2/255-100)
}
// index= 0x00,0x00,0x1C,0x00
// raw =
fun calcu79(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x00,0x1D,0x00
// raw =
fun calcu80(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x00,0x1E,0x00
// raw =
fun calcu81(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x00,0x1F,0x00
// raw =data[2]*256+data[3]
fun calcu82(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x00,0x21,0x00
// raw =data[2]*256+data[3]
fun calcu83(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x00,0x22,0x00
// raw =(data[2]*256+data[3])*0.079
fun calcu84(data:List<Byte>):String{
    return  String.format("%4.3f", (data[2]*256+data[3])*0.079)
}
// index= 0x00,0x00,0x23,0x00
// raw =(data[2]*256+data[3])*10
fun calcu85(data:List<Byte>):String{
    return  String.format("%d", (data[2]*256+data[3])*10)
}
// index= 0x00,0x00,0x24,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu86(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x24,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu87(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x24,0x20
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu88(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x24,0x21
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu89(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x25,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu90(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x25,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu91(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x25,0x20
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu92(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x25,0x21
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu93(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x26,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu94(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x26,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu95(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x26,0x20
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu96(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x26,0x21
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu97(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x27,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu98(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x27,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu99(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x27,0x20
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu100(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x27,0x21
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu101(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x28,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu102(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x28,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu103(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x28,0x20
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu104(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x28,0x21
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu105(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x29,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu106(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x29,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu107(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x29,0x20
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu108(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x29,0x21
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu109(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x2A,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu110(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x2A,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu111(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x2A,0x20
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu112(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x2A,0x21
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu113(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x2B,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu114(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x2B,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu115(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x2B,0x20
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu116(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x2B,0x21
// raw =(data[4]*256+data[5])*8.0/65536
fun calcu117(data:List<Byte>):String{
    return  String.format("%1.3f", (data[4]*256+data[5])*8.0/65536)
}
// index= 0x00,0x00,0x2C,0x00
// raw =data[2]*100.0/255
fun calcu118(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*100.0/255)
}
// index= 0x00,0x00,0x2D,0x00
// raw =data[2]*199.2/255-100
fun calcu119(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*199.2/255-100)
}
// index= 0x00,0x00,0x2E,0x00
// raw =data[2]*100.0/255
fun calcu120(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*100.0/255)
}
// index= 0x00,0x00,0x2F,0x00
// raw =data[2]*100.0/255
fun calcu121(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*100.0/255)
}
// index= 0x00,0x00,0x30,0x00
// raw =data[2]
fun calcu122(data:List<Byte>):String{
    return  String.format("%d", data[2])
}
// index= 0x00,0x00,0x31,0x00
// raw =data[2]*256+data[3]
fun calcu123(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x00,0x32,0x00
// raw =(data[2]*256+data[3])*0.25
fun calcu124(data:List<Byte>):String{
    return  String.format("%4.2f", (data[2]*256+data[3])*0.25)
}
// index= 0x00,0x00,0x33,0x00
// raw =data[2]
fun calcu125(data:List<Byte>):String{
    return  String.format("%d", data[2])
}
// index= 0x00,0x00,0x34,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu126(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x34,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu127(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x34,0x20
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu128(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x34,0x21
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu129(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x35,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu130(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x35,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu131(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x35,0x20
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu132(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x35,0x21
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu133(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x36,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu134(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x36,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu135(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x36,0x20
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu136(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x36,0x21
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu137(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x37,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu138(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x37,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu139(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x37,0x20
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu140(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x37,0x21
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu141(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x38,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu142(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x38,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu143(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x38,0x20
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu144(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x38,0x21
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu145(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x39,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu146(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x39,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu147(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x39,0x20
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu148(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x39,0x21
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu149(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x3A,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu150(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x3A,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu151(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x3A,0x20
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu152(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x3A,0x21
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu153(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x3B,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu154(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x3B,0x01
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu155(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x3B,0x20
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu156(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x3B,0x21
// raw =(data[3]*256+data[4])*128.0/65536-128
fun calcu157(data:List<Byte>):String{
    return  String.format("%3.3f", (data[3]*256+data[4])*128.0/65536-128)
}
// index= 0x00,0x00,0x3C,0x00
// raw =(data[2]*256+data[3])*0.1-40
fun calcu158(data:List<Byte>):String{
    return  String.format("%4.1f", (data[2]*256+data[3])*0.1-40)
}
// index= 0x00,0x00,0x3D,0x00
// raw =(data[2]*256+data[3])*0.1-40
fun calcu159(data:List<Byte>):String{
    return  String.format("%4.1f", (data[2]*256+data[3])*0.1-40)
}
// index= 0x00,0x00,0x3E,0x00
// raw =(data[2]*256+data[3])*0.1-40
fun calcu160(data:List<Byte>):String{
    return  String.format("%4.1f", (data[2]*256+data[3])*0.1-40)
}
// index= 0x00,0x00,0x3F,0x00
// raw =(data[2]*256+data[3])*0.1-40
fun calcu161(data:List<Byte>):String{
    return  String.format("%4.1f", (data[2]*256+data[3])*0.1-40)
}
// index= 0x00,0x00,0x41,0x20
// raw =(data[3]&0x01)?0:1
fun calcu162(data:List<Byte>):String{
    return if  (data[3] and 0x01.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x21
// raw =(data[3]&0x02)?0:1
fun calcu163(data:List<Byte>):String{
    return if  (data[3] and 0x02.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x22
// raw =(data[3]&0x04)?0:1
fun calcu164(data:List<Byte>):String{
    return if  (data[3] and 0x04.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x24
// raw =(data[3]&0x10)?0:1
fun calcu165(data:List<Byte>):String{
    return if  (data[3] and 0x10.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x41,0x25
// raw =(data[3]&0x20)?0:1
fun calcu166(data:List<Byte>):String{
    return if  (data[3] and 0x20.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x41,0x26
// raw =(data[3]&0x40)?0:1
fun calcu167(data:List<Byte>):String{
    return if  (data[3] and 0x40.toByte() ==0x01.toByte() ) "NO" else "YES"
}
// index= 0x00,0x00,0x41,0x30
// raw =(data[4]&0x01)?0:1
fun calcu168(data:List<Byte>):String{
    return if  (data[4] and 0x01.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x31
// raw =(data[4]&0x02)?0:1
fun calcu169(data:List<Byte>):String{
    return if  (data[4] and 0x02.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x32
// raw =(data[4]&0x04)?0:1
fun calcu170(data:List<Byte>):String{
    return if  (data[4] and 0x04.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x33
// raw =(data[4]&0x08)?0:1
fun calcu171(data:List<Byte>):String{
    return if  (data[4] and 0x08.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x34
// raw =(data[4]&0x10)?0:1
fun calcu172(data:List<Byte>):String{
    return if  (data[4] and 0x10.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x35
// raw =(data[4]&0x20)?0:1
fun calcu173(data:List<Byte>):String{
    return if  (data[4] and 0x20.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x36
// raw =(data[4]&0x40)?0:1
fun calcu174(data:List<Byte>):String{
    return if  (data[4] and 0x40.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x37
// raw =(data[4]&0x80)?0:1
fun calcu175(data:List<Byte>):String{
    return if  (data[4] and 0x80.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x40
// raw =(data[4]&0x01)?0:1
fun calcu176(data:List<Byte>):String{
    return if  (data[4] and 0x01.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x41
// raw =(data[4]&0x02)?0:1
fun calcu177(data:List<Byte>):String{
    return if  (data[4] and 0x02.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x42
// raw =(data[4]&0x04)?0:1
fun calcu178(data:List<Byte>):String{
    return if  (data[4] and 0x04.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x43
// raw =(data[4]&0x08)?0:1
fun calcu179(data:List<Byte>):String{
    return if  (data[4] and 0x08.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x44
// raw =(data[4]&0x10)?0:1
fun calcu180(data:List<Byte>):String{
    return if  (data[4] and 0x10.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x45
// raw =(data[4]&0x20)?0:1
fun calcu181(data:List<Byte>):String{
    return if  (data[4] and 0x20.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x46
// raw =(data[4]&0x40)?0:1
fun calcu182(data:List<Byte>):String{
    return if  (data[4] and 0x40.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x41,0x47
// raw =(data[4]&0x80)?0:1
fun calcu183(data:List<Byte>):String{
    return if  (data[4] and 0x80.toByte() ==0x01.toByte() ) "YES" else "NO"
}
// index= 0x00,0x00,0x42,0x00
// raw =(data[2]*256+data[3])*0.001
fun calcu184(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])*0.001)
}
// index= 0x00,0x00,0x43,0x00
// raw =(data[2]*256+data[3])*100/255.0
fun calcu185(data:List<Byte>):String{
    return  String.format("%3.1f", (data[2]*256+data[3])*100/255.0)
}
// index= 0x00,0x00,0x44,0x00
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu186(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x00,0x45,0x00
// raw =data[2]*100.0/255
fun calcu187(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*100.0/255)
}
// index= 0x00,0x00,0x46,0x00
// raw =data[2]-40
fun calcu188(data:List<Byte>):String{
    return  String.format("%d", data[2]-40)
}
// index= 0x00,0x00,0x47,0x00
// raw =data[2]*100.0/255
fun calcu189(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*100.0/255)
}
// index= 0x00,0x00,0x48,0x00
// raw =data[2]*100.0/255
fun calcu190(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*100.0/255)
}
// index= 0x00,0x00,0x49,0x00
// raw =data[2]*100.0/255
fun calcu191(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*100.0/255)
}
// index= 0x00,0x00,0x4A,0x00
// raw =data[2]*100.0/255
fun calcu192(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*100.0/255)
}
// index= 0x00,0x00,0x4B,0x00
// raw =data[2]*100.0/255
fun calcu193(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*100.0/255)
}
// index= 0x00,0x00,0x4C,0x00
// raw =data[2]*100.0/255
fun calcu194(data:List<Byte>):String{
    return  String.format("%3.1f", data[2]*100.0/255)
}
// index= 0x00,0x00,0x4D,0x00
// raw =data[2]*256+data[3]
fun calcu195(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x00,0x4E,0x00
// raw =data[2]*256+data[3]
fun calcu196(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x06,0x12,0x01
// raw =data[2]*256+data[3]
fun calcu197(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x06,0x12,0x02
// raw =(data[2]*256+data[3])/10.0
fun calcu198(data:List<Byte>):String{
    return  String.format("%4.1f", (data[2]*256+data[3])/10.0)
}
// index= 0x00,0x06,0x12,0x03
// raw =(data[2]*256+data[3])/100.0
fun calcu199(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])/100.0)
}
// index= 0x00,0x06,0x12,0x04
// raw =(data[2]*256+data[3])/1000.0
fun calcu200(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])/1000.0)
}
// index= 0x00,0x06,0x12,0x05
// raw =(data[2]*256+data[3])*256.0/65536
fun calcu201(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*256.0/65536)
}
// index= 0x00,0x06,0x12,0x06
// raw =(data[2]*256+data[3])*20.0/65536
fun calcu202(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])*20.0/65536)
}
// index= 0x00,0x06,0x12,0x07
// raw =(data[2]*256+data[3])*0.25+0.5
fun calcu203(data:List<Byte>):String{
    return  String.format("%5.0f", (data[2]*256+data[3])*0.25+0.5)
}
// index= 0x00,0x06,0x12,0x08
// raw =(data[2]*256+data[3])/100.0
fun calcu204(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])/100.0)
}
// index= 0x00,0x06,0x12,0x09
// raw =data[2]*256+data[3]
fun calcu205(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x06,0x12,0x0A
// raw =(data[2]*256+data[3])*8.0/65536
fun calcu206(data:List<Byte>):String{
    return  String.format("%1.4f", (data[2]*256+data[3])*8.0/65536)
}
// index= 0x00,0x06,0x12,0x0B
// raw =(data[2]*256+data[3])/1000.0
fun calcu207(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])/1000.0)
}
// index= 0x00,0x06,0x12,0x0C
// raw =(data[2]*256+data[3])/100.0
fun calcu208(data:List<Byte>):String{
    return  String.format("%3.3f", (data[2]*256+data[3])/100.0)
}
// index= 0x00,0x06,0x12,0x0D
// raw =(data[2]*256+data[3])*256.0/65536
fun calcu209(data:List<Byte>):String{
    return  String.format("%3.3f", (data[2]*256+data[3])*256.0/65536)
}
// index= 0x00,0x06,0x12,0x0E
// raw =(data[2]*256+data[3])/1000.0
fun calcu210(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])/1000.0)
}
// index= 0x00,0x06,0x12,0x0F
// raw =(data[2]*256+data[3])/100.0
fun calcu211(data:List<Byte>):String{
    return  String.format("%3.3f", (data[2]*256+data[3])/100.0)
}
// index= 0x00,0x06,0x12,0x10
// raw =(data[2]*256+data[3])/1000.0
fun calcu212(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])/1000.0)
}
// index= 0x00,0x06,0x12,0x11
// raw =(data[2]*256+data[3])/10.0
fun calcu213(data:List<Byte>):String{
    return  String.format("%4.1f", (data[2]*256+data[3])/10.0)
}
// index= 0x00,0x06,0x12,0x12
// raw =data[2]*256+data[3]
fun calcu214(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x06,0x12,0x13
// raw =(data[2]*256+data[3])/1000.0
fun calcu215(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])/1000.0)
}
// index= 0x00,0x06,0x12,0x14
// raw =(data[2]*256+data[3])/1000.0
fun calcu216(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])/1000.0)
}
// index= 0x00,0x06,0x12,0x15
// raw =data[2]*256+data[3]
fun calcu217(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x06,0x12,0x16
// raw =(data[2]*256+data[3]-40)/10.0
fun calcu218(data:List<Byte>):String{
    return  String.format("%4.1f", (data[2]*256+data[3]-40)/10.0)
}
// index= 0x00,0x06,0x12,0x17
// raw =(data[2]*256+data[3])/100.0
fun calcu219(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])/100.0)
}
// index= 0x00,0x06,0x12,0x18
// raw =(data[2]*256+data[3])*0.0117
fun calcu220(data:List<Byte>):String{
    return  String.format("%3.3f", (data[2]*256+data[3])*0.0117)
}
// index= 0x00,0x06,0x12,0x19
// raw =(data[2]*256+data[3])*0.079
fun calcu221(data:List<Byte>):String{
    return  String.format("%4.3f", (data[2]*256+data[3])*0.079)
}
// index= 0x00,0x06,0x12,0x1A
// raw =data[2]*256+data[3]
fun calcu222(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x06,0x12,0x1B
// raw =(data[2]*256+data[3])*10
fun calcu223(data:List<Byte>):String{
    return  String.format("%d", (data[2]*256+data[3])*10)
}
// index= 0x00,0x06,0x12,0x1C
// raw =(data[2]*256+data[3])/100.0
fun calcu224(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])/100.0)
}
// index= 0x00,0x06,0x12,0x1D
// raw =(data[2]*256+data[3])*0.5
fun calcu225(data:List<Byte>):String{
    return  String.format("%5.1f", (data[2]*256+data[3])*0.5)
}
// index= 0x00,0x06,0x12,0x1E
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu226(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x06,0x12,0x1F
// raw =(data[2]*256+data[3])*0.05
fun calcu227(data:List<Byte>):String{
    return  String.format("%4.2f", (data[2]*256+data[3])*0.05)
}
// index= 0x00,0x06,0x12,0x20
// raw =(data[2]*256+data[3])*256.0/65536
fun calcu228(data:List<Byte>):String{
    return  String.format("%3.3f", (data[2]*256+data[3])*256.0/65536)
}
// index= 0x00,0x06,0x12,0x21
// raw =(data[2]*256+data[3])/1000
fun calcu229(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])/1000)
}
// index= 0x00,0x06,0x12,0x22
// raw =data[2]*256+data[3]
fun calcu230(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x06,0x12,0x23
// raw =(data[2]*256+data[3])/1000.0
fun calcu231(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])/1000.0)
}
// index= 0x00,0x06,0x12,0x24
// raw =data[2]*256+data[3]
fun calcu232(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x06,0x12,0x25
// raw =data[2]*256+data[3]
fun calcu233(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x06,0x12,0x26
// raw =(data[2]*256+data[3])/10000
fun calcu234(data:List<Byte>):String{
    return  String.format("%2.4f", (data[2]*256+data[3])/10000)
}
// index= 0x00,0x06,0x12,0x27
// raw =(data[2]*256+data[3])/100.0
fun calcu235(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])/100.0)
}
// index= 0x00,0x06,0x12,0x28
// raw =data[2]*256+data[3]
fun calcu236(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x06,0x12,0x29
// raw =(data[2]*256+data[3])16.384/65535
fun calcu237(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])*16.384/65535)
}
// index= 0x00,0x06,0x12,0x2A
// raw =(data[2]*256+data[3])/1000.0
fun calcu238(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])/1000.0)
}
// index= 0x00,0x06,0x12,0x2B
// raw =data[2]*256+data[3]
fun calcu239(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x06,0x12,0x2C
// raw =(data[2]*256+data[3])/100.0
fun calcu240(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])/100.0)
}
// index= 0x00,0x06,0x12,0x2D
// raw =(data[2]*256+data[3])*100.0
fun calcu241(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])*100.0)
}
// index= 0x00,0x06,0x12,0x2E
// raw =(data[2]*256+data[3])?1:0
fun calcu242(data:List<Byte>):String{
    return if ( (data[2]*256+data[3]) ==0x01 ) "false" else "true"
}
// index= 0x00,0x06,0x12,0x2F
// raw =(data[2]*256+data[3])/100.0
fun calcu243(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])/100.0)
}
// index= 0x00,0x06,0x12,0x30
// raw =(data[2]*256+data[3])*100.0/65535
fun calcu244(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])*100.0/65535)
}
// index= 0x00,0x06,0x12,0x31
// raw =(data[2]*256+data[3])/1000.0
fun calcu245(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])/1000.0)
}
// index= 0x00,0x06,0x12,0x32
// raw =(data[2]*256+data[3])*2.0/65536
fun calcu246(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])*2.0/65536)
}
// index= 0x00,0x06,0x12,0x33
// raw =(data[2]*256+data[3])*16.0/65535
fun calcu247(data:List<Byte>):String{
    return  String.format("%2.2f", (data[2]*256+data[3])*16.0/65535)
}
// index= 0x00,0x06,0x12,0x34
// raw =
fun calcu248(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x35
// raw =
fun calcu249(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x36
// raw =
fun calcu250(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x37
// raw =
fun calcu251(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x38
// raw =
fun calcu252(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x39
// raw =
fun calcu253(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x3A
// raw =
fun calcu254(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x3B
// raw =
fun calcu255(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x3C
// raw =
fun calcu256(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x3D
// raw =
fun calcu257(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x3E
// raw =
fun calcu258(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x3F
// raw =
fun calcu259(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x40
// raw =
fun calcu260(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x41
// raw =
fun calcu261(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x42
// raw =
fun calcu262(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x43
// raw =
fun calcu263(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x44
// raw =
fun calcu264(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x45
// raw =
fun calcu265(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x46
// raw =
fun calcu266(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x47
// raw =
fun calcu267(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x48
// raw =
fun calcu268(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x49
// raw =
fun calcu269(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x4A
// raw =
fun calcu270(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x4B
// raw =
fun calcu271(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x4C
// raw =
fun calcu272(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x4D
// raw =
fun calcu273(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x4E
// raw =
fun calcu274(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x4F
// raw =
fun calcu275(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x50
// raw =
fun calcu276(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x51
// raw =
fun calcu277(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x52
// raw =
fun calcu278(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x53
// raw =
fun calcu279(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x54
// raw =
fun calcu280(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x55
// raw =
fun calcu281(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x56
// raw =
fun calcu282(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x57
// raw =
fun calcu283(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x58
// raw =
fun calcu284(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x59
// raw =
fun calcu285(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x5A
// raw =
fun calcu286(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x5B
// raw =
fun calcu287(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x5C
// raw =
fun calcu288(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x5D
// raw =
fun calcu289(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x5E
// raw =
fun calcu290(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x5F
// raw =
fun calcu291(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x60
// raw =
fun calcu292(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x61
// raw =
fun calcu293(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x62
// raw =
fun calcu294(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x63
// raw =
fun calcu295(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x64
// raw =
fun calcu296(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x65
// raw =
fun calcu297(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x66
// raw =
fun calcu298(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x67
// raw =
fun calcu299(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x68
// raw =
fun calcu300(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x69
// raw =
fun calcu301(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x6A
// raw =
fun calcu302(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x6B
// raw =
fun calcu303(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x6C
// raw =
fun calcu304(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x6D
// raw =
fun calcu305(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x6E
// raw =
fun calcu306(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x6F
// raw =
fun calcu307(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x70
// raw =
fun calcu308(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x71
// raw =
fun calcu309(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x72
// raw =
fun calcu310(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x73
// raw =
fun calcu311(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x74
// raw =
fun calcu312(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x75
// raw =
fun calcu313(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x76
// raw =
fun calcu314(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x77
// raw =
fun calcu315(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x78
// raw =
fun calcu316(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x79
// raw =
fun calcu317(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x7A
// raw =
fun calcu318(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x7B
// raw =
fun calcu319(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x7C
// raw =
fun calcu320(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x7D
// raw =
fun calcu321(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x7E
// raw =
fun calcu322(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x7F
// raw =
fun calcu323(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x80
// raw =
fun calcu324(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x81
// raw =data[2]*256+data[3]
fun calcu325(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x06,0x12,0x82
// raw =(data[2]*256+data[3])*0.1
fun calcu326(data:List<Byte>):String{
    return  String.format("%4.1f", (data[2]*256+data[3])*0.1)
}
// index= 0x00,0x06,0x12,0x83
// raw =(data[2]*256+data[3])*0.01
fun calcu327(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])*0.01)
}
// index= 0x00,0x06,0x12,0x84
// raw =(data[2]*256+data[3])*0.001
fun calcu328(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])*0.001)
}
// index= 0x00,0x06,0x12,0x85
// raw =(data[2]*256+data[3])*0.999/32768
fun calcu329(data:List<Byte>):String{
    return  String.format("%1.3f", (data[2]*256+data[3])*0.999/32768)
}
// index= 0x00,0x06,0x12,0x86
// raw =(data[2]*256+data[3])*9.99424/32768
fun calcu330(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])*9.99424/32768)
}
// index= 0x00,0x06,0x12,0x87
// raw =
fun calcu331(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x88
// raw =
fun calcu332(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x89
// raw =
fun calcu333(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x8A
// raw =(data[2]*256+data[3])*3.997696/32768
fun calcu334(data:List<Byte>):String{
    return  String.format("%1.4f", (data[2]*256+data[3])*3.997696/32768)
}
// index= 0x00,0x06,0x12,0x8B
// raw =(data[2]*256+data[3])*0.001
fun calcu335(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])*0.001)
}
// index= 0x00,0x06,0x12,0x8C
// raw =(data[2]*256+data[3])*0.01
fun calcu336(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])*0.01)
}
// index= 0x00,0x06,0x12,0x8D
// raw =(data[2]*256+data[3])*128.0/32768
fun calcu337(data:List<Byte>):String{
    return  String.format("%3.3f", (data[2]*256+data[3])*128.0/32768)
}
// index= 0x00,0x06,0x12,0x8E
// raw =(data[2]*256+data[3])*0.001
fun calcu338(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])*0.001)
}
// index= 0x00,0x06,0x12,0x8F
// raw =
fun calcu339(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x90
// raw =(data[2]*256+data[3])*0.001
fun calcu340(data:List<Byte>):String{
    return  String.format("%2.3f", (data[2]*256+data[3])*0.001)
}
// index= 0x00,0x06,0x12,0x91
// raw =
fun calcu341(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x92
// raw =
fun calcu342(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x93
// raw =
fun calcu343(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x94
// raw =
fun calcu344(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x95
// raw =
fun calcu345(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x96
// raw =(data[2]*256+data[3])*0.1
fun calcu346(data:List<Byte>):String{
    return  String.format("%4.1f", (data[2]*256+data[3])*0.1)
}
// index= 0x00,0x06,0x12,0x97
// raw =
fun calcu347(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x98
// raw =
fun calcu348(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x99
// raw =
fun calcu349(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x9A
// raw =
fun calcu350(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x9B
// raw =
fun calcu351(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x9C
// raw =(data[2]*256+data[3])*0.01
fun calcu352(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])*0.01)
}
// index= 0x00,0x06,0x12,0x9D
// raw =(data[2]*256+data[3])*0.5
fun calcu353(data:List<Byte>):String{
    return  String.format("%5.1f", (data[2]*256+data[3])*0.5)
}
// index= 0x00,0x06,0x12,0x9E
// raw =
fun calcu354(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0x9F
// raw =
fun calcu355(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xA0
// raw =
fun calcu356(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xA1
// raw =
fun calcu357(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xA2
// raw =
fun calcu358(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xA3
// raw =
fun calcu359(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xA4
// raw =
fun calcu360(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xA5
// raw =
fun calcu361(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xA6
// raw =
fun calcu362(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xA7
// raw =
fun calcu363(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xA8
// raw =data[2]*256+data[3]
fun calcu364(data:List<Byte>):String{
    return  String.format("%d", data[2]*256+data[3])
}
// index= 0x00,0x06,0x12,0xA9
// raw =(data[2]*256+data[3])*0.25
fun calcu365(data:List<Byte>):String{
    return  String.format("%4.2f", (data[2]*256+data[3])*0.25)
}
// index= 0x00,0x06,0x12,0xAA
// raw =
fun calcu366(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xAB
// raw =
fun calcu367(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xAC
// raw =
fun calcu368(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xAD
// raw =
fun calcu369(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xAE
// raw =
fun calcu370(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xAF
// raw =(data[2]*256+data[3])*0.01
fun calcu371(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])*0.01)
}
// index= 0x00,0x06,0x12,0xB0
// raw =(data[2]*256+data[3])*100.007936/32768
fun calcu372(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])*100.007936/32768)
}
// index= 0x00,0x06,0x12,0xB1
// raw =
fun calcu373(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xB2
// raw =
fun calcu374(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xB3
// raw =
fun calcu375(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xB4
// raw =
fun calcu376(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xB5
// raw =
fun calcu377(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xB6
// raw =
fun calcu378(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xB7
// raw =
fun calcu379(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xB8
// raw =
fun calcu380(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xB9
// raw =
fun calcu381(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xBA
// raw =
fun calcu382(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xBB
// raw =
fun calcu383(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xBC
// raw =
fun calcu384(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xBD
// raw =
fun calcu385(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xBE
// raw =
fun calcu386(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xBF
// raw =
fun calcu387(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xC0
// raw =
fun calcu388(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xC1
// raw =
fun calcu389(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xC2
// raw =
fun calcu390(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xC3
// raw =
fun calcu391(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xC4
// raw =
fun calcu392(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xC5
// raw =
fun calcu393(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xC6
// raw =
fun calcu394(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xC7
// raw =
fun calcu395(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xC8
// raw =
fun calcu396(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xC9
// raw =
fun calcu397(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xCA
// raw =
fun calcu398(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xCB
// raw =
fun calcu399(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xCC
// raw =
fun calcu400(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xCD
// raw =
fun calcu401(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xCE
// raw =
fun calcu402(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xCF
// raw =
fun calcu403(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xD0
// raw =
fun calcu404(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xD1
// raw =
fun calcu405(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xD2
// raw =
fun calcu406(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xD3
// raw =
fun calcu407(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xD4
// raw =
fun calcu408(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xD5
// raw =
fun calcu409(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xD6
// raw =
fun calcu410(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xD7
// raw =
fun calcu411(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xD8
// raw =
fun calcu412(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xD9
// raw =
fun calcu413(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xDA
// raw =
fun calcu414(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xDB
// raw =
fun calcu415(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xDC
// raw =
fun calcu416(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xDD
// raw =
fun calcu417(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xDE
// raw =
fun calcu418(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xDF
// raw =
fun calcu419(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xE0
// raw =
fun calcu420(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xE1
// raw =
fun calcu421(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xE2
// raw =
fun calcu422(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xE3
// raw =
fun calcu423(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xE4
// raw =
fun calcu424(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xE5
// raw =
fun calcu425(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xE6
// raw =
fun calcu426(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xE7
// raw =
fun calcu427(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xE8
// raw =
fun calcu428(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xE9
// raw =
fun calcu429(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xEA
// raw =
fun calcu430(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xEB
// raw =
fun calcu431(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xEC
// raw =
fun calcu432(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xED
// raw =
fun calcu433(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xEE
// raw =
fun calcu434(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xEF
// raw =
fun calcu435(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xF0
// raw =
fun calcu436(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xF1
// raw =
fun calcu437(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xF2
// raw =
fun calcu438(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xF3
// raw =
fun calcu439(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xF4
// raw =
fun calcu440(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xF5
// raw =
fun calcu441(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xF6
// raw =
fun calcu442(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xF7
// raw =
fun calcu443(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xF8
// raw =
fun calcu444(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xF9
// raw =
fun calcu445(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xFA
// raw =
fun calcu446(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xFB
// raw =
fun calcu447(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xFC
// raw =
fun calcu448(data:List<Byte>):String{
    return  ""
}
// index= 0x00,0x06,0x12,0xFD
// raw =(data[2]*256+data[3])*0.001
fun calcu449(data:List<Byte>):String{
    return  String.format("%3.2f", (data[2]*256+data[3])*0.001)
}
// index= 0x00,0x06,0x12,0xFE
// raw =(data[2]*256+data[3])*0.25
fun calcu450(data:List<Byte>):String{
    return  String.format("%4.2f", (data[2]*256+data[3])*0.25)
}
// index= 0x00,0x06,0x12,0xFF
// raw =
fun calcu451(data:List<Byte>):String{
    return  ""
}
fun calculation(item:ObdItem):String{
    var value=""
    when(item.index){
        "0x00,0x00,0x01,0x10" -> value= calcu0(item.obd)
        "0x00,0x00,0x01,0x11" -> value= calcu1(item.obd)
        "0x00,0x00,0x01,0x20" -> value= calcu2(item.obd)
        "0x00,0x00,0x01,0x21" -> value= calcu3(item.obd)
        "0x00,0x00,0x01,0x22" -> value= calcu4(item.obd)
        "0x00,0x00,0x01,0x24" -> value= calcu5(item.obd)
        "0x00,0x00,0x01,0x25" -> value= calcu6(item.obd)
        "0x00,0x00,0x01,0x26" -> value= calcu7(item.obd)
        "0x00,0x00,0x01,0x30" -> value= calcu8(item.obd)
        "0x00,0x00,0x01,0x31" -> value= calcu9(item.obd)
        "0x00,0x00,0x01,0x32" -> value= calcu10(item.obd)
        "0x00,0x00,0x01,0x33" -> value= calcu11(item.obd)
        "0x00,0x00,0x01,0x34" -> value= calcu12(item.obd)
        "0x00,0x00,0x01,0x35" -> value= calcu13(item.obd)
        "0x00,0x00,0x01,0x36" -> value= calcu14(item.obd)
        "0x00,0x00,0x01,0x37" -> value= calcu15(item.obd)
        "0x00,0x00,0x01,0x40" -> value= calcu16(item.obd)
        "0x00,0x00,0x01,0x41" -> value= calcu17(item.obd)
        "0x00,0x00,0x01,0x42" -> value= calcu18(item.obd)
        "0x00,0x00,0x01,0x43" -> value= calcu19(item.obd)
        "0x00,0x00,0x01,0x44" -> value= calcu20(item.obd)
        "0x00,0x00,0x01,0x45" -> value= calcu21(item.obd)
        "0x00,0x00,0x01,0x46" -> value= calcu22(item.obd)
        "0x00,0x00,0x01,0x47" -> value= calcu23(item.obd)
        "0x00,0x00,0x02,0x00" -> value= calcu24(item.obd)
        "0x00,0x00,0x03,0x00" -> value= calcu25(item.obd)
        "0x00,0x00,0x03,0x10" -> value= calcu26(item.obd)
        "0x00,0x00,0x04,0x00" -> value= calcu27(item.obd)
        "0x00,0x00,0x05,0x00" -> value= calcu28(item.obd)
        "0x00,0x00,0x06,0x00" -> value= calcu29(item.obd)
        "0x00,0x00,0x06,0x10" -> value= calcu30(item.obd)
        "0x00,0x00,0x07,0x00" -> value= calcu31(item.obd)
        "0x00,0x00,0x07,0x10" -> value= calcu32(item.obd)
        "0x00,0x00,0x08,0x00" -> value= calcu33(item.obd)
        "0x00,0x00,0x08,0x10" -> value= calcu34(item.obd)
        "0x00,0x00,0x09,0x00" -> value= calcu35(item.obd)
        "0x00,0x00,0x09,0x10" -> value= calcu36(item.obd)
        "0x00,0x00,0x0A,0x00" -> value= calcu37(item.obd)
        "0x00,0x00,0x0B,0x00" -> value= calcu38(item.obd)
        "0x00,0x00,0x0C,0x00" -> value= calcu39(item.obd)
        "0x00,0x00,0x0D,0x00" -> value= calcu40(item.obd)
        "0x00,0x00,0x0E,0x00" -> value= calcu41(item.obd)
        "0x00,0x00,0x0F,0x00" -> value= calcu42(item.obd)
        "0x00,0x00,0x10,0x00" -> value= calcu43(item.obd)
        "0x00,0x00,0x11,0x00" -> value= calcu44(item.obd)
        "0x00,0x00,0x12,0x00" -> value= calcu45(item.obd)
        "0x00,0x00,0x13,0x00" -> value= calcu46(item.obd)
        "0x00,0x00,0x14,0x00" -> value= calcu47(item.obd)
        "0x00,0x00,0x14,0x01" -> value= calcu48(item.obd)
        "0x00,0x00,0x14,0x10" -> value= calcu49(item.obd)
        "0x00,0x00,0x14,0x11" -> value= calcu50(item.obd)
        "0x00,0x00,0x15,0x00" -> value= calcu51(item.obd)
        "0x00,0x00,0x15,0x01" -> value= calcu52(item.obd)
        "0x00,0x00,0x15,0x10" -> value= calcu53(item.obd)
        "0x00,0x00,0x15,0x11" -> value= calcu54(item.obd)
        "0x00,0x00,0x16,0x00" -> value= calcu55(item.obd)
        "0x00,0x00,0x16,0x01" -> value= calcu56(item.obd)
        "0x00,0x00,0x16,0x10" -> value= calcu57(item.obd)
        "0x00,0x00,0x16,0x11" -> value= calcu58(item.obd)
        "0x00,0x00,0x17,0x00" -> value= calcu59(item.obd)
        "0x00,0x00,0x17,0x01" -> value= calcu60(item.obd)
        "0x00,0x00,0x17,0x10" -> value= calcu61(item.obd)
        "0x00,0x00,0x17,0x11" -> value= calcu62(item.obd)
        "0x00,0x00,0x18,0x00" -> value= calcu63(item.obd)
        "0x00,0x00,0x18,0x01" -> value= calcu64(item.obd)
        "0x00,0x00,0x18,0x10" -> value= calcu65(item.obd)
        "0x00,0x00,0x18,0x11" -> value= calcu66(item.obd)
        "0x00,0x00,0x19,0x00" -> value= calcu67(item.obd)
        "0x00,0x00,0x19,0x01" -> value= calcu68(item.obd)
        "0x00,0x00,0x19,0x10" -> value= calcu69(item.obd)
        "0x00,0x00,0x19,0x11" -> value= calcu70(item.obd)
        "0x00,0x00,0x1A,0x00" -> value= calcu71(item.obd)
        "0x00,0x00,0x1A,0x01" -> value= calcu72(item.obd)
        "0x00,0x00,0x1A,0x10" -> value= calcu73(item.obd)
        "0x00,0x00,0x1A,0x11" -> value= calcu74(item.obd)
        "0x00,0x00,0x1B,0x00" -> value= calcu75(item.obd)
        "0x00,0x00,0x1B,0x01" -> value= calcu76(item.obd)
        "0x00,0x00,0x1B,0x10" -> value= calcu77(item.obd)
        "0x00,0x00,0x1B,0x11" -> value= calcu78(item.obd)
        "0x00,0x00,0x1C,0x00" -> value= calcu79(item.obd)
        "0x00,0x00,0x1D,0x00" -> value= calcu80(item.obd)
        "0x00,0x00,0x1E,0x00" -> value= calcu81(item.obd)
        "0x00,0x00,0x1F,0x00" -> value= calcu82(item.obd)
        "0x00,0x00,0x21,0x00" -> value= calcu83(item.obd)
        "0x00,0x00,0x22,0x00" -> value= calcu84(item.obd)
        "0x00,0x00,0x23,0x00" -> value= calcu85(item.obd)
        "0x00,0x00,0x24,0x00" -> value= calcu86(item.obd)
        "0x00,0x00,0x24,0x01" -> value= calcu87(item.obd)
        "0x00,0x00,0x24,0x20" -> value= calcu88(item.obd)
        "0x00,0x00,0x24,0x21" -> value= calcu89(item.obd)
        "0x00,0x00,0x25,0x00" -> value= calcu90(item.obd)
        "0x00,0x00,0x25,0x01" -> value= calcu91(item.obd)
        "0x00,0x00,0x25,0x20" -> value= calcu92(item.obd)
        "0x00,0x00,0x25,0x21" -> value= calcu93(item.obd)
        "0x00,0x00,0x26,0x00" -> value= calcu94(item.obd)
        "0x00,0x00,0x26,0x01" -> value= calcu95(item.obd)
        "0x00,0x00,0x26,0x20" -> value= calcu96(item.obd)
        "0x00,0x00,0x26,0x21" -> value= calcu97(item.obd)
        "0x00,0x00,0x27,0x00" -> value= calcu98(item.obd)
        "0x00,0x00,0x27,0x01" -> value= calcu99(item.obd)
        "0x00,0x00,0x27,0x20" -> value= calcu100(item.obd)
        "0x00,0x00,0x27,0x21" -> value= calcu101(item.obd)
        "0x00,0x00,0x28,0x00" -> value= calcu102(item.obd)
        "0x00,0x00,0x28,0x01" -> value= calcu103(item.obd)
        "0x00,0x00,0x28,0x20" -> value= calcu104(item.obd)
        "0x00,0x00,0x28,0x21" -> value= calcu105(item.obd)
        "0x00,0x00,0x29,0x00" -> value= calcu106(item.obd)
        "0x00,0x00,0x29,0x01" -> value= calcu107(item.obd)
        "0x00,0x00,0x29,0x20" -> value= calcu108(item.obd)
        "0x00,0x00,0x29,0x21" -> value= calcu109(item.obd)
        "0x00,0x00,0x2A,0x00" -> value= calcu110(item.obd)
        "0x00,0x00,0x2A,0x01" -> value= calcu111(item.obd)
        "0x00,0x00,0x2A,0x20" -> value= calcu112(item.obd)
        "0x00,0x00,0x2A,0x21" -> value= calcu113(item.obd)
        "0x00,0x00,0x2B,0x00" -> value= calcu114(item.obd)
        "0x00,0x00,0x2B,0x01" -> value= calcu115(item.obd)
        "0x00,0x00,0x2B,0x20" -> value= calcu116(item.obd)
        "0x00,0x00,0x2B,0x21" -> value= calcu117(item.obd)
        "0x00,0x00,0x2C,0x00" -> value= calcu118(item.obd)
        "0x00,0x00,0x2D,0x00" -> value= calcu119(item.obd)
        "0x00,0x00,0x2E,0x00" -> value= calcu120(item.obd)
        "0x00,0x00,0x2F,0x00" -> value= calcu121(item.obd)
        "0x00,0x00,0x30,0x00" -> value= calcu122(item.obd)
        "0x00,0x00,0x31,0x00" -> value= calcu123(item.obd)
        "0x00,0x00,0x32,0x00" -> value= calcu124(item.obd)
        "0x00,0x00,0x33,0x00" -> value= calcu125(item.obd)
        "0x00,0x00,0x34,0x00" -> value= calcu126(item.obd)
        "0x00,0x00,0x34,0x01" -> value= calcu127(item.obd)
        "0x00,0x00,0x34,0x20" -> value= calcu128(item.obd)
        "0x00,0x00,0x34,0x21" -> value= calcu129(item.obd)
        "0x00,0x00,0x35,0x00" -> value= calcu130(item.obd)
        "0x00,0x00,0x35,0x01" -> value= calcu131(item.obd)
        "0x00,0x00,0x35,0x20" -> value= calcu132(item.obd)
        "0x00,0x00,0x35,0x21" -> value= calcu133(item.obd)
        "0x00,0x00,0x36,0x00" -> value= calcu134(item.obd)
        "0x00,0x00,0x36,0x01" -> value= calcu135(item.obd)
        "0x00,0x00,0x36,0x20" -> value= calcu136(item.obd)
        "0x00,0x00,0x36,0x21" -> value= calcu137(item.obd)
        "0x00,0x00,0x37,0x00" -> value= calcu138(item.obd)
        "0x00,0x00,0x37,0x01" -> value= calcu139(item.obd)
        "0x00,0x00,0x37,0x20" -> value= calcu140(item.obd)
        "0x00,0x00,0x37,0x21" -> value= calcu141(item.obd)
        "0x00,0x00,0x38,0x00" -> value= calcu142(item.obd)
        "0x00,0x00,0x38,0x01" -> value= calcu143(item.obd)
        "0x00,0x00,0x38,0x20" -> value= calcu144(item.obd)
        "0x00,0x00,0x38,0x21" -> value= calcu145(item.obd)
        "0x00,0x00,0x39,0x00" -> value= calcu146(item.obd)
        "0x00,0x00,0x39,0x01" -> value= calcu147(item.obd)
        "0x00,0x00,0x39,0x20" -> value= calcu148(item.obd)
        "0x00,0x00,0x39,0x21" -> value= calcu149(item.obd)
        "0x00,0x00,0x3A,0x00" -> value= calcu150(item.obd)
        "0x00,0x00,0x3A,0x01" -> value= calcu151(item.obd)
        "0x00,0x00,0x3A,0x20" -> value= calcu152(item.obd)
        "0x00,0x00,0x3A,0x21" -> value= calcu153(item.obd)
        "0x00,0x00,0x3B,0x00" -> value= calcu154(item.obd)
        "0x00,0x00,0x3B,0x01" -> value= calcu155(item.obd)
        "0x00,0x00,0x3B,0x20" -> value= calcu156(item.obd)
        "0x00,0x00,0x3B,0x21" -> value= calcu157(item.obd)
        "0x00,0x00,0x3C,0x00" -> value= calcu158(item.obd)
        "0x00,0x00,0x3D,0x00" -> value= calcu159(item.obd)
        "0x00,0x00,0x3E,0x00" -> value= calcu160(item.obd)
        "0x00,0x00,0x3F,0x00" -> value= calcu161(item.obd)
        "0x00,0x00,0x41,0x20" -> value= calcu162(item.obd)
        "0x00,0x00,0x41,0x21" -> value= calcu163(item.obd)
        "0x00,0x00,0x41,0x22" -> value= calcu164(item.obd)
        "0x00,0x00,0x41,0x24" -> value= calcu165(item.obd)
        "0x00,0x00,0x41,0x25" -> value= calcu166(item.obd)
        "0x00,0x00,0x41,0x26" -> value= calcu167(item.obd)
        "0x00,0x00,0x41,0x30" -> value= calcu168(item.obd)
        "0x00,0x00,0x41,0x31" -> value= calcu169(item.obd)
        "0x00,0x00,0x41,0x32" -> value= calcu170(item.obd)
        "0x00,0x00,0x41,0x33" -> value= calcu171(item.obd)
        "0x00,0x00,0x41,0x34" -> value= calcu172(item.obd)
        "0x00,0x00,0x41,0x35" -> value= calcu173(item.obd)
        "0x00,0x00,0x41,0x36" -> value= calcu174(item.obd)
        "0x00,0x00,0x41,0x37" -> value= calcu175(item.obd)
        "0x00,0x00,0x41,0x40" -> value= calcu176(item.obd)
        "0x00,0x00,0x41,0x41" -> value= calcu177(item.obd)
        "0x00,0x00,0x41,0x42" -> value= calcu178(item.obd)
        "0x00,0x00,0x41,0x43" -> value= calcu179(item.obd)
        "0x00,0x00,0x41,0x44" -> value= calcu180(item.obd)
        "0x00,0x00,0x41,0x45" -> value= calcu181(item.obd)
        "0x00,0x00,0x41,0x46" -> value= calcu182(item.obd)
        "0x00,0x00,0x41,0x47" -> value= calcu183(item.obd)
        "0x00,0x00,0x42,0x00" -> value= calcu184(item.obd)
        "0x00,0x00,0x43,0x00" -> value= calcu185(item.obd)
        "0x00,0x00,0x44,0x00" -> value= calcu186(item.obd)
        "0x00,0x00,0x45,0x00" -> value= calcu187(item.obd)
        "0x00,0x00,0x46,0x00" -> value= calcu188(item.obd)
        "0x00,0x00,0x47,0x00" -> value= calcu189(item.obd)
        "0x00,0x00,0x48,0x00" -> value= calcu190(item.obd)
        "0x00,0x00,0x49,0x00" -> value= calcu191(item.obd)
        "0x00,0x00,0x4A,0x00" -> value= calcu192(item.obd)
        "0x00,0x00,0x4B,0x00" -> value= calcu193(item.obd)
        "0x00,0x00,0x4C,0x00" -> value= calcu194(item.obd)
        "0x00,0x00,0x4D,0x00" -> value= calcu195(item.obd)
        "0x00,0x00,0x4E,0x00" -> value= calcu196(item.obd)
        "0x00,0x06,0x12,0x01" -> value= calcu197(item.obd)
        "0x00,0x06,0x12,0x02" -> value= calcu198(item.obd)
        "0x00,0x06,0x12,0x03" -> value= calcu199(item.obd)
        "0x00,0x06,0x12,0x04" -> value= calcu200(item.obd)
        "0x00,0x06,0x12,0x05" -> value= calcu201(item.obd)
        "0x00,0x06,0x12,0x06" -> value= calcu202(item.obd)
        "0x00,0x06,0x12,0x07" -> value= calcu203(item.obd)
        "0x00,0x06,0x12,0x08" -> value= calcu204(item.obd)
        "0x00,0x06,0x12,0x09" -> value= calcu205(item.obd)
        "0x00,0x06,0x12,0x0A" -> value= calcu206(item.obd)
        "0x00,0x06,0x12,0x0B" -> value= calcu207(item.obd)
        "0x00,0x06,0x12,0x0C" -> value= calcu208(item.obd)
        "0x00,0x06,0x12,0x0D" -> value= calcu209(item.obd)
        "0x00,0x06,0x12,0x0E" -> value= calcu210(item.obd)
        "0x00,0x06,0x12,0x0F" -> value= calcu211(item.obd)
        "0x00,0x06,0x12,0x10" -> value= calcu212(item.obd)
        "0x00,0x06,0x12,0x11" -> value= calcu213(item.obd)
        "0x00,0x06,0x12,0x12" -> value= calcu214(item.obd)
        "0x00,0x06,0x12,0x13" -> value= calcu215(item.obd)
        "0x00,0x06,0x12,0x14" -> value= calcu216(item.obd)
        "0x00,0x06,0x12,0x15" -> value= calcu217(item.obd)
        "0x00,0x06,0x12,0x16" -> value= calcu218(item.obd)
        "0x00,0x06,0x12,0x17" -> value= calcu219(item.obd)
        "0x00,0x06,0x12,0x18" -> value= calcu220(item.obd)
        "0x00,0x06,0x12,0x19" -> value= calcu221(item.obd)
        "0x00,0x06,0x12,0x1A" -> value= calcu222(item.obd)
        "0x00,0x06,0x12,0x1B" -> value= calcu223(item.obd)
        "0x00,0x06,0x12,0x1C" -> value= calcu224(item.obd)
        "0x00,0x06,0x12,0x1D" -> value= calcu225(item.obd)
        "0x00,0x06,0x12,0x1E" -> value= calcu226(item.obd)
        "0x00,0x06,0x12,0x1F" -> value= calcu227(item.obd)
        "0x00,0x06,0x12,0x20" -> value= calcu228(item.obd)
        "0x00,0x06,0x12,0x21" -> value= calcu229(item.obd)
        "0x00,0x06,0x12,0x22" -> value= calcu230(item.obd)
        "0x00,0x06,0x12,0x23" -> value= calcu231(item.obd)
        "0x00,0x06,0x12,0x24" -> value= calcu232(item.obd)
        "0x00,0x06,0x12,0x25" -> value= calcu233(item.obd)
        "0x00,0x06,0x12,0x26" -> value= calcu234(item.obd)
        "0x00,0x06,0x12,0x27" -> value= calcu235(item.obd)
        "0x00,0x06,0x12,0x28" -> value= calcu236(item.obd)
        "0x00,0x06,0x12,0x29" -> value= calcu237(item.obd)
        "0x00,0x06,0x12,0x2A" -> value= calcu238(item.obd)
        "0x00,0x06,0x12,0x2B" -> value= calcu239(item.obd)
        "0x00,0x06,0x12,0x2C" -> value= calcu240(item.obd)
        "0x00,0x06,0x12,0x2D" -> value= calcu241(item.obd)
        "0x00,0x06,0x12,0x2E" -> value= calcu242(item.obd)
        "0x00,0x06,0x12,0x2F" -> value= calcu243(item.obd)
        "0x00,0x06,0x12,0x30" -> value= calcu244(item.obd)
        "0x00,0x06,0x12,0x31" -> value= calcu245(item.obd)
        "0x00,0x06,0x12,0x32" -> value= calcu246(item.obd)
        "0x00,0x06,0x12,0x33" -> value= calcu247(item.obd)
        "0x00,0x06,0x12,0x34" -> value= calcu248(item.obd)
        "0x00,0x06,0x12,0x35" -> value= calcu249(item.obd)
        "0x00,0x06,0x12,0x36" -> value= calcu250(item.obd)
        "0x00,0x06,0x12,0x37" -> value= calcu251(item.obd)
        "0x00,0x06,0x12,0x38" -> value= calcu252(item.obd)
        "0x00,0x06,0x12,0x39" -> value= calcu253(item.obd)
        "0x00,0x06,0x12,0x3A" -> value= calcu254(item.obd)
        "0x00,0x06,0x12,0x3B" -> value= calcu255(item.obd)
        "0x00,0x06,0x12,0x3C" -> value= calcu256(item.obd)
        "0x00,0x06,0x12,0x3D" -> value= calcu257(item.obd)
        "0x00,0x06,0x12,0x3E" -> value= calcu258(item.obd)
        "0x00,0x06,0x12,0x3F" -> value= calcu259(item.obd)
        "0x00,0x06,0x12,0x40" -> value= calcu260(item.obd)
        "0x00,0x06,0x12,0x41" -> value= calcu261(item.obd)
        "0x00,0x06,0x12,0x42" -> value= calcu262(item.obd)
        "0x00,0x06,0x12,0x43" -> value= calcu263(item.obd)
        "0x00,0x06,0x12,0x44" -> value= calcu264(item.obd)
        "0x00,0x06,0x12,0x45" -> value= calcu265(item.obd)
        "0x00,0x06,0x12,0x46" -> value= calcu266(item.obd)
        "0x00,0x06,0x12,0x47" -> value= calcu267(item.obd)
        "0x00,0x06,0x12,0x48" -> value= calcu268(item.obd)
        "0x00,0x06,0x12,0x49" -> value= calcu269(item.obd)
        "0x00,0x06,0x12,0x4A" -> value= calcu270(item.obd)
        "0x00,0x06,0x12,0x4B" -> value= calcu271(item.obd)
        "0x00,0x06,0x12,0x4C" -> value= calcu272(item.obd)
        "0x00,0x06,0x12,0x4D" -> value= calcu273(item.obd)
        "0x00,0x06,0x12,0x4E" -> value= calcu274(item.obd)
        "0x00,0x06,0x12,0x4F" -> value= calcu275(item.obd)
        "0x00,0x06,0x12,0x50" -> value= calcu276(item.obd)
        "0x00,0x06,0x12,0x51" -> value= calcu277(item.obd)
        "0x00,0x06,0x12,0x52" -> value= calcu278(item.obd)
        "0x00,0x06,0x12,0x53" -> value= calcu279(item.obd)
        "0x00,0x06,0x12,0x54" -> value= calcu280(item.obd)
        "0x00,0x06,0x12,0x55" -> value= calcu281(item.obd)
        "0x00,0x06,0x12,0x56" -> value= calcu282(item.obd)
        "0x00,0x06,0x12,0x57" -> value= calcu283(item.obd)
        "0x00,0x06,0x12,0x58" -> value= calcu284(item.obd)
        "0x00,0x06,0x12,0x59" -> value= calcu285(item.obd)
        "0x00,0x06,0x12,0x5A" -> value= calcu286(item.obd)
        "0x00,0x06,0x12,0x5B" -> value= calcu287(item.obd)
        "0x00,0x06,0x12,0x5C" -> value= calcu288(item.obd)
        "0x00,0x06,0x12,0x5D" -> value= calcu289(item.obd)
        "0x00,0x06,0x12,0x5E" -> value= calcu290(item.obd)
        "0x00,0x06,0x12,0x5F" -> value= calcu291(item.obd)
        "0x00,0x06,0x12,0x60" -> value= calcu292(item.obd)
        "0x00,0x06,0x12,0x61" -> value= calcu293(item.obd)
        "0x00,0x06,0x12,0x62" -> value= calcu294(item.obd)
        "0x00,0x06,0x12,0x63" -> value= calcu295(item.obd)
        "0x00,0x06,0x12,0x64" -> value= calcu296(item.obd)
        "0x00,0x06,0x12,0x65" -> value= calcu297(item.obd)
        "0x00,0x06,0x12,0x66" -> value= calcu298(item.obd)
        "0x00,0x06,0x12,0x67" -> value= calcu299(item.obd)
        "0x00,0x06,0x12,0x68" -> value= calcu300(item.obd)
        "0x00,0x06,0x12,0x69" -> value= calcu301(item.obd)
        "0x00,0x06,0x12,0x6A" -> value= calcu302(item.obd)
        "0x00,0x06,0x12,0x6B" -> value= calcu303(item.obd)
        "0x00,0x06,0x12,0x6C" -> value= calcu304(item.obd)
        "0x00,0x06,0x12,0x6D" -> value= calcu305(item.obd)
        "0x00,0x06,0x12,0x6E" -> value= calcu306(item.obd)
        "0x00,0x06,0x12,0x6F" -> value= calcu307(item.obd)
        "0x00,0x06,0x12,0x70" -> value= calcu308(item.obd)
        "0x00,0x06,0x12,0x71" -> value= calcu309(item.obd)
        "0x00,0x06,0x12,0x72" -> value= calcu310(item.obd)
        "0x00,0x06,0x12,0x73" -> value= calcu311(item.obd)
        "0x00,0x06,0x12,0x74" -> value= calcu312(item.obd)
        "0x00,0x06,0x12,0x75" -> value= calcu313(item.obd)
        "0x00,0x06,0x12,0x76" -> value= calcu314(item.obd)
        "0x00,0x06,0x12,0x77" -> value= calcu315(item.obd)
        "0x00,0x06,0x12,0x78" -> value= calcu316(item.obd)
        "0x00,0x06,0x12,0x79" -> value= calcu317(item.obd)
        "0x00,0x06,0x12,0x7A" -> value= calcu318(item.obd)
        "0x00,0x06,0x12,0x7B" -> value= calcu319(item.obd)
        "0x00,0x06,0x12,0x7C" -> value= calcu320(item.obd)
        "0x00,0x06,0x12,0x7D" -> value= calcu321(item.obd)
        "0x00,0x06,0x12,0x7E" -> value= calcu322(item.obd)
        "0x00,0x06,0x12,0x7F" -> value= calcu323(item.obd)
        "0x00,0x06,0x12,0x80" -> value= calcu324(item.obd)
        "0x00,0x06,0x12,0x81" -> value= calcu325(item.obd)
        "0x00,0x06,0x12,0x82" -> value= calcu326(item.obd)
        "0x00,0x06,0x12,0x83" -> value= calcu327(item.obd)
        "0x00,0x06,0x12,0x84" -> value= calcu328(item.obd)
        "0x00,0x06,0x12,0x85" -> value= calcu329(item.obd)
        "0x00,0x06,0x12,0x86" -> value= calcu330(item.obd)
        "0x00,0x06,0x12,0x87" -> value= calcu331(item.obd)
        "0x00,0x06,0x12,0x88" -> value= calcu332(item.obd)
        "0x00,0x06,0x12,0x89" -> value= calcu333(item.obd)
        "0x00,0x06,0x12,0x8A" -> value= calcu334(item.obd)
        "0x00,0x06,0x12,0x8B" -> value= calcu335(item.obd)
        "0x00,0x06,0x12,0x8C" -> value= calcu336(item.obd)
        "0x00,0x06,0x12,0x8D" -> value= calcu337(item.obd)
        "0x00,0x06,0x12,0x8E" -> value= calcu338(item.obd)
        "0x00,0x06,0x12,0x8F" -> value= calcu339(item.obd)
        "0x00,0x06,0x12,0x90" -> value= calcu340(item.obd)
        "0x00,0x06,0x12,0x91" -> value= calcu341(item.obd)
        "0x00,0x06,0x12,0x92" -> value= calcu342(item.obd)
        "0x00,0x06,0x12,0x93" -> value= calcu343(item.obd)
        "0x00,0x06,0x12,0x94" -> value= calcu344(item.obd)
        "0x00,0x06,0x12,0x95" -> value= calcu345(item.obd)
        "0x00,0x06,0x12,0x96" -> value= calcu346(item.obd)
        "0x00,0x06,0x12,0x97" -> value= calcu347(item.obd)
        "0x00,0x06,0x12,0x98" -> value= calcu348(item.obd)
        "0x00,0x06,0x12,0x99" -> value= calcu349(item.obd)
        "0x00,0x06,0x12,0x9A" -> value= calcu350(item.obd)
        "0x00,0x06,0x12,0x9B" -> value= calcu351(item.obd)
        "0x00,0x06,0x12,0x9C" -> value= calcu352(item.obd)
        "0x00,0x06,0x12,0x9D" -> value= calcu353(item.obd)
        "0x00,0x06,0x12,0x9E" -> value= calcu354(item.obd)
        "0x00,0x06,0x12,0x9F" -> value= calcu355(item.obd)
        "0x00,0x06,0x12,0xA0" -> value= calcu356(item.obd)
        "0x00,0x06,0x12,0xA1" -> value= calcu357(item.obd)
        "0x00,0x06,0x12,0xA2" -> value= calcu358(item.obd)
        "0x00,0x06,0x12,0xA3" -> value= calcu359(item.obd)
        "0x00,0x06,0x12,0xA4" -> value= calcu360(item.obd)
        "0x00,0x06,0x12,0xA5" -> value= calcu361(item.obd)
        "0x00,0x06,0x12,0xA6" -> value= calcu362(item.obd)
        "0x00,0x06,0x12,0xA7" -> value= calcu363(item.obd)
        "0x00,0x06,0x12,0xA8" -> value= calcu364(item.obd)
        "0x00,0x06,0x12,0xA9" -> value= calcu365(item.obd)
        "0x00,0x06,0x12,0xAA" -> value= calcu366(item.obd)
        "0x00,0x06,0x12,0xAB" -> value= calcu367(item.obd)
        "0x00,0x06,0x12,0xAC" -> value= calcu368(item.obd)
        "0x00,0x06,0x12,0xAD" -> value= calcu369(item.obd)
        "0x00,0x06,0x12,0xAE" -> value= calcu370(item.obd)
        "0x00,0x06,0x12,0xAF" -> value= calcu371(item.obd)
        "0x00,0x06,0x12,0xB0" -> value= calcu372(item.obd)
        "0x00,0x06,0x12,0xB1" -> value= calcu373(item.obd)
        "0x00,0x06,0x12,0xB2" -> value= calcu374(item.obd)
        "0x00,0x06,0x12,0xB3" -> value= calcu375(item.obd)
        "0x00,0x06,0x12,0xB4" -> value= calcu376(item.obd)
        "0x00,0x06,0x12,0xB5" -> value= calcu377(item.obd)
        "0x00,0x06,0x12,0xB6" -> value= calcu378(item.obd)
        "0x00,0x06,0x12,0xB7" -> value= calcu379(item.obd)
        "0x00,0x06,0x12,0xB8" -> value= calcu380(item.obd)
        "0x00,0x06,0x12,0xB9" -> value= calcu381(item.obd)
        "0x00,0x06,0x12,0xBA" -> value= calcu382(item.obd)
        "0x00,0x06,0x12,0xBB" -> value= calcu383(item.obd)
        "0x00,0x06,0x12,0xBC" -> value= calcu384(item.obd)
        "0x00,0x06,0x12,0xBD" -> value= calcu385(item.obd)
        "0x00,0x06,0x12,0xBE" -> value= calcu386(item.obd)
        "0x00,0x06,0x12,0xBF" -> value= calcu387(item.obd)
        "0x00,0x06,0x12,0xC0" -> value= calcu388(item.obd)
        "0x00,0x06,0x12,0xC1" -> value= calcu389(item.obd)
        "0x00,0x06,0x12,0xC2" -> value= calcu390(item.obd)
        "0x00,0x06,0x12,0xC3" -> value= calcu391(item.obd)
        "0x00,0x06,0x12,0xC4" -> value= calcu392(item.obd)
        "0x00,0x06,0x12,0xC5" -> value= calcu393(item.obd)
        "0x00,0x06,0x12,0xC6" -> value= calcu394(item.obd)
        "0x00,0x06,0x12,0xC7" -> value= calcu395(item.obd)
        "0x00,0x06,0x12,0xC8" -> value= calcu396(item.obd)
        "0x00,0x06,0x12,0xC9" -> value= calcu397(item.obd)
        "0x00,0x06,0x12,0xCA" -> value= calcu398(item.obd)
        "0x00,0x06,0x12,0xCB" -> value= calcu399(item.obd)
        "0x00,0x06,0x12,0xCC" -> value= calcu400(item.obd)
        "0x00,0x06,0x12,0xCD" -> value= calcu401(item.obd)
        "0x00,0x06,0x12,0xCE" -> value= calcu402(item.obd)
        "0x00,0x06,0x12,0xCF" -> value= calcu403(item.obd)
        "0x00,0x06,0x12,0xD0" -> value= calcu404(item.obd)
        "0x00,0x06,0x12,0xD1" -> value= calcu405(item.obd)
        "0x00,0x06,0x12,0xD2" -> value= calcu406(item.obd)
        "0x00,0x06,0x12,0xD3" -> value= calcu407(item.obd)
        "0x00,0x06,0x12,0xD4" -> value= calcu408(item.obd)
        "0x00,0x06,0x12,0xD5" -> value= calcu409(item.obd)
        "0x00,0x06,0x12,0xD6" -> value= calcu410(item.obd)
        "0x00,0x06,0x12,0xD7" -> value= calcu411(item.obd)
        "0x00,0x06,0x12,0xD8" -> value= calcu412(item.obd)
        "0x00,0x06,0x12,0xD9" -> value= calcu413(item.obd)
        "0x00,0x06,0x12,0xDA" -> value= calcu414(item.obd)
        "0x00,0x06,0x12,0xDB" -> value= calcu415(item.obd)
        "0x00,0x06,0x12,0xDC" -> value= calcu416(item.obd)
        "0x00,0x06,0x12,0xDD" -> value= calcu417(item.obd)
        "0x00,0x06,0x12,0xDE" -> value= calcu418(item.obd)
        "0x00,0x06,0x12,0xDF" -> value= calcu419(item.obd)
        "0x00,0x06,0x12,0xE0" -> value= calcu420(item.obd)
        "0x00,0x06,0x12,0xE1" -> value= calcu421(item.obd)
        "0x00,0x06,0x12,0xE2" -> value= calcu422(item.obd)
        "0x00,0x06,0x12,0xE3" -> value= calcu423(item.obd)
        "0x00,0x06,0x12,0xE4" -> value= calcu424(item.obd)
        "0x00,0x06,0x12,0xE5" -> value= calcu425(item.obd)
        "0x00,0x06,0x12,0xE6" -> value= calcu426(item.obd)
        "0x00,0x06,0x12,0xE7" -> value= calcu427(item.obd)
        "0x00,0x06,0x12,0xE8" -> value= calcu428(item.obd)
        "0x00,0x06,0x12,0xE9" -> value= calcu429(item.obd)
        "0x00,0x06,0x12,0xEA" -> value= calcu430(item.obd)
        "0x00,0x06,0x12,0xEB" -> value= calcu431(item.obd)
        "0x00,0x06,0x12,0xEC" -> value= calcu432(item.obd)
        "0x00,0x06,0x12,0xED" -> value= calcu433(item.obd)
        "0x00,0x06,0x12,0xEE" -> value= calcu434(item.obd)
        "0x00,0x06,0x12,0xEF" -> value= calcu435(item.obd)
        "0x00,0x06,0x12,0xF0" -> value= calcu436(item.obd)
        "0x00,0x06,0x12,0xF1" -> value= calcu437(item.obd)
        "0x00,0x06,0x12,0xF2" -> value= calcu438(item.obd)
        "0x00,0x06,0x12,0xF3" -> value= calcu439(item.obd)
        "0x00,0x06,0x12,0xF4" -> value= calcu440(item.obd)
        "0x00,0x06,0x12,0xF5" -> value= calcu441(item.obd)
        "0x00,0x06,0x12,0xF6" -> value= calcu442(item.obd)
        "0x00,0x06,0x12,0xF7" -> value= calcu443(item.obd)
        "0x00,0x06,0x12,0xF8" -> value= calcu444(item.obd)
        "0x00,0x06,0x12,0xF9" -> value= calcu445(item.obd)
        "0x00,0x06,0x12,0xFA" -> value= calcu446(item.obd)
        "0x00,0x06,0x12,0xFB" -> value= calcu447(item.obd)
        "0x00,0x06,0x12,0xFC" -> value= calcu448(item.obd)
        "0x00,0x06,0x12,0xFD" -> value= calcu449(item.obd)
        "0x00,0x06,0x12,0xFE" -> value= calcu450(item.obd)
        "0x00,0x06,0x12,0xFF" -> value= calcu451(item.obd)
        else -> { ::calcuEmpty}
    }
    return value
}


