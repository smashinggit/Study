package com.cs.architecture.mvp.view

/**
 *
 * author : ChenSen
 * data : 2019/6/12
 * desc:
 */
interface ILoginView : IBaseView {

    fun loginSuccess(msg: String)

    fun loginFailed(msg: String)

}