package com.xtooltech.adten.module.splash

import androidx.lifecycle.ViewModel
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.adten.R
import com.xtooltech.adten.BR
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.adten.util.PATH_WELCOME
import com.xtooltech.adten.databinding.ActivityWelcomeBinding
import com.xtooltech.base.util.toast

class WelcomeViewModel : ViewModel() {

}

@Route(path = PATH_WELCOME)
class WelcomeActivity : BaseVMActivity<ActivityWelcomeBinding, WelcomeViewModel>() {

    override fun initView() {
        toast("welcome")
    }

    override fun initData() {}

    override fun getLayoutId(): Int = R.layout.activity_welcome

    override fun getBindingId(): Int = BR.model
}
