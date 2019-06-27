package com.cs.architecture.mvp.base

/**
 *
 * author : ChenSen
 * data : 2019/6/27
 * desc:
 */
abstract class BaseView<P : IPresenter<P, V>, V : IView<P, V>> : IView<P, V> {

    var mPresenter: P? = null


    fun bindPresenter(presenter: P) {
        mPresenter = presenter
    }

    override fun onBind() {
    }

    override fun onUnBind() {
        mPresenter = null
    }

    override fun showToast(msg: String) {
    }

}