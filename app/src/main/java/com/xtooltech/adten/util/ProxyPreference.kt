package com.xtooltech.adten.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.xtooltech.adten.App
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ProxyPreference<T>(
    private val key: String,
    private val defaultValue: T,
    private val useCommit: Boolean = false
) : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharePreferences(key, defaultValue)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putSharePreferences(key, value)
    }

    //延迟属性，只会加载一次
    private val prefs: SharedPreferences by lazy {
        App.instance.getSharedPreferences(
            PKG_NAME,
            Context.MODE_PRIVATE
        )
    }

    @SuppressLint("ApplySharedPref")
    private fun putSharePreferences(name: String, value: T) = with(prefs.edit()) {
        val editor = when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("Type Error, cannot be saved!")
        }
        if (useCommit) editor.commit() else editor.apply()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getSharePreferences(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name,default as String)!!
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("Type Error, cannot be got!")
        }
        return res as T
    }
}