package com.cs.architecture.mvvm

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.cs.architecture.R
import com.cs.architecture.databinding.ActivityMvvmBinding
import com.cs.architecture.model.User
import com.cs.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_mvvm.*

/**
 *
 * author : ChenSen
 * data : 2019/7/3
 * desc:
 *
 * 将应用所有的UI数据保存在ViewModel中而不是Activity中
 */
class MvvmActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_mvvm)

        val mvvmBinding = DataBindingUtil.setContentView<ActivityMvvmBinding>(this, R.layout.activity_mvvm)
        val loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        mvvmBinding.loginViewModel = loginViewModel
        mvvmBinding.lifecycleOwner = this


        val sex = if (Math.random() > 0.5) 1 else 0
        val user = User("chensen~${System.currentTimeMillis()}", "${System.currentTimeMillis()}", sex)

    }
}