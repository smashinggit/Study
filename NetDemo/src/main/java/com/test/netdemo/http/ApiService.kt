package com.test.netdemo.http

import com.google.gson.JsonObject
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 *
 * author : ChenSen
 * data : 2018/5/21
 * desc:
 */
interface ApiService {


    @GET("users/{id}/repos")
    fun listRepos(@Path("id") id: String): Call<List<User>>


    @GET("group/{id}/users")
    fun groupList(@Path("id") id: String, @Query("sort") sort: String,
                  @QueryMap options: Map<String, String>): Call<List<String>>


    @POST("users/new")
    fun creatUser(@Body user: User): Call<User>


    @FormUrlEncoded
    @POST("user/edit")
    fun updateUser(@Field("name") name: String, @FieldMap map: Map<String, String>): Call<User>


    @Multipart
    @POST("user/photo")
    fun updateUser(@Part("photo") photo: RequestBody, @Part("param") param: RequestBody): Call<User>


}