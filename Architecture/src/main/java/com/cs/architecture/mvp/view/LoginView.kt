package com.cs.architecture.mvp.view

import android.content.Context
import android.view.View
import android.widget.Toast
import com.cs.architecture.model.User
import com.cs.architecture.mvp.base.BaseView
import com.cs.architecture.mvp.contract.LoginContract
import kotlinx.android.synthetic.main.activity_bind.view.etName
import kotlinx.android.synthetic.main.activity_mvp.view.*

/**
 *
 * author : ChenSen
 * data : 2019/6/27
 * desc:
 */
class LoginView(private val context: Context, private val rootView: View) : BaseView<LoginContract.Presenter, LoginContract.View>(), LoginContract.View {


    override fun onBind() {
        super.onBind()

        rootView.btLogin.setOnClickListener {
            val user = User(rootView.etName.text.toString(), rootView.etPwd.text.toString())
            mPresenter?.login(user)
        }
    }

    override fun showToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun loginSuccess(msg: String) {
        showToast(msg)
    }

    override fun loginFailed(msg: String) {
        showToast(msg)
    }


}