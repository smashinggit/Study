package com.cs.app.ui.coordinator

import android.os.Bundle
import android.view.MotionEvent
import com.cs.app.R
import com.cs.library_architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_coordinator1.*
import kotlinx.android.synthetic.main.activity_coordinator2.*
import kotlinx.android.synthetic.main.activity_ui.*

/**
 * @Author : ChenSen
 * @Date : 2019/10/11 9:14
 *
 * @Desc :
 */
class CoordinatorActivity2 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator2)

        webView.loadUrl("https://wanandroid.com/")
    }
}