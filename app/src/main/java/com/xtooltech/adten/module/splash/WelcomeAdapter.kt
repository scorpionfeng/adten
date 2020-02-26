package com.xtooltech.adten.module.splash

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class WelcomeAdapter(var views:List<View>)  :BaseAdapter(){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return views.get(position)
    }

    override fun getItem(position: Int): Any =views.get(position)

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int =views.size

}