package com.xtooltech.adtenx.common.destructu

import java.lang.Exception


/**
 * can标准解包
 */
class DestructCanStd : DestructBase(){

    /** 解析VinCode */
    override fun parseVin(data:List<ByteArray?>):String = commonParseVin(data)
}