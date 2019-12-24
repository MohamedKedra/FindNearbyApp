package com.mohamed.findnearbyapp.API

import android.content.Context
import com.mohamed.findnearbyapp.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {

    companion object {
        fun getInstance(context: Context) : NearbyService{
            return Retrofit.Builder().baseUrl(Constant.BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).build().create(NearbyService::class.java)
        }
    }
}