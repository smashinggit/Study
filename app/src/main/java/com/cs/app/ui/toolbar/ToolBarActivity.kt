package com.cs.app.ui.toolbar

import android.os.Bundle
import android.util.Log
import com.cs.app.R
import com.cs.library_architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_toolbar.*

/**
 * @Author : ChenSen
 * @Date : 2019/10/11 17:55
 *
 * @Desc :
 */
class ToolBarActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar)


        toolBar.inflateMenu(R.menu.menu_toolbar)
        toolBar.setOnMenuItemClickListener {
            Log.e("tag", "点击 menu -> ${it.itemId}")
            return@setOnMenuItemClickListener true
        }

    }


}