package com.xtooltech.adten.module.home

import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.navigation.NavigationView
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.databinding.ActivityHomeBinding
import com.xtooltech.adten.util.*
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast
import com.xtooltech.widget.UniversalAdapter
import kotlinx.android.synthetic.main.home_bar.*


data class MenuItem(val id:Int,val title:String,val icon:Int)

class HomeViewModel : ViewModel() {

}

@Route(path = PATH_HOME)
class HomeActivity : BaseVMActivity<ActivityHomeBinding, HomeViewModel>(), NavigationView.OnNavigationItemSelectedListener {

    var adtenid by ProxyPreference("adtenid","")

    private val data= listOf(
        MenuItem(1,"仪表盘",R.mipmap.ic_launcher),
        MenuItem(2,"车辆检测",R.mipmap.ic_launcher),
        MenuItem(3,"车辆数据",R.mipmap.ic_launcher),
        MenuItem(4,"性能测试",R.mipmap.ic_launcher),
        MenuItem(5,"行程记录",R.mipmap.ic_launcher),
        MenuItem(6,"排行榜",R.mipmap.ic_launcher)
    )

    val menuAdapter= UniversalAdapter(data,R.layout.item_home,BR.model)

    override fun initView() {

        setSupportActionBar(vb.include.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, vb.drawerLayout, vb.include.toolbar, R.string.app_name, R.string.app_name
        )
        vb.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        vb.drawerLayout.openDrawer(GravityCompat.START)

        vb.navView.setNavigationItemSelectedListener(this)

        vb.include.grid.adapter=menuAdapter

        menuAdapter.setOnInnerItemClick { _, _, position -> toast("$position").also { printMessage("$position--->") } }
        vb.include.grid.layoutManager= GridLayoutManager(this,2)
        menuAdapter.setOnItemClick{
            _,item,index->

            ARouter.getInstance().build(PATH_DIY).navigation();
        }

    }

    override fun initData() {}


    override fun onResume() {
        super.onResume()
        adtenid.isEmpty().trueLet {
            ARouter.getInstance().build(PATH_SCAN).navigation()
        }
    }


    override fun getLayoutId(): Int = R.layout.activity_home

    override fun getBindingId(): Int = BR.model
    override fun onNavigationItemSelected(item: MenuItem): Boolean {


        vb.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
