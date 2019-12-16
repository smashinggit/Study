package com.cs.recyclerview.http

import androidx.annotation.NonNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.fastjson.FastJsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @Author : ChenSen
 * @Date : 2019/9/6 15:45
 *
 * @Desc :
 */
object HttpUtils {

    private const val CONNECTED_TIMEOUT = 15L
    private const val READ_TIMEOUT = 15L

    private var BASE_URL = " http://gank.io/api/"


    private val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(CONNECTED_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .build()


    private var builder = Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(FastJsonConverterFactory.create())


    fun setBaseUrl(url: String) {
        BASE_URL = url
        builder.baseUrl(BASE_URL)
    }


    fun <T> createService(@NonNull clazz: Class<T>): T {
        return builder.build().create(clazz)
    }

}