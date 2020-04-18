package com.xtooltech.adtenx.common.destructu

import java.lang.Exception


class DestructUnknow :DestructBiz{

    /** 解析VinCode */
    override fun parseVin(data:List<ByteArray?>):String="未知"

    override fun parseTrobCode(cmd: Byte, data: List<ByteArray?>): Pair<Int, List<String>> {
        return Pair(0, listOfNotNull())
    }
}