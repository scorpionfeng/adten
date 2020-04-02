package com.xtooltech.adten

import android.app.Application
import android.util.Log
import android.view.WindowManager
import com.alibaba.android.arouter.launcher.ARouter
import com.xtooltech.adtenx.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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
        instance = this

        ARouter.init(this)
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openDebug()
        }


        Thread {

            val running = arrayOf("logcat", "-s", "adb logcat *: W")
            val exec = Runtime.getRuntime().exec(running)

            var os: FileOutputStream? = null
            try {
                val input = exec.inputStream
                var file=File("/sdcard/Log.txt")
                takeIf { !file.exists() }?.apply {
                    file.createNewFile()
                }
                //新建一个路径信息
                os = FileOutputStream("/sdcard/Log.txt");
                var uread: Int = -1

                input.use {
                    os.use {
                        val result = { uread = input.read();uread }
                        while (result() != -1) {
                            it?.write(uread)
                        }
                    }

                }
            } catch (e: Exception) {
                Log.d(
                    "writelog",
                    "read logcat process failed. message: "
                            + e.localizedMessage
                );
            } finally {
                if (null != os) {
                    try {
                        os.close();
                        os = null;
                    } catch (e2: IOException) {
                        // Do nothing
                    }
                }
            }
        }.start();
    }
}