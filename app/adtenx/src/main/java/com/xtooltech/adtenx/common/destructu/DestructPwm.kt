package com.xtooltech.adtenx.common.destructu

import java.lang.Exception


/**
 * PWM解包
 */
class DestructPwm : DestructBase(){

    /** 解析VinCode */
    override fun parseVin(data:List<ByteArray?>):String = commonParseVin(data)
}