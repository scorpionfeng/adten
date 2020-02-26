package com.xtooltech.adten

import com.xtooltech.adten.util.ProxyPreference
import com.xtooltech.adten.util.trueLet
import com.xtooltech.base.BaseRouteActivity
import com.xtooltech.base.util.toast

class SplashActivity : BaseRouteActivity() {

    var firstEnter by ProxyPreference("first_enter",false)


    override fun getLayoutId(): Int =R.layout.activity_main

    override fun initData() {
    }

    override fun initView() {
    }

    override fun preLayout() {

        firstEnter.trueLet {
            toast("is true")

        }.elseLeft {

            toast("is false")
        }

    }



}
