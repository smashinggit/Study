package com.cs.architecture.jetpack.livedata

import androidx.lifecycle.LiveData

/**
 *
 * author : ChenSen
 * data : 2019/6/26
 * desc:
 * 自定义LiveData
 */
class CustomLiveData : LiveData<String>() {


    override fun onActive() {
        super.onActive()
        //有活跃观察者调用该方法
        //开启服务...
    }

    override fun onInactive() {
        super.onInactive()
        //没有任何活跃观察者调用该方法
        //结束服务...
    }
}