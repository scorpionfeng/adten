package com.xtooltech.adten.module.dash

import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.navigation.NavigationView
import com.kaopiz.kprogresshud.KProgressHUD
import com.xtooltech.adten.App
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.common.ble.BleListener
import com.xtooltech.adtenx.common.ble.ObdManger
import com.xtooltech.adten.databinding.ActivityDashBinding
import com.xtooltech.adten.util.*
import com.xtooltech.adtenx.common.obd.OBDUtil
import com.xtooltech.adtenx.plus.Utils
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast
import com.xtooltech.widget.UniversalAdapter


data class FunItem(val id:Int, val title:String, val icon:Int,val content:String="")

class DashViewModel : ViewModel() {

}

@Route(path = PATH_DASH)
class DashActivity : BaseVMActivity<ActivityDashBinding, DashViewModel>(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var hud: KProgressHUD

    private var bleConnected =false
    var deviceAddress by ProxyPreference(BLE_ADDRESS,"")

    private val data= listOf(
        FunItem(1,"仪表盘",R.mipmap.ic_launcher),
        FunItem(2,"车辆检测",R.mipmap.ic_launcher),
        FunItem(3,"车辆数据",R.mipmap.ic_launcher),
        FunItem(4,"性能测试",R.mipmap.ic_launcher),
        FunItem(5,"行程记录",R.mipmap.ic_launcher),
        FunItem(6,"排行榜",R.mipmap.ic_launcher),
        FunItem(7,"DIY模式",R.mipmap.ic_launcher)
    )

    val menuAdapter= UniversalAdapter(data,R.layout.item_dash,BR.model)

    override fun initView() {

        hud=KProgressHUD.create(this)
            .setCancellable(false)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
            .setLabel("正在连接蓝牙...")

        setSupportActionBar(vb.include.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, vb.drawerLayout, vb.include.toolbar, R.string.app_name, R.string.app_name
        )
        vb.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

       // vb.drawerLayout.openDrawer(GravityCompat.START)

        vb.navView.setNavigationItemSelectedListener(this)

        vb.include.grid.adapter=menuAdapter

        vb.include.grid.layoutManager= GridLayoutManager(this,2)
        menuAdapter.setOnItemClick{
            _,item,index->
            when(item.id){
                7->  ARouter.getInstance().build(PATH_DIY).navigation()
                1->  ARouter.getInstance().build(PATH_DASH).navigation();
            }

        }

        Thread {
            val isReadSuccess: Boolean = OBDUtil.readDataToMemory(this)
        }.start()

    }

    override fun initData() {}


    override fun onResume() {
        super.onResume()

        App.instance.isGuest.falseLet {
            deviceAddress.isEmpty().trueLet {
                AlertDialog.Builder(this@DashActivity)
                    .setMessage("进入设备连接页面")
                    .setPositiveButton("是") { dialog, which ->
                        ARouter.getInstance().build(PATH_SCAN).navigation()
                    }
                    .setNegativeButton("否") { dialog, which ->

                    }
                    .setOnCancelListener {

                    }
                    .show()

            }.elseLet {

                if (bleConnected) {
                    return@elseLet
                }

                hud.show()

                ObdManger.getIns().connect(this@DashActivity,object :BleListener{
                    override fun onBleError(content: String) {
                        printMessage("112 onerror $content")
                        hud.dismiss()
                    }

                    override fun onCharacteristicChanged(p0: ByteArray?) {
                        printMessage("onCharacteristicChanged ${Utils.debugByteData(p0)}")
                    }

                    override fun onNotFoundUuid() {
                    }

                    override fun onConnected() {
                        toast("ble connected")
                        hud.dismiss()
                        bleConnected=true
                    }

                    override fun onDisconnected() {
                        hud.dismiss()
                        bleConnected=false
                    }

                    override fun onConnectTimeout() {
                    }

                    override fun onFoundUuid(p0: Int) {
                        printMessage("onFoundUUid= $p0")
                        hud.dismiss()
                    }
                })
            }
        }


    }


    override fun getLayoutId(): Int = R.layout.activity_dash

    override fun getBindingId(): Int = BR.model
    override fun onNavigationItemSelected(item: MenuItem): Boolean {


        vb.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
