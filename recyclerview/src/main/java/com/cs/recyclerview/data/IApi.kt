package com.cs.recyclerview.data

import com.alibaba.fastjson.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

/**
 * @Author : ChenSen
 * @Date : 2019/9/23 14:32
 *
 * @Desc :
 */
interface IApi {

    @GET("data/%E7%A6%8F%E5%88%A9/{num}/{page}")
    fun getMeizhi(@Path("num") num: Int, @Path("page") page: Int): Call<JSONObject>
}