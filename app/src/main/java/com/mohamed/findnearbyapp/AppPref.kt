package com.mohamed.findnearbyapp

import android.content.Context
import android.content.Context.MODE_PRIVATE

class AppPref(val context : Context) {

    private var preferences = context.getSharedPreferences("appPref", MODE_PRIVATE)

    fun setMode(mode : String) {
        val editor = preferences.edit()
        editor.putString(Constant.modeKey,mode).commit()
    }

    fun getMode() : String{
        return preferences.getString(Constant.modeKey,Constant.RealTime)!!
    }

}