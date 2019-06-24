package com.cs.architecture.jetpack.databinding

import android.content.Context
import android.view.View
import android.widget.Toast

/**
 *
 * author : ChenSen
 * data : 2019/6/24
 * desc:
 * 事件处理
 * 1. 方法引用
 * 在编译期完成，如果对应的方法不存在或者方法名错误，则会报错
 *
 * 2. 监听器绑定
 *
 *
 * 方法引用和监听绑定的不同：
 * 方法引用实际的listener是在数据绑定的时候，而不是事件发生的时候
 * 监听绑定发生在事件发生时，并且允许执行任意的表达式
 *
 * 方法绑定的参数必须和对应listener的参数相同
 * 监听绑定只需要返回值和对应listener相同即可
 *
 */
class EventPresenter(private val context: Context) {

    //改变User信息，此时UI会自动刷新
    fun changeUser(user: User) {
        val name  = user.name.get()?.split("~")?.get(0)
        user.name.set("$name~${System.currentTimeMillis()}")
    }


    //方法绑定
    fun myClick(view: View) { //参数必须是view，因为onClickLstener方法中的参数是view
        Toast.makeText(context, "调用自定义的方法", Toast.LENGTH_SHORT).show()
    }

    //监听绑定
    fun onClickImpl(name: String) {//两种方法，1是省略listener中的参数，2是不省略
        Toast.makeText(context, "onClickListener 点击事件,收到参数为 $name", Toast.LENGTH_SHORT).show()
    }

    //监听绑定2
    fun onClickImpl2(view: View, name: String) {//两种方法，1是省略listener中的参数，2是不省略
        Toast.makeText(context, "onClickListener 点击事件,收到参数为 $name", Toast.LENGTH_SHORT).show()
    }


}