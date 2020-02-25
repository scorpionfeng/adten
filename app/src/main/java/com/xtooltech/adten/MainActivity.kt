package com.xtooltech.adten

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xtooltech.adten.util.ProxySp

class MainActivity : AppCompatActivity() {

    var firstEnter by ProxySp("first_enter",false)

    var name by ProxySp("name","phoenix")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}
