package com.cs.architecture.mvp

import android.os.Bundle
import android.view.LayoutInflater
import com.cs.architecture.R
import com.cs.architecture.mvp.presenter.LoginPresenter
import com.cs.architecture.mvp.view.LoginView
import com.cs.common.base.BaseActivity

/**
 *
 * author : ChenSen
 * data : 2019/6/12
 * desc: View层单独分离
 */
class MvpActivity : BaseActivity() {

    private val loginPresenter = LoginPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootView = LayoutInflater.from(this).inflate(R.layout.activity_mvp, null)
        setContentView(rootView)

        val loginView = LoginView(this, rootView)


        loginPresenter.attachView(loginView)
        loginView.bindPresenter(loginPresenter)

    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter.detachView()
    }
}