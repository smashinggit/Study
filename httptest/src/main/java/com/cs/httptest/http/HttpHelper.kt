package com.cs.httptest.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Created by Lenovo on 2018/1/8.
 */
object HttpHelper {
    var BASE_URL = ""

    val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()
   var builder = Retrofit.Builder


}