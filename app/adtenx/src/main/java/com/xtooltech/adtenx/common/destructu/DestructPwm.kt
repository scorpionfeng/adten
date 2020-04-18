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
                    answer = bytes.contains(0x49.toByte())
                    takeIf { answer }?.apply {
                        vinList.addAll(bytes.slice(9 until bytes.size-1))
                    } ?:apply {
                        return@loop
                    }
            }
        }

        takeIf { vinList.isNotEmpty() }?.apply {
            try {
                vinList.take(17).forEach{
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