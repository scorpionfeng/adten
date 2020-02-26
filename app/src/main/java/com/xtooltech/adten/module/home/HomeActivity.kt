package com.xtooltech.adten.module.home

import androidx.lifecycle.ViewModel
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.adten.R
import com.xtooltech.adten.BR
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.adten.util.PATH_HOME
import com.xtooltech.adten.databinding.ActivityHomeBinding
import com.xtooltech.base.util.toast

class HomeViewModel : ViewModel() {

}

@Route(path = PATH_HOME)
class HomeActivity : BaseVMActivity<ActivityHomeBinding, HomeViewModel>() {

    override fun initView() {
        toast("I'm home")
    }

    override fun initData() {}

    override fun getLayoutId(): Int = R.layout.activity_home

    override fun getBindingId(): Int = BR.model
}
