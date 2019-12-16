package com.cs.app.ui.toolbar

import android.os.Bundle
import android.widget.Toast
import com.cs.app.R
import com.cs.library_architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_zhihu.*

/**
 * @Author : ChenSen
 * @Date : 2019/10/11 17:58
 *
 * @Desc :
 */
class ZhiHuActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zhihu)

        toolBar.inflateMenu(R.menu.menu_zhihu)
        toolBar.setNavigationOnClickListener {
            Toast.makeText(this, "点击", Toast.LENGTH_SHORT).show()
        }
    }
}