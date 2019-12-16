package com.cs.app.ui.coordinator

import android.os.Bundle
import com.cs.app.R
import com.cs.library_architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_coordinator2.webView
import kotlinx.android.synthetic.main.activity_coordinator3.*

/**
 * @Author : ChenSen
 * @Date : 2019/10/11 9:14
 *
 * @Desc :
 */
class CoordinatorActivity3 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator3)

        webView.loadUrl("https://wanandroid.com/")

        tabLayout.addTab(tabLayout.newTab().setText("专辑"))
        tabLayout.addTab(tabLayout.newTab().setText("音乐"))
        tabLayout.addTab(tabLayout.newTab().setText("喜欢"))
    }
}