package com.cmos.agera.http;

import com.alibaba.fastjson.JSONObject;
import com.google.android.agera.Result;
import com.google.android.agera.Supplier;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * author : ChenSen
 * data : 2019/5/24
 * desc:
 */
public interface Api {

    @GET()
    Supplier<Result<ResponseBody>> getAgeraPic(@Url String url); //获取网络图片

    @GET("project/tree/json")
    Supplier<Result<JSONObject>> getProjectTree(); //获取项目列表

    @GET("project/list/1/json")
    Supplier<Result<JSONObject>> getProjectList(@Query("cid") int cid); //获取列表详情

}
