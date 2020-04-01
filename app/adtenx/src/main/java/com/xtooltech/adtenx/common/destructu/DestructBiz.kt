package com.xtooltech.adtenx.common.destructu


interface DestructBiz {

    /** 解析VIN */
    fun parseVin(data:List<ByteArray?>):String
}