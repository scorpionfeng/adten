package com.xtooltech.adtenx.common.destructu


/**
 * 解包业务
 */
interface DestructBiz {

    /** 解析VIN */
    fun parseVin(data:List<ByteArray?>):String

    /** 解析故障码 */
    fun parseTrobCode(cmd:Byte,data:List<ByteArray?>):Pair<Int,List<String>>
}