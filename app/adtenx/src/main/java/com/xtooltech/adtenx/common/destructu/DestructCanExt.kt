package com.xtooltech.adtenx.common.destructu

import java.lang.Exception


/**
 * CAN扩展解包
 */
class DestructCanExt : DestructBase() {

    /** 解析VinCode */
    override fun parseVin(data:List<ByteArray?>):String = commonParseVin(data)

}