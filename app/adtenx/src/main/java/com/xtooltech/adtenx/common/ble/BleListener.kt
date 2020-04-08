package com.xtooltech.adten.common.ble

import com.xtooltech.adtenx.plus.BleCallback


/**
 * 扩展蓝牙错误回调
 */
interface BleListener : BleCallback {
    fun onBleError(content:String)
}