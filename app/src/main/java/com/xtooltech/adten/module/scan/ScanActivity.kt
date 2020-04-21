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
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.location.LocationManager
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kaopiz.kprogresshud.KProgressHUD
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.common.ble.BleListener
import com.xtooltech.adten.databinding.ActivityScanBinding
import com.xtooltech.adten.util.*
import com.xtooltech.adtenx.common.ble.*
import com.xtooltech.adtenx.plus.BleConnection
import com.xtooltech.adtenx.plus.Communication
import com.xtooltech.adtenx.plus.Utils
import com.xtooltech.adtenx.util.toHex
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast
import java.io.*
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

    private val PATH = Environment.getExternalStorageDirectory().path


    @RequiresApi(Build.VERSION_CODES.M)
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

         isStoragePermissionGranted()


    }


    private fun isStoragePermissionGranted():Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            val context = getApplicationContext();
            var readPermissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            var writePermissionCheck = ContextCompat.checkSelfPermission(context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (readPermissionCheck == PackageManager.PERMISSION_GRANTED && writePermissionCheck == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }






    override fun onResume() {
        super.onResume()
        Thread{
            startScan()
        }.start()
    }


    private fun initBle() {
        takeIf {  Build.VERSION.SDK_INT>=Build.VERSION_CODES.M }?.apply {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                enableLocationPermission = false
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_COARSE_LOCATION)
            }
            //判断位置是否开启
            val locationManager =getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager != null) {
                val isGpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                val isNetWorkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                if (!(isGpsProvider || isNetWorkProvider)) {
                    enableLocation = false
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent, REQUEST_COARSE_LOCATION)
                }
            }
        }

        //蓝牙权限，判断蓝牙是否开启
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        (Build.VERSION.SDK_INT<21).trueLet {
            mScanCallback = LeScanCallback { device, rssi, scanRecord ->
                    val name = device.name
                var destDevices="ED:67:17:22:D2:27"
                    if (name != null && name == "AD10" || scanRecord[5] == 0x10.toByte() && scanRecord[6] == 0x01.toByte()&&destDevices==device.address ) {
                        val mac = device.address


                        runOnUiThread {
//                            if (rssiOn  && device.address=="ED:67:17:22:DD:F2") {
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
                            Log.i("xtool","mac address= "+mac)
                            runOnUiThread {
                                if (rssiOn ) {
//                                if (rssiOn && result.device.address=="ED:67:17:22:DD:F2" ) {
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
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH)
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
                //ED:67:17:22:DD:F2-> 白色
                ObdManger.getIns().deviceAddress=address
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

        Log.v("juno","onRequestPermissionsResult requestCode ： " + requestCode
                + " Permission: " + permissions[0] + " was " + grantResults[0]
                + " Permission: " + permissions[1] + " was " + grantResults[1]
        );
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //resume tasks needing this permission
            var directory = File(PATH);
            var files = directory.listFiles();
            Log.i("juno", "After PERMISSION_GRANTED files : " + (files?.size ?: 0))
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
           ObdManger.getIns().connect(this@ScanActivity,this@ScanActivity)

       }

    }

    override fun onBleError(content: String) {
        printMessage("ble error= $content")
        hud.isShowing.trueLet {
            hud.setLabel("没有地址")
            hud.dismiss()
        }
    }

    override fun onCharacteristicChanged(p0: ByteArray?) {
        printMessage("接收"+ Utils.debugByteData(p0))

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
        vm.status.value="连接状态:已找到UUID $p0"
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



    fun onScan(view: View) {
        hud.show()
        Thread{
            var kind = ObdManger.getIns().scan()
            when(kind){
                OBD_EXT_CAN ->vm.status.postValue("扩展CAN")
                OBD_STD_CAN ->vm.status.postValue("标准CAN")
                OBD_ISO ->vm.status.postValue("ISO")
                OBD_KWP ->vm.status.postValue("KWP")
                OBD_PWM ->vm.status.postValue("PWM")
                OBD_VPW ->vm.status.postValue("VPW")
                OBD_UNKNOWN ->vm.status.postValue("扫描失败")
            }

            handler.post {
                hud.dismiss()
            }

            if (kind!= OBD_UNKNOWN) {
                Log.i("Communication","进入"+ nameMap.get(kind))
                ARouter.getInstance().build(PATH_DIY).navigation()
            }


        }.start()

    }

    fun startFirmwareUpdate(v:View) {

       var copyed= copyApkFromAssets(this,"AD10_BLE_OBD_V01.05.bin",Environment.getExternalStorageDirectory().absolutePath + "/AD10")
        copyed.falseLet { return }

        var mSelectedFilePath=""
        val array: MutableList<String> = ArrayList()
        val homeDir = File(Environment.getExternalStorageDirectory().absolutePath + "/AD10")
        if (homeDir.exists() && homeDir.isDirectory) {
            val files = homeDir.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isFile) {
                        if (file.name.toLowerCase().endsWith(".bin")) {
                            array.add(file.name)
                        } else if (file.name.toLowerCase().endsWith(".zip")) {
                            array.add(file.name)
                        }
                    }
                }
            }
        } else {
            homeDir.mkdir()
        }
        if (array.isEmpty()) {
            mSelectedFilePath = ""
            AlertDialog.Builder(this)
                .setTitle("固件升级")
                .setMessage("未找到固件!\n请将固件放置于外部存储空间的AD10的文件夹下面")
                .setPositiveButton("确定", null)
                .show()
        } else {
            var items= arrayOfNulls<String>(array.size)
            array.forEachIndexed{index, s ->  items[index]=s}
            var finalItems=array.toTypedArray()
            mSelectedFilePath = finalItems[0]
            AlertDialog.Builder(this)
                .setTitle("AD10下的固件")
                .setSingleChoiceItems(items, 0
                ) { dialog, which -> mSelectedFilePath = finalItems[which] }
                .setPositiveButton("确定") { dialogInterface, i ->
                    mSelectedFilePath = "$homeDir/$mSelectedFilePath"
                    hud.setLabel("准备固件升级...").show()
                        updateFirmware(mSelectedFilePath)
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }
    fun startFirmwareUpdate_sd(v:View) {
        var mSelectedFilePath=""
        val array: MutableList<String> = ArrayList()
        val homeDir = File(Environment.getExternalStorageDirectory().absolutePath + "/AD10")
        if (homeDir.exists() && homeDir.isDirectory) {
            val files = homeDir.listFiles()
            if (files != null) {
                for (file in files) {
                    if (file.isFile) {
                        if (file.name.toLowerCase().endsWith(".bin")) {
                            array.add(file.name)
                        } else if (file.name.toLowerCase().endsWith(".zip")) {
                            array.add(file.name)
                        }
                    }
                }
            }
        } else {
            homeDir.mkdir()
        }
        if (array.isEmpty()) {
            mSelectedFilePath = ""
            AlertDialog.Builder(this)
                .setTitle("固件升级")
                .setMessage("未找到固件!\n请将固件放置于外部存储空间的AD10的文件夹下面")
                .setPositiveButton("确定", null)
                .show()
        } else {
            var items= arrayOfNulls<String>(array.size)
            array.forEachIndexed{index, s ->  items[index]=s}
            var finalItems=array.toTypedArray()
            mSelectedFilePath = finalItems[0]
            AlertDialog.Builder(this)
                .setTitle("AD10下的固件")
                .setSingleChoiceItems(items, 0
                ) { dialog, which -> mSelectedFilePath = finalItems[which] }
                .setPositiveButton("确定") { dialogInterface, i ->
                    mSelectedFilePath = "$homeDir/$mSelectedFilePath"
                    hud.setLabel("准备固件升级...").show()
                        updateFirmware(mSelectedFilePath)
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }


     fun copyApkFromAssets(context:Context, fileName:String, path:String):Boolean {
        var copyIsFinish = false;
        try {
            var inputStream = context.resources.assets.open(fileName);
            var file = File(path+File.separator+fileName);
            file.createNewFile()
            var fos = FileOutputStream(file);
            var temp = ByteArray(1024)
            var i = 0;
            while ((inputStream.read(temp)) !=  -1) {
                fos.write(temp);
            }
            fos.close();
            inputStream.close();
            copyIsFinish = true;
        } catch (e:IOException) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }



    private fun updateFirmware(mSelectedFilePath: String) {
            Thread(Runnable {
                val file = File(mSelectedFilePath)
                var finalSuccess = false
                if (ObdManger.getIns().initFirmwareUpdate(file)== InitState.UPDATE) {
                    ObdManger.getIns().burnBin(file,object :ObdManger.OnBurnCallBack{
                        override fun progress(progress: Double) {
                            runOnUiThread {
                                hud.setLabel("burning $progress")
                            }
                        }

                        override fun isSuccess(isSucced: Boolean) {
                            finalSuccess=isSucced
                            runOnUiThread {
                                hud.dismiss()
                                AlertDialog.Builder(this@ScanActivity)
                                    .setMessage(if (finalSuccess) "更新固件成功" else "更新固件失败")
                                    .setPositiveButton("确定", null)
                                    .show()
                            }
                        }

                    })

                } else {
                    runOnUiThread {
                        hud.dismiss()
                        AlertDialog.Builder(this@ScanActivity)
                            .setMessage("初始化固件升级失败")
                            .setPositiveButton("确定", null)
                            .show()
                    }
                }
            }).start()
    }

    fun click_readBinVersion(view: View) {
        Thread Thread@{
            var dir=File(Environment.getExternalStorageDirectory().absolutePath + "/AD10")
            takeUnless { dir.exists() }?.apply { dir.mkdir() }
            var copyed= copyApkFromAssets(this,"AD10_BLE_OBD_V01.05.bin",Environment.getExternalStorageDirectory().absolutePath + "/AD10")
            copyed.falseLet { return@Thread }
            var mSelectedFilePath=""
            val array: MutableList<String> = ArrayList()
            val binFile=File(dir,"AD10_BLE_OBD_V01.05.bin")

          var version=  ObdManger.getIns().readBinFileVersion(binFile.absolutePath)
            //AD10_BLE_OBD V01.05

            Log.i("Communication","file version=["+version+"]")
        }.start()

    }

    fun click_reset(view: View) {
        ObdManger.getIns().reset()
    }

    fun click_avgful(view: View) {
        ARouter.getInstance().build(PATH_DIY_FUEL).navigation()
    }
}
