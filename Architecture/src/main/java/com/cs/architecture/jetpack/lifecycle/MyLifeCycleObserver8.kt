package com.cs.architecture.jetpack.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 *
 * author : ChenSen
 * data : 2019/6/26
 * desc:
 * 如果是java8,建议使用这种方式
 */
class MyLifeCycleObserver8 :DefaultLifecycleObserver{


    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
    }

}