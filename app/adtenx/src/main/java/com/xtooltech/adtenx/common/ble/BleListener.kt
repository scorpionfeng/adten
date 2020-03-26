package com.xtooltech.adten.common.ble

import com.xtooltech.adtenx.plus.BleCallback


interface BleListener : BleCallback {
    fun onBleError(content:String)
}