package com.xtooltech.adten.module.splash

import android.view.View
import androidx.lifecycle.ViewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.xtooltech.adten.App
import com.xtooltech.adten.BR
import com.xtooltech.adten.R
import com.xtooltech.adten.databinding.ActivityWelcomeBinding
import com.xtooltech.adten.util.PATH_HOME
import com.xtooltech.adten.util.PATH_WELCOME
import com.xtooltech.adten.util.ProxyPreference
import com.xtooltech.base.BaseVMActivity
import com.xtooltech.base.util.toast
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.item_welcome_four.view.*

class WelcomeViewModel : ViewModel() {

}

@Route(path = PATH_WELCOME)
class WelcomeActivity : BaseVMActivity<ActivityWelcomeBinding, WelcomeViewModel>() {

    private var firstEnter by ProxyPreference("first_enter",true)
    private val viewEnd:View= View.inflate(App.instance,R.layout.item_welcome_four,null)

   private val views:List<View> = listOf(
        View.inflate(App.instance,R.layout.item_welcome_one,null),
        View.inflate(App.instance,R.layout.item_welcome_two,null),
        View.inflate(App.instance,R.layout.item_welcome_three,null),
        viewEnd
    )

    override fun initView() {

        flow.adapter=WelcomeAdapter(views)
        flow.setFlowIndicator(circle)
        viewEnd.button.setOnClickListener{
            toast("click me")
            ARouter.getInstance().build(PATH_HOME).navigation()
            firstEnter=false
            finish()
        }

    }

    override fun initData() {

    }

    override fun getLayoutId(): Int = R.layout.activity_welcome

    override fun getBindingId(): Int = BR.model




}
