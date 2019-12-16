package com.cs.app.ui.coordinator

import android.os.Bundle
import com.cs.app.R
import com.cs.library_architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_coordinator4.*

/**
 * @Author : ChenSen
 * @Date : 2019/10/11 9:14
 *
 * @Desc :
 *
 * app:contentScrim：指定CollapsingToolbarLayout折叠后的Toolbar颜色
 * app:collapsedTitleTextAppearance：折叠状态标题文字的样式
 * app:expandedTitleTextAppearance：展开状态标题文字的样式
 */
class CoordinatorActivity4 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator4)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        webView.loadUrl("https://wanandroid.com/")

        tabLayout.addTab(tabLayout.newTab().setText("专辑"))
        tabLayout.addTab(tabLayout.newTab().setText("音乐"))
        tabLayout.addTab(tabLayout.newTab().setText("喜欢"))
    }
}