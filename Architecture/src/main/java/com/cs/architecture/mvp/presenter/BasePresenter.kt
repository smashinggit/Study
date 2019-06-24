package com.cs.architecture.mvp.presenter

/**
 *
 * author : ChenSen
 * data : 2019/6/12
 * desc:
 */
abstract class BasePresenter<T> : IBasePresenter<T> {

    var mView: T? = null

    override fun attachView(view: T) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

}