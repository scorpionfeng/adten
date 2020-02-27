package com.xtooltech.adten.module.scan

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.LeScanCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.common.ble.DETECT_RSSI
import com.xtooltech.adten.common.ble.REQUEST_COARSE_LOCATION
import com.xtooltech.adten.common.ble.REQUEST_ENABLE_BLUETOOTH
import com.xtooltech.adten.common.ble.REQUEST_EXTERNAL_STORAGE
import com.xtooltech.adten.databinding.ActivityScanBinding
import com.xtooltech.adten.module.home.HomeActivity
import com.xtooltech.adten.util.PATH_SCAN
import com.xtooltech.adten.util.ProxyPreference
import com.xtooltech.adten.util.falseLet
import com.xtooltech.adten.util.trueLet
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import java.util.*

class ConnectViewModel : ViewModel() {

    val deviceName by lazy {
        MutableLiveData<String>()
    }

    fun bleScan(view: View){
    }

}

@Route(path = PATH_SCAN)
class ScanActivity : BaseVMActivity<ActivityScanBinding, ConnectViewModel>() {

    private var enableBle: Boolean =false
    private lateinit var mScanCallback: Any
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var enableLocation: Boolean=true
    private var enableLocationPermission: Boolean   =true
    var deviceId by ProxyPreference("deviceId","")
    var rssiOn by ProxyPreference(DETECT_RSSI,true)

    override fun initView() {
        deviceId.isEmpty().falseLet {
            vm.deviceName.value=deviceId
        }

        initBle()

    }


    override fun onResume() {
        super.onResume()
        Thread{
            startScan()
        }.start()
    }


    private fun initBle() {
        takeIf {  Build.VERSION.SDK_INT>=23 }.apply {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                enableLocationPermission = false
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_COARSE_LOCATION
                )
            }
            //判断位置是否开启
            val locationManager =getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager != null) {
                val isGpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetWorkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                if (!(isGpsProvider || isNetWorkProvider)) {
                    enableLocation = false
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent,REQUEST_COARSE_LOCATION)
                }
            }
        }

        //蓝牙权限，判断蓝牙是否开启
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        (Build.VERSION.SDK_INT<21).trueLet {
            mScanCallback = LeScanCallback { device, rssi, scanRecord ->
                    val name = device.name
                    if (name != null && name == "AD10" || scanRecord[5] == 0x10.toByte() && scanRecord[6] == 0x01.toByte()
                    ) {
                        val mac = device.address
                        runOnUiThread {
                            if (rssiOn ) {
                                stopScan()
                                confirmDevice(name,mac)
                            }
                        }
                    }
                }
        }.elseLet {
            mScanCallback = object : ScanCallback() {
                override fun onScanResult(
                    callbackType: Int,
                    result: ScanResult
                ) {
                    var scanRecord: ByteArray? = null
                    if (result.scanRecord != null) {
                        scanRecord = result.scanRecord!!.bytes
                    }
                    if (scanRecord != null) { //Log.d(TAG, "onScanResult: " + result);
                        val name = result.device.name
                        if (name != null && name == "AD10" || scanRecord[5] == 0x10.toByte() && scanRecord[6] == 0x01.toByte()
                        ) {
                            val mac = result.device.address
                            runOnUiThread {
                                if (rssiOn ) {
                                    stopScan()
                                    confirmDevice(name,mac)
                                }
                            }
                        }
                    }
                }

                override fun onBatchScanResults(results: List<ScanResult>) {
                    printMessage("onBatchScanResults: $results")
                }

                override fun onScanFailed(errorCode: Int) {
                    printMessage("onScanFailed: $errorCode")
                }
            }
        }

        if (bluetoothAdapter.isEnabled) {
            enableBle = true
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent,REQUEST_ENABLE_BLUETOOTH)
        }

    }

    private fun stopScan() {
        if (enableLocationPermission && enableLocation && enableBle) {
            if (Build.VERSION.SDK_INT < 21) {
                bluetoothAdapter.stopLeScan(mScanCallback as BluetoothAdapter.LeScanCallback)
            } else {
                bluetoothAdapter.bluetoothLeScanner.stopScan(mScanCallback as ScanCallback)
            }
        }
    }

    private fun confirmDevice(name:String,address:String) {
        AlertDialog.Builder(this)
            .setMessage(name)
            .setPositiveButton("是") { dialog, which ->
                stopScan()
            }
            .setNegativeButton("否") { dialog, which ->
                Thread(Runnable { startScan() }).start()
            }
            .setOnCancelListener {
                Thread(Runnable { startScan() }).start()
            }
            .show()
    }

    private fun startScan() {
        if (enableLocationPermission && enableLocation && enableBle && bluetoothAdapter != null) {
            if (Build.VERSION.SDK_INT < 21) {
                bluetoothAdapter.startLeScan(mScanCallback as (BluetoothAdapter.LeScanCallback))
            } else {
                val filters: MutableList<ScanFilter> = ArrayList()
                val filter =  ScanFilter.Builder().build()
                filters.add(filter)
                var settings: ScanSettings
                takeIf { rssiOn }.apply {  settings = ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build() }
                bluetoothAdapter.bluetoothLeScanner .startScan(filters, settings, mScanCallback as ScanCallback)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocationPermission = true
            } else {
                finish()
            }
        } else if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    override fun onActivityResult(requestCode: Int,resultCode: Int,data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH || requestCode == REQUEST_COARSE_LOCATION) {
            if (resultCode != Activity.RESULT_OK) {
                finish()
            } else {
                if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
                    enableBle = true
                } else {
                    enableLocation = true
                }
            }
        }
    }


    override fun initData() {}

    override fun getLayoutId(): Int = R.layout.activity_scan

    override fun getBindingId(): Int = BR.model
    fun doScan(view: View) {
        Thread{
            startScan()
        }.run()
    }
}
