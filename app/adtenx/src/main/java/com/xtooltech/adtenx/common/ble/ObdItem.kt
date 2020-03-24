package com.xtooltech.adtenx.common.ble

data class ObdItem(var kind:Byte, var title:String, var selected:Boolean, var content:String, var symbol:String, var index:String, var obd:List<Byte> = mutableListOf())