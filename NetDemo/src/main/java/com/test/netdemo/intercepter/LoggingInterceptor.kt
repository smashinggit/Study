package com.test.netdemo.intercepter

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * author : ChenSen
 * data : 2018/8/20
 * desc:
 */
class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain?.request()
        val nanoTime = System.nanoTime()

        Log.d("okhttp", "Sending request on $nanoTime , ${request?.url()},${request?.headers()},${request?.body()}")


        val response = chain?.proceed(request)
        val nanoTime2 = System.nanoTime()
        Log.d("okhttp", "Received  response  for  ${response?.request()?.url()} , ${response?.headers()}, 用时 ${nanoTime2 - nanoTime / 1000}")

        return response!!
    }
}