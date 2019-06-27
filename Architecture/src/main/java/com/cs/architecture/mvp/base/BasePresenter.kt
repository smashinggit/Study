package com.cs.architecture.mvp.base

/**
 *
 * author : ChenSen
 * data : 2019/6/12
 * desc:
 */
abstract class BasePresenter<P : IPresenter<P, V>, V : IView<P, V>> : IPresenter<P, V> {

    var mView: V? = null

    override fun attachView(view: V) {
        mView = view
        mView?.onBind()
    }

    override fun detachView() {
        mView?.onUnBind()
        mView = null
    }

}