package com.xtooltech.adtenx.common.destructu


interface DestructBiz {

    /** 解析VIN */
    fun parseVin(data:List<ByteArray?>):String

    /** 解析故障码 */
//    fun parseTrobCode(data:List<ByteArray?>):Pair<Int,List<String>>
}