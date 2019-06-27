package com.cs.architecture.mvp

import android.os.Bundle
import android.widget.Toast
import com.cs.architecture.R
import com.cs.architecture.model.User
import com.cs.architecture.mvp.contract.LoginContract
import com.cs.architecture.mvp.presenter.LoginPresenter
import com.cs.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_mvp.*

/**
 *
 * author : ChenSen
 * data : 2019/6/27
 * desc: 以Activity作为View层
 *
 */
class MvpActivity2 : BaseActivity(), LoginContract.View {

    private val loginPresenter = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvp)

        loginPresenter.attachView(this)

        btLogin.setOnClickListener {
            val user = User(etName.text.toString(), etPwd.text.toString())
            loginPresenter?.login(user)
        }
    }


    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun loginSuccess(msg: String) {
        showToast(msg)
    }

    override fun loginFailed(msg: String) {
        showToast(msg)
    }


    override fun onBind() {
    }

    override fun onUnBind() {
    }


    override fun onDestroy() {
        super.onDestroy()
        loginPresenter.detachView()
    }
}