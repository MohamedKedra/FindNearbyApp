package com.mohamed.findnearbyapp

import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import android.app.Application
import android.content.Context


class AppNetwork : Application() {

    companion object{
        fun hasNetwork(context: Context): Boolean {
            return isNetworkConnected(context)
        }

        private fun isNetworkConnected(context: Context) : Boolean{
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
    }
}