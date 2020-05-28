package com.xtooltech.adtenx.common.ble

/**
 * 数据请求项
 */
data class ObdItem(var kind:Byte,
                   var title:String,
                   var selected:Boolean,
                   var content:String,
                   var symbol:String,
                   var index:String,
                   var obd:List<Byte> = mutableListOf())