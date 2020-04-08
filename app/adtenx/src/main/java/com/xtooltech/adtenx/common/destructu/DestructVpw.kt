package com.xtooltech.adtenx.common.destructu

import java.lang.Exception


/**
 * VPW解包
 */
class DestructVpw : DestructBase(){

    /** 解析VinCode */
    override fun parseVin(data:List<ByteArray?>):String = commonParseVin(data)
}