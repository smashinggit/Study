package com.cs.architecture.mvp.base

/**
 *
 * author : ChenSen
 * data : 2019/6/12
 * desc:
 */
interface IView<P : IPresenter<P, V>, V : IView<P, V>> {

    fun showToast(msg: String)

    fun onBind()

    fun onUnBind()
}