package com.xtooltech.adtenx.common.destructu

import android.util.Log
import com.xtooltech.adtenx.util.parse2BizSingle
import com.xtooltech.adtenx.util.toHex
import java.lang.Exception


class DestructVpw :DestructBiz{

    /** 解析VinCode */
    override fun parseVin(data:List<ByteArray?>):String{
        var vinCode=""
        var vinList= mutableListOf<Byte>()
        var answer=false
        data.forEachIndexed loop@{index, bytes ->
            bytes?.apply {
                takeIf { index==0 }?.apply {
                    answer = bytes.contains(0x49.toByte())
                    takeIf { answer }?.apply {
                        vinList.addAll(bytes.slice(bytes.size-2 until bytes.size-1))
                    } ?:apply {
                        return@loop
                    }
                }
                takeIf { index>0 &&  answer }?.apply {
                    vinList.addAll(bytes.slice(6 until bytes.size-1))
                }
            }
        }

        takeIf { vinList.isNotEmpty() }?.apply {
            try {
                vinList.forEach {
                    vinCode += String.format("%c", it)
                }
            }catch (e:Exception){
                println("parse vin error")
            }

        }
        return vinCode

    }

    override fun parseTrobCode(cmd: Byte, data: List<ByteArray?>): Pair<Int, List<String>> {

        //60 0A 0B 41 6B 10 47 01 93 12 33 00 00 0C
        var flag_andwer=false
        var list_raw= mutableListOf<Byte>()
        var list_code= mutableListOf<String>()
        var data_temp=""
        data.distinct().forEach {
            it?.apply {
                var data_parsed= parse2BizSingle(it)
                flag_andwer=  data_parsed.contains((0x40.toByte()+cmd).toByte())
                takeUnless { flag_andwer }?.apply { return Pair(0, listOfNotNull()) }
                list_raw.addAll(data_parsed.slice(1 .. data_parsed.size-1))
            }
        }

        takeIf { list_raw.isNotEmpty() }?.apply {
            for (i in list_raw.indices step 2){
                if(list_raw.size <=i+1){
                    continue
                }
                var data_head = list_raw[i].toInt()
                var data_end = list_raw[i+1].toInt()
                if(data_head==0x00 && data_end == 0x00 ){
                    continue
                }
                val codeHex=(data_head.shl(8).and(0xffff)).or(data_end.toInt().and(0xff))
                try {
                    data_temp = when {
                        codeHex < 0x4000 -> { // P
                            String.format("P%04X", codeHex)
                        }
                        codeHex < 0x8000 -> { // C
                            String.format("C%04X", codeHex - 0x4000)
                        }
                        codeHex < 0xC000 -> { // B
                            String.format("B%04X", codeHex - 0x8000)
                        }
                        else -> { // U
                            String.format("U%04X", codeHex - 0xC000)
                        }
                    }

                    list_code.add(data_temp)
                }catch (e: Exception){
                    println("format exception")
                }
            }
        }

        return Pair(list_code.size, list_code)
    }
}