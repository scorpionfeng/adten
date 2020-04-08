package com.xtooltech.adtenx.common.destructu

import java.lang.Exception


/**
 * KWP解包
 */
class DestructKwp : DestructBase(){

    /** 解析VinCode */
    override fun parseVin(data:List<ByteArray?>):String = commonParseVin(data)
}