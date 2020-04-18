package com.xtooltech.adtenx.common.destructu

import android.util.Log
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
        return Pair(0, listOfNotNull())
    }
}