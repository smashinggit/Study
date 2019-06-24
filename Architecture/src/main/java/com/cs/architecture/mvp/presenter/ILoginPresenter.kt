package com.cs.architecture.mvp.presenter

import com.cs.architecture.model.User


/**
 *
 * author : ChenSen
 * data : 2019/6/12
 * desc:
 */
interface ILoginPresenter {
    fun login(user: User)
}