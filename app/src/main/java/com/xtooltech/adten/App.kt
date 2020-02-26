package com.xtooltech.adten

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.xtooltech.adten.common.HttpManager
import kotlin.properties.Delegates

class App  :Application(){


    companion object{
        var instance:App by Delegates.notNull()
    }


    override fun onCreate() {
        super.onCreate()
        instance=this

        ARouter.init(this)
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openDebug()
        }

        HttpManager.get().init()

    }
}