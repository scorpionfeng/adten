package com.xtooltech.adtenx.common.destructu

import java.lang.Exception


/**
 * ISO解包
 */
class DestructIso : DestructBase(){

    /** 解析VinCode */
    override fun parseVin(data:List<ByteArray?>):String = commonParseVin(data)
}