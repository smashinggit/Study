package com.test.netdemo.http

import android.os.Environment
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

/**
 *
 * author : ChenSen
 * data : 2018/5/21
 * desc:
 */
object HttpHelper {

    private val cacheFile = File("${Environment.getExternalStorageDirectory()}/NewDemoCache", "cache")
    private val cacheSize = 10 * 1024 * 1024

    val client = OkHttpClient.Builder()
            .cache(Cache(cacheFile, cacheSize.toLong()))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(NetCacheInterceptor())
            .build()

    val retrofit = Retrofit.Builder()
            .baseUrl("http://www.wanandroid.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

}