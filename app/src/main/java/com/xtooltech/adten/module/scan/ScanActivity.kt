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
import android.os.Handler
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.kaopiz.kprogresshud.KProgressHUD
import com.xtooltech.ad10.BleConnection
import com.xtooltech.ad10.Communication
import com.xtooltech.ad10.Utils
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.common.ble.*
import com.xtooltech.adten.databinding.ActivityScanBinding
import com.xtooltech.adten.module.diy.ObdItem
import com.xtooltech.adten.util.*
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast
import kotlinx.coroutines.launch
import java.util.*

class ScanViewModel : ViewModel() {

    val deviceName by lazy {
        MutableLiveData<String>()
    }
    val status by lazy {
        MutableLiveData<String>()
    }


}

@Route(path = PATH_SCAN)
class ScanActivity : BaseVMActivity<ActivityScanBinding, ScanViewModel>(), BleListener {


    private val handler = Handler()
    private lateinit var communication: Communication
    private lateinit var bleConnection: BleConnection
    private  lateinit var  hud:KProgressHUD

    private var enableBle: Boolean =false
    private lateinit var mScanCallback: Any
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var enableLocation: Boolean=true
    private var enableLocationPermission: Boolean   =true
    var deviceAddress by ProxyPreference(BLE_ADDRESS,"")
    var rssiOn by ProxyPreference(DETECT_RSSI,true)


    override fun initView() {



        hud=KProgressHUD.create(this)
            .setCancellable(false)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .setLabel("正在加载...")
        deviceAddress.isEmpty().falseLet {
            vm.deviceName.value=deviceAddress
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
                vm.deviceName.value=name
                deviceAddress=address
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

    fun doConnect(view: View) {

       takeIf { deviceAddress.isNotEmpty() }.apply {
           hud.show()
//           bleConnection=BleConnection(this,true,deviceAddress)
//           bleConnection.addBleCallback(this)
//           communication=Communication(bleConnection)
//           bleConnection.start()

           ObdManger.getIns().connect(this@ScanActivity,this@ScanActivity)

       }

    }

    override fun onBleError(content: String) {
        printMessage("ble error= $content")
    }

    override fun onCharacteristicChanged(p0: ByteArray?) {
        printMessage("接收"+Utils.debugByteData(p0))
//        BleManger.getIns().append(p0)
//        communication?.addByteArray(p0)

    }

    override fun onNotFoundUuid() {
        vm.status.value="连接状态:未找到UUID"
        showMessageFinish("未找到UUID")
    }

    override fun onConnected() {
        toast("已经连接")
        vm.status.value="连接状态:已连接"
    }

    override fun onDisconnected() {
        vm.status.value="连接状态:已断开"
    }

    override fun onConnectTimeout() {
        vm.status.value="连接状态:已超时"
    }

    override fun onFoundUuid(p0: Int) {
        hud.dismiss()
        vm.status.value="连接状态:已找到UUID"
    }

    fun showMessageFinish(message: String?) {
        hud.dismiss()
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(
                "确定"
            ) { dialogInterface, i ->
                bleConnection.removeBleCallback()
                bleConnection.stop()
                finish()
            }
            .setOnCancelListener {
                bleConnection.removeBleCallback()
                bleConnection.stop()
                finish()
            }
            .show()
    }

    fun clieckEnter(view: View) {

        hud.setLabel("进入"+ nameMap.get(ObdManger.getIns().currProto))

        Thread{

            var success = ObdManger.getIns().enter()

//            var success = communication?.enterPwmVpw(true)
            vm.status.postValue("enter ${nameMap.get(ObdManger.getIns().currProto)} is $success")
        }.start()

    }

    fun enterPwm2(view: View) {
        lifecycleScope.launch {
            hud.setLabel("进入pwm2")
            var succ=communication?.enterPwmVpw(true)
            vm.status.postValue("enter pwm2 is $succ")
        }

    }

    fun onScan(view: View) {
        Thread{
            var kind = ObdManger.getIns().scan()
            when(kind){
                OBD_EXT_CAN->vm.status.postValue("扩展CAN")
                OBD_STD_CAN->vm.status.postValue("标准CAN")
                OBD_ISO->vm.status.postValue("ISO")
                OBD_KWP->vm.status.postValue("KWP")
                OBD_PWM->vm.status.postValue("PWM")
                OBD_VPW->vm.status.postValue("VPW")
                OBD_UNKNOWN->vm.status.postValue("扫描失败")

            }
        }.start()

    }

    fun readVin(view: View) {

        Thread{

            var readVinCode = ObdManger.getIns().readVin()
            readVinCode.apply {
                vm.status.postValue("vin code =$this")
            }


        }.start()



    }

    fun onFuellevel(view: View) {

        Thread {

            var value = ObdManger.getIns().readFlowItem(ObdItem(0x2f, "燃油液位", false, "", "%", "0x00,0x00,0x2F,0x00"))
            value.apply {
                vm.status.postValue("燃油液位 =$this %")
            }

        }.start()

    }


}
