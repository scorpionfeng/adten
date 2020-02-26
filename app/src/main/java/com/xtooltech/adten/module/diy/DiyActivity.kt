package com.xtooltech.adten.module.diy

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.xtooltech.base.BaseVMActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.databinding.ActivityDiyBinding
import com.xtooltech.adten.module.home.MenuItem
import com.xtooltech.adten.util.PATH_DIY
import com.xtooltech.base.util.printMessage
import com.xtooltech.base.util.toast
import com.xtooltech.widget.UniversalAdapter

class DiyViewModel : ViewModel() {

}


@Route(path = PATH_DIY)
class DiyActivity : BaseVMActivity<ActivityDiyBinding, DiyViewModel>() {

    private val data= listOf(
        MenuItem(1,"数据流",R.mipmap.ic_launcher),
        MenuItem(2,"冻结帧",R.mipmap.ic_launcher),
        MenuItem(3,"MIL状态",R.mipmap.ic_launcher),
        MenuItem(4,"烟雾测试",R.mipmap.ic_launcher)
    )

    val menuAdapter= UniversalAdapter(data,R.layout.item_home,BR.model)

    override fun initView() {
        vb.grid.adapter=menuAdapter

        menuAdapter.setOnInnerItemClick { _, _, position -> toast("$position").also { printMessage("$position--->") } }
        vb.grid.layoutManager= GridLayoutManager(this,2)
        menuAdapter.setOnItemClick{
_,_,_->
            toast("click")
        }

    }

    override fun initData() {}

    override fun getLayoutId(): Int = R.layout.activity_diy

    override fun getBindingId(): Int = BR.model
}
