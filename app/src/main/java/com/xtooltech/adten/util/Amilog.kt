package com.xtooltech.adten.util

val calc= mapOf(
    "0x00,0x00,0x01,0x10" to 	Pair("%d",	"BYTE[2]&0x7F;"),
    "0x00,0x00,0x01,0x11" to 	Pair("MIL ON|MIL OFF",	"(BYTE[2]&0x80)?0:1;"),
    "0x00,0x00,0x01,0x20" to 	Pair("YES|NO",	"(BYTE[3]&0x01)?0:1;"),
    "0x00,0x00,0x01,0x21" to 	Pair("YES|NO",	"(BYTE[3]&0x02)?0:1;"),
    "0x00,0x00,0x01,0x22" to 	Pair("YES|NO",	"(BYTE[3]&0x04)?0:1;"),
    "0x00,0x00,0x01,0x24" to 	Pair("NO|YES",	"(BYTE[3]&0x10)?0:1;"),
    "0x00,0x00,0x01,0x25" to 	Pair("NO|YES",	"(BYTE[3]&0x20)?0:1;"),
    "0x00,0x00,0x01,0x26" to 	Pair("NO|YES",	"(BYTE[3]&0x40)?0:1;"),
    "0x00,0x00,0x01,0x30" to 	Pair("YES|NO",	"(BYTE[4]&0x01)?0:1;"),
    "0x00,0x00,0x01,0x31" to 	Pair("YES|NO",	"(BYTE[4]&0x02)?0:1;"),
    "0x00,0x00,0x01,0x32" to 	Pair("YES|NO",	"(BYTE[4]&0x04)?0:1;"),
    "0x00,0x00,0x01,0x33" to 	Pair("YES|NO",	"(BYTE[4]&0x08)?0:1;"),
    "0x00,0x00,0x01,0x34" to 	Pair("YES|NO",	"(BYTE[4]&0x10)?0:1;"),
    "0x00,0x00,0x01,0x35" to 	Pair("YES|NO",	"(BYTE[4]&0x20)?0:1;"),
    "0x00,0x00,0x01,0x36" to 	Pair("YES|NO",	"(BYTE[4]&0x40)?0:1;"),
    "0x00,0x00,0x01,0x37" to 	Pair("YES|NO",	"(BYTE[4]&0x80)?0:1;"),
    "0x00,0x00,0x01,0x40" to 	Pair("NO|YES",	"(BYTE[5]&0x01)?0:1;"),
    "0x00,0x00,0x01,0x41" to 	Pair("NO|YES",	"(BYTE[5]&0x02)?0:1;"),
    "0x00,0x00,0x01,0x42" to 	Pair("NO|YES",	"(BYTE[5]&0x04)?0:1;"),
    "0x00,0x00,0x01,0x43" to 	Pair("NO|YES",	"(BYTE[5]&0x08)?0:1;"),
    "0x00,0x00,0x01,0x44" to 	Pair("NO|YES",	"(BYTE[5]&0x10)?0:1;"),
    "0x00,0x00,0x01,0x45" to 	Pair("NO|YES",	"(BYTE[5]&0x20)?0:1;"),
    "0x00,0x00,0x01,0x46" to 	Pair("NO|YES",	"(BYTE[5]&0x40)?0:1;"),
    "0x00,0x00,0x01,0x47" to 	Pair("NO|YES",	"(BYTE[5]&0x80)?0:1;"),
    "0x00,0x00,0x02,0x00" to 	Pair("",	""),
    "0x00,0x00,0x03,0x00" to 	Pair("",	""),
    "0x00,0x00,0x03,0x10" to 	Pair("",	""),
    "0x00,0x00,0x04,0x00" to 	Pair("%3.1f",	"BYTE[2]*100.0/255;"),
    "0x00,0x00,0x05,0x00" to 	Pair("%d",	"BYTE[2]-40;"),
    "0x00,0x00,0x06,0x00" to 	Pair("%3.1f",	"BYTE[2]*199.2/255-100;"),
    "0x00,0x00,0x06,0x10" to 	Pair("%3.1f",	"BYTE[2]*199.2/255-100;"),
    "0x00,0x00,0x07,0x00" to 	Pair("%3.1f",	"BYTE[2]*199.2/255-100;"),
    "0x00,0x00,0x07,0x10" to 	Pair("%3.1f",	"BYTE[2]*199.2/255-100;"),
    "0x00,0x00,0x08,0x00" to 	Pair("%3.1f",	"BYTE[2]*199.2/255-100;"),
    "0x00,0x00,0x08,0x10" to 	Pair("%3.1f",	"BYTE[2]*199.2/255-100;"),
    "0x00,0x00,0x09,0x00" to 	Pair("%3.1f",	"BYTE[2]*199.2/255-100;"),
    "0x00,0x00,0x09,0x10" to 	Pair("%3.1f",	"BYTE[2]*199.2/255-100;"),
    "0x00,0x00,0x0A,0x00" to 	Pair("%d",	"BYTE[2]*3;"),
    "0x00,0x00,0x0B,0x00" to 	Pair("%d",	"BYTE[2];"),
    "0x00,0x00,0x0C,0x00" to 	Pair("%d",	"(BYTE[2]*256+BYTE[3])/4;"),
    "0x00,0x00,0x0D,0x00" to 	Pair("%d",	"BYTE[2];"),
    "0x00,0x00,0x0E,0x00" to 	Pair("%2.1f",	"BYTE[2]/2.0-64;"),
    "0x00,0x00,0x0F,0x00" to 	Pair("%d",	"BYTE[2]-40;"),
    "0x00,0x00,0x10,0x00" to 	Pair("%3.2f",	"(BYTE[2]*256+BYTE[3])/100.0;"),
    "0x00,0x00,0x11,0x00" to 	Pair("%3.1f",	"BYTE[2]*100.0/255;"),
    "0x00,0x00,0x12,0x00" to 	Pair("",	""),
    "0x00,0x00,0x13,0x00" to 	Pair("",	""),
    "0x00,0x00,0x14,0x00" to 	Pair("%1.3f",	"BYTE[2]*0.005;"),
    "0x00,0x00,0x14,0x01" to 	Pair("%1.3f",	"BYTE[2]*0.005;"),
    "0x00,0x00,0x14,0x10" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x14,0x11" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x15,0x00" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x15,0x01" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x15,0x10" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x15,0x11" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x16,0x00" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x16,0x01" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x16,0x10" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x16,0x11" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x17,0x00" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x17,0x01" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x17,0x10" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x17,0x11" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x18,0x00" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x18,0x01" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x18,0x10" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x18,0x11" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x19,0x00" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x19,0x01" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x19,0x10" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x19,0x11" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x1A,0x00" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x1A,0x01" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x1A,0x10" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x1A,0x11" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x1B,0x00" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x1B,0x01" to 	Pair("%1.3f",	"BYTE[2]*0.005;"        ),
    "0x00,0x00,0x1B,0x10" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x1B,0x11" to 	Pair("%3.1f",	"BYTE[3]*199.2/255-100;"),
    "0x00,0x00,0x1C,0x00" to 	Pair("",	""),
    "0x00,0x00,0x1D,0x00" to 	Pair("",	""),
    "0x00,0x00,0x1E,0x00" to 	Pair("",	""),
    "0x00,0x00,0x1F,0x00" to 	Pair("%d",	"BYTE[2]*256+BYTE[3];"),
    "0x00,0x00,0x21,0x00" to 	Pair("%d",	"BYTE[2]*256+BYTE[3];"),
    "0x00,0x00,0x22,0x00" to 	Pair("%4.3f",	"(BYTE[2]*256+BYTE[3])*0.079;"),
    "0x00,0x00,0x23,0x00" to 	Pair("%d",	"(BYTE[2]*256+BYTE[3])*10;"),
    "0x00,0x00,0x24,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"),
    "0x00,0x00,0x24,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"),
    "0x00,0x00,0x24,0x20" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;"),
    "0x00,0x00,0x24,0x21" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;"),
    "0x00,0x00,0x25,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x25,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x25,0x20" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x25,0x21" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x26,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x26,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x26,0x20" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x26,0x21" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x27,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x27,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x27,0x20" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x27,0x21" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x28,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x28,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x28,0x20" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x28,0x21" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x29,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x29,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x29,0x20" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x29,0x21" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x2A,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x2A,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x2A,0x20" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x2A,0x21" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x2B,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x2B,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;" ),
    "0x00,0x00,0x2B,0x20" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x2B,0x21" to 	Pair("%1.3f",	"(BYTE[4]*256+BYTE[5])*8.0/65536;" ),
    "0x00,0x00,0x2C,0x00" to 	Pair("%3.1f",	"BYTE[2]*100.0/255;"),
    "0x00,0x00,0x2D,0x00" to 	Pair("%3.1f",	"BYTE[2]*199.2/255-100;"),
    "0x00,0x00,0x2E,0x00" to 	Pair("%3.1f",	"BYTE[2]*100.0/255;"),
    "0x00,0x00,0x2F,0x00" to 	Pair("%3.1f",	"BYTE[2]*100.0/255;"),
    "0x00,0x00,0x30,0x00" to 	Pair("%d",	"BYTE[2];"),
    "0x00,0x00,0x31,0x00" to 	Pair("%d",	"BYTE[2]*256+BYTE[3];"),
    "0x00,0x00,0x32,0x00" to 	Pair("%4.2f",	"(CHAR[2]*256+BYTE[3])*0.25;"),
    "0x00,0x00,0x33,0x00" to 	Pair("%d",	"BYTE[2];"),
    "0x00,0x00,0x34,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"),
    "0x00,0x00,0x34,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"),
    "0x00,0x00,0x34,0x20" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x34,0x21" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x35,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x35,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x35,0x20" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x35,0x21" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x36,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x36,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x36,0x20" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x36,0x21" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x37,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x37,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x37,0x20" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x37,0x21" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x38,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x38,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x38,0x20" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x38,0x21" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x39,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x39,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x39,0x20" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x39,0x21" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x3A,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x3A,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x3A,0x20" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x3A,0x21" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x3B,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x3B,0x01" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"      ),
    "0x00,0x00,0x3B,0x20" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x3B,0x21" to 	Pair("%3.3f",	"(BYTE[3]*256+BYTE[4])*128.0/65536-128;"),
    "0x00,0x00,0x3C,0x00" to 	Pair("%4.1f",	"(BYTE[2]*256+BYTE[3])*0.1-40;"),
    "0x00,0x00,0x3D,0x00" to 	Pair("%4.1f",	"(BYTE[2]*256+BYTE[3])*0.1-40;"),
    "0x00,0x00,0x3E,0x00" to 	Pair("%4.1f",	"(BYTE[2]*256+BYTE[3])*0.1-40;"),
    "0x00,0x00,0x3F,0x00" to 	Pair("%4.1f",	"(BYTE[2]*256+BYTE[3])*0.1-40;"),
    "0x00,0x00,0x41,0x20" to 	Pair("YES|NO",	"(BYTE[3]&0x01)?0:1;"),
    "0x00,0x00,0x41,0x21" to 	Pair("YES|NO",	"(BYTE[3]&0x02)?0:1;"),
    "0x00,0x00,0x41,0x22" to 	Pair("YES|NO",	"(BYTE[3]&0x04)?0:1;"),
    "0x00,0x00,0x41,0x24" to 	Pair("NO|YES",	"(BYTE[3]&0x10)?0:1;"),
    "0x00,0x00,0x41,0x25" to 	Pair("NO|YES",	"(BYTE[3]&0x20)?0:1;"),
    "0x00,0x00,0x41,0x26" to 	Pair("NO|YES",	"(BYTE[3]&0x40)?0:1;"),
    "0x00,0x00,0x41,0x30" to 	Pair("YES|NO",	"(BYTE[4]&0x01)?0:1;"),
    "0x00,0x00,0x41,0x31" to 	Pair("YES|NO",	"(BYTE[4]&0x02)?0:1;"),
    "0x00,0x00,0x41,0x32" to 	Pair("YES|NO",	"(BYTE[4]&0x04)?0:1;"),
    "0x00,0x00,0x41,0x33" to 	Pair("YES|NO",	"(BYTE[4]&0x08)?0:1;"),
    "0x00,0x00,0x41,0x34" to 	Pair("YES|NO",	"(BYTE[4]&0x10)?0:1;"),
    "0x00,0x00,0x41,0x35" to 	Pair("YES|NO",	"(BYTE[4]&0x20)?0:1;"),
    "0x00,0x00,0x41,0x36" to 	Pair("YES|NO",	"(BYTE[4]&0x40)?0:1;"),
    "0x00,0x00,0x41,0x37" to 	Pair("YES|NO",	"(BYTE[4]&0x80)?0:1;"),
    "0x00,0x00,0x41,0x40" to 	Pair("YES|NO",	"(BYTE[4]&0x01)?0:1;"),
    "0x00,0x00,0x41,0x41" to 	Pair("YES|NO",	"(BYTE[4]&0x02)?0:1;"),
    "0x00,0x00,0x41,0x42" to 	Pair("YES|NO",	"(BYTE[4]&0x04)?0:1;"),
    "0x00,0x00,0x41,0x43" to 	Pair("YES|NO",	"(BYTE[4]&0x08)?0:1;"),
    "0x00,0x00,0x41,0x44" to 	Pair("YES|NO",	"(BYTE[4]&0x10)?0:1;"),
    "0x00,0x00,0x41,0x45" to 	Pair("YES|NO",	"(BYTE[4]&0x20)?0:1;"),
    "0x00,0x00,0x41,0x46" to 	Pair("YES|NO",	"(BYTE[4]&0x40)?0:1;"),
    "0x00,0x00,0x41,0x47" to 	Pair("YES|NO",	"(BYTE[4]&0x80)?0:1;"),
    "0x00,0x00,0x42,0x00" to 	Pair("%2.3f",	"(BYTE[2]*256+BYTE[3])*0.001;"),
    "0x00,0x00,0x43,0x00" to 	Pair("%3.1f",	"(BYTE[2]*256+BYTE[3])*100/255.0;"),
    "0x00,0x00,0x44,0x00" to 	Pair("%1.3f",	"(BYTE[2]*256+BYTE[3])*2.0/65536;"),
    "0x00,0x00,0x45,0x00" to 	Pair("%3.1f",	"BYTE[2]*100.0/255;"),
    "0x00,0x00,0x46,0x00" to 	Pair("%d",	"BYTE[2]-40;"),
    "0x00,0x00,0x47,0x00" to 	Pair("%3.1f",	"BYTE[2]*100.0/255;"),
    "0x00,0x00,0x48,0x00" to 	Pair("%3.1f",	"BYTE[2]*100.0/255;"),
    "0x00,0x00,0x49,0x00" to 	Pair("%3.1f",	"BYTE[2]*100.0/255;"),
    "0x00,0x00,0x4A,0x00" to 	Pair("%3.1f",	"BYTE[2]*100.0/255;"),
    "0x00,0x00,0x4B,0x00" to 	Pair("%3.1f",	"BYTE[2]*100.0/255;"),
    "0x00,0x00,0x4C,0x00" to 	Pair("%3.1f",	"BYTE[2]*100.0/255;"),
    "0x00,0x00,0x4D,0x00" to 	Pair("%d",	"BYTE[2]*256+BYTE[3];"),
    "0x00,0x00,0x4E,0x00" to 	Pair("%d",	"BYTE[2]*256+BYTE[3];")

)