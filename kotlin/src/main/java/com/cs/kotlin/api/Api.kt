package com.cs.kotlin.api

import com.cs.kotlin.data.Repository
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
}