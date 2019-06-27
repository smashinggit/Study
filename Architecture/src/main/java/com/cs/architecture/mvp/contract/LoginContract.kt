package com.cs.architecture.mvp.contract

import com.cs.architecture.model.User
import com.cs.architecture.mvp.base.IPresenter
import com.cs.architecture.mvp.base.IView

/**
 *
 * author : ChenSen
 * data : 2019/6/27
 * desc:
 */
interface LoginContract {

    interface View : IView<Presenter, View> {

        fun loginSuccess(msg: String)

        fun loginFailed(msg: String)
    }

    interface Presenter : IPresenter<Presenter, View> {
        fun login(user: User)
    }
}