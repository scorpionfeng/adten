package com.xtooltech.adten

import android.app.Application
import android.view.WindowManager
import com.alibaba.android.arouter.launcher.ARouter
import com.xtooltech.adtenx.BuildConfig
import kotlin.properties.Delegates

class App  :Application(){

     var isGuest:Boolean= true

    companion object{
        var instance:App by Delegates.notNull()
    }

     var wmParams=WindowManager.LayoutParams();
    fun  getMywmParams(): WindowManager.LayoutParams{
        return wmParams;
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