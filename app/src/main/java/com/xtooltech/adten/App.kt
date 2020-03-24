package com.xtooltech.adten

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import kotlin.properties.Delegates

class App  :Application(){

     var isGuest:Boolean= true

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
    }

}