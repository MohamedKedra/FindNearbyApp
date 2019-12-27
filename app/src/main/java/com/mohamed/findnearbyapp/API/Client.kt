package com.mohamed.findnearbyapp.API

import android.content.Context
import com.mohamed.findnearbyapp.AppNetwork
import com.mohamed.findnearbyapp.Constant
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Client {

    companion object {

        private val HEADER_CACHE_CONTROL = "Cache-Control"
        private val HEADER_PRAGMA = "Pragma"

        fun getInstance(context: Context): NearbyService {
            return Retrofit.Builder().baseUrl(Constant.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHTTPInstance(context))
                .build().create(NearbyService::class.java)

        }

        private fun getHTTPInstance(context: Context): OkHttpClient {

            val cache = Cache(File(context.cacheDir, "offlineCache"), 10 * 1024 * 1024)
            return OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor())
                .addNetworkInterceptor(networkInterceptor())
                .addInterceptor(offlineInterceptor(context))
                .build()
        }

        private fun offlineInterceptor(context: Context): Interceptor {
            return Interceptor { chain ->
                var request = chain.request()

                if (!AppNetwork.hasNetwork(context)) {
                    val cacheControl = CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()

                    request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build()
                }

                chain.proceed(request)
            }
        }

        private fun networkInterceptor(): Interceptor {
            return Interceptor { chain ->
                val response = chain.proceed(chain.request())

                val cacheControl = CacheControl.Builder()
                    .maxAge(5, TimeUnit.SECONDS)
                    .build()

                response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build()
            }
        }

        private fun httpLoggingInterceptor(): HttpLoggingInterceptor {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return httpLoggingInterceptor
        }
    }
}