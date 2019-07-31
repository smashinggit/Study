package com.cs.architecture.jetpack.databinding

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.cs.architecture.BR

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
            notifyPropertyChanged(BR.name)
        }

    @get:Bindable
    var age: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.age)
        }

}