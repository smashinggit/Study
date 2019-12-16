package com.cs.statusbar

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager


/**
 * author :  chensen
 * data  :  2018/3/13
 * desc :标题栏与状态栏颜色一致
 */
class SameColorWithTitleBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status_toolbar_same_color)

        //在style文件中，设置colorPrimaryDark的颜色与所需要的标题栏颜色相同
        // 但是这种在低于21版本的手机上没有效果，可用以下方法

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //5.0以上
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.colorAccent)
        } else {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            val contentView = findViewById<ViewGroup>(android.R.id.content)
//            val statusBarView = View(this)
//            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight())
//            statusBarView.setBackgroundColor(resources.getColor(R.color.colorAccent))
//            contentView.getChildAt(0).fitsSystemWindows = true
//            contentView.addView(statusBarView, 0, lp)
        }
    }
}