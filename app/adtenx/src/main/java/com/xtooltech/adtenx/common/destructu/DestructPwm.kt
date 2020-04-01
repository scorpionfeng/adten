package com.xtooltech.adtenx.common.destructu

import java.lang.Exception


class DestructPwm :DestructBiz{

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
                        vinList.addAll(bytes.slice(bytes.size-3 until bytes.size))
                    } ?:apply {
                        return@loop
                    }
                }
                takeIf { index>0 &&  answer }?.apply {
                    vinList.addAll(bytes.slice(4 until bytes.size))
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
}