package com.xtooltech.adtenx.common.destructu


/**
 * 解包业务
 */
interface DestructBiz {

    /** 解析VIN */
    fun parseVin(data:List<ByteArray?>):String
}