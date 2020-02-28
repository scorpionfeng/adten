package com.xtooltech.adten.common.ble

import com.xtooltech.ad10.BleCallback

interface BleListener :BleCallback {
    fun onBleError(content:String)
}