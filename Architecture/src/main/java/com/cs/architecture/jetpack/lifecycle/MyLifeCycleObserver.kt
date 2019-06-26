package com.cs.architecture.jetpack.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import android.content.Context
import com.cs.common.utils.LogUtils

/**
 *
 * author : ChenSen
 * data : 2019/6/21
 * desc:  用于监听带有生命周期组件的声明周期
 * 用途：将声明周期组件的中相关操作抽离到此类中，减少Activity等代码量
 */
class MyLifeCycleObserver(val context: Context,
                          private val lifecycle: Lifecycle,
                          val callBack: (value: String) -> Unit) : LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startDoSth() {
        LogUtils.logi("Lifecycle.Event.ON_RESUME")
        callBack("start")

    }


    fun doWork() {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            //do sth
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopDoSth() {
        LogUtils.logi("Lifecycle.Event.ON_PAUSE")
        callBack("stop")
    }

}