package com.cs.recyclerview.data

import com.alibaba.fastjson.JSONObject
import com.cs.recyclerview.http.HttpUtils
import retrofit2.Call

/**
 * @Author : ChenSen
 * @Date : 2019/9/23 14:31
 *
 * @Desc :
 */
class MeizhiRepository private constructor() {

    companion object {
        private var instance: MeizhiRepository? = null

        fun getInstance(): MeizhiRepository {
            return instance ?: synchronized(this) {
                instance ?: MeizhiRepository().apply {
                    instance = this
                }
            }
        }
    }


    fun getMeizhi(page: Int): Call<JSONObject> {
        return HttpUtils.createService(IApi::class.java).getMeizhi(100, page)
    }

}