package com.cs.architecture.mvp.presenter

import com.cs.architecture.model.User
import com.cs.architecture.mvp.base.BasePresenter
import com.cs.architecture.mvp.contract.LoginContract

/**
 *
 * author : ChenSen
 * data : 2019/6/12
 * desc:
 */
class LoginPresenter : BasePresenter<LoginContract.Presenter, LoginContract.View>(), LoginContract.Presenter {


    override fun login(user: User) {

        if (user.name.isEmpty() || user.age.isEmpty()) {
            mView?.showToast("用户名/密码不能为空")
        } else {

            if (user.name == "qq" && user.age == "123") {
                mView?.loginSuccess("登录成功")
            } else {
                mView?.loginFailed("登录失败")
            }
        }
    }

}