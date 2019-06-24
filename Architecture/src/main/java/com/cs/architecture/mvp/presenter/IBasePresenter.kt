package com.cs.architecture.mvp.presenter

/**
 *
 * author : ChenSen
 * data : 2019/6/12
 * desc:
 */
interface IBasePresenter<T> {

    fun attachView(view: T)

    fun detachView()
}