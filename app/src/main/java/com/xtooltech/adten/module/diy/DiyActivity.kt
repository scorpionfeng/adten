package com.xtooltech.adten.module.diy

import androidx.lifecycle.ViewModel
import com.xtooltech.base.BaseVMActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.databinding.ActivityDiyBinding
import com.xtooltech.adten.util.PATH_DIY

class DiyViewModel : ViewModel() {

}

@Route(path = PATH_DIY)
class DiyActivity : BaseVMActivity<ActivityDiyBinding, DiyViewModel>() {

    override fun initView() {}

    override fun initData() {}

    override fun getLayoutId(): Int = R.layout.activity_diy

    override fun getBindingId(): Int = BR.model
}
