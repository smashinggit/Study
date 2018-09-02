package com.test.netdemo.http

import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * author : ChenSen
 * data : 2018/5/21
 * desc:
 */
class NetCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val response = chain.proceed(request)

        //设置响应的缓存时间为60秒，即设置Cache-Control头，并移除pragma消息头，
        // 因为pragma也是控制缓存的一个消息头属性
        response.newBuilder()
                .removeHeader("param")
                .header("Cache-Control", "max-age=10")
                .build()

        return response
    }
}