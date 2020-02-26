package com.xtooltech.adten.module.splash

import com.alibaba.android.arouter.launcher.ARouter
import com.xtooltech.adten.util.PATH_HOME
import com.xtooltech.adten.util.PATH_WELCOME
import com.xtooltech.adten.R
import com.xtooltech.adten.util.*
import com.xtooltech.base.BaseRouteActivity

class SplashActivity : BaseRouteActivity() {

    var firstEnter by ProxyPreference("first_enter",true)

    var btid by ProxyPreference("btid","")


    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initData() {
    }

    override fun initView() {
    }

    override fun preLayout() {

        firstEnter.trueLet {

            ARouter.getInstance().build(PATH_WELCOME).navigation()

            firstEnter=false

        }.elseLeft {
                ARouter.getInstance().build(PATH_HOME).navigation()
        }
        return 

    }



}
