package com.cs.app.ui.coordinator

import android.os.Bundle
import android.view.MotionEvent
import com.cs.app.R
import com.cs.library_architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_coordinator1.*
import kotlinx.android.synthetic.main.activity_ui.*

/**
 * @Author : ChenSen
 * @Date : 2019/10/11 9:14
 *
 * @Desc :
 */
class CoordinatorActivity1 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator1)

        btnObservable.setOnTouchListener { v, event ->

            if (event.action == MotionEvent.ACTION_MOVE) {
                v.x = event.rawX - v.width / 2
                v.y = event.rawY - v.height / 2
            }
            true
        }
    }
}