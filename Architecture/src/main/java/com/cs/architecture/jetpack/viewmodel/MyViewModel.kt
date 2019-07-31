package com.cs.architecture.jetpack.viewmodel

import androidx.lifecycle.ViewModel

/**
 *
 * author : ChenSen
 * data : 2019/6/26
 * desc:
 *
 * 我们可以通过一个String类型的状态来表示一个TextView，
 * 同理，我们也可以通过一个List<T>类型的状态来维护一个RecyclerView的列表——
 * 在实际开发中我们通过观察这些数据的状态，来维护UI的自动更新，
 * 这就是 数据驱动视图（观察者模式）
 *
 * 我对它的形容类似一个 状态存储器 ， 它存储着UI中各种各样的状态,ViewModel的重心是对 数据状态的维护
 *
 * 职责：
 * 1.规范化了ViewModel的基类；
 * 2.ViewModel不会随着Activity的屏幕旋转而销毁；
 * 3.在对应的作用域内，保正只生产出对应的唯一实例，保证UI组件间的通信
 *
 */
class MyViewModel : ViewModel() {

}