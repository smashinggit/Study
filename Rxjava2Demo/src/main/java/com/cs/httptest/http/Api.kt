package com.cs.httptest.http

import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("/api/data/{category}/{count}/{page}")
    fun getGankData(@Path("category") category: String, @Path("count") count: Int, @Path("page") page: Int): Observable<JSONObject>


}