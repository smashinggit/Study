package com.cs.architecture.jetpack.databinding

import android.databinding.BaseObservable
import android.databinding.Bindable

/**
 *
 * author : ChenSen
 * data : 2019/6/24
 * desc:
 *
 * 继承BaseObservable，
 * 在 getter方法上加 Bindable注解
 * 在 setter 方法中调用 notifyPropertyChanged()
 */
class User2 : BaseObservable() {

    @get:Bindable
    var name: String = ""
        set(value) {
            field = value
        }

    @get:Bindable
    var age: String = ""
        set(value) {
            field = value
        }

}