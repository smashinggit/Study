package com.cs.kotlin.api

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.cs.kotlin.data.Repository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @Desc
 * @Author ChenSen
 * @Date 2020/5/8-22:52
 *
 */
interface Api {

    @GET("users/{user}/repos")
    suspend fun getRepo(@Path("user") user: String): List<Repository>

    @GET("users/{user}/repos")
     fun getRepo2(@Path("user") user: String): Call<ResponseBody>
}