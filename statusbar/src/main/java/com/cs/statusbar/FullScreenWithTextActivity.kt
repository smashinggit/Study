package com.cs.statusbar

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager

/**
 * author :  chensen
 * data  :  2018/3/13
 * desc :
 */
class FullScreenWithTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        //默认API 最低19
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            val contentView = window.decorView.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
            contentView.getChildAt(0).fitsSystemWindows = false
        }
    }
}