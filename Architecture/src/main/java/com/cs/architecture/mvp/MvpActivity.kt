package com.cs.architecture.mvp

import android.os.Bundle
import android.widget.Toast
import com.cs.architecture.R
import com.cs.architecture.model.User
import com.cs.architecture.mvp.presenter.LoginPresenter
import com.cs.architecture.mvp.view.ILoginView
import com.cs.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_mvp.*

/**
 *
 * author : ChenSen
 * data : 2019/6/12
 * desc:
 */
class MvpActivity : BaseActivity(), ILoginView {
    override fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun loginSuccess(msg: String) {
        showToast(msg)
    }

    override fun loginFailed(msg: String) {
        showToast(msg)
    }


    lateinit var mPresenter: LoginPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvp)

        mPresenter = LoginPresenter()
        mPresenter.attachView(this)


        btLogin.setOnClickListener {
            val user = User(etName.text.toString(), etPwd.text.toString())
            mPresenter.login(user)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}