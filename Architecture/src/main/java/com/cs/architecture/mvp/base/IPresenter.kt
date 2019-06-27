package com.cs.architecture.mvp.base

/**
 *
 * author : ChenSen
 * data : 2019/6/12
 * desc:
 */
interface IPresenter<P : IPresenter<P, V>, V : IView<P, V>> {

    fun attachView(view: V)

    fun detachView()
}