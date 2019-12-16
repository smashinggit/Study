package com.cs.app.ui

import android.content.Intent
import android.os.Bundle
import com.cs.app.R
import com.cs.app.ui.coordinator.CoordinatorActivity1
import com.cs.app.ui.coordinator.CoordinatorActivity2
import com.cs.app.ui.coordinator.CoordinatorActivity3
import com.cs.app.ui.coordinator.CoordinatorActivity4
import com.cs.app.ui.toolbar.ToolBarActivity
import com.cs.app.ui.toolbar.ZhiHuActivity
import com.cs.library_architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_ui.*

/**
 * @Author : ChenSen
 * @Date : 2019/10/11 9:08
 *
 * @Desc :
 */
class UiActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui)

        btnCoordinatorLayout.setOnClickListener {
            startActivity(Intent(this, CoordinatorActivity1::class.java))
        }

        btnCoordinatorLayout2.setOnClickListener {
            startActivity(Intent(this, CoordinatorActivity2::class.java))
        }

        btnCoordinatorLayout3.setOnClickListener {
            startActivity(Intent(this, CoordinatorActivity3::class.java))
        }

        btnCoordinatorLayout4.setOnClickListener {
            startActivity(Intent(this, CoordinatorActivity4::class.java))
        }

        btnToolbar.setOnClickListener {
            startActivity(Intent(this, ToolBarActivity::class.java))
        }

        btnZhihu.setOnClickListener {
            startActivity(Intent(this, ZhiHuActivity::class.java))
        }
    }



}