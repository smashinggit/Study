package com.cs.statusbar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager

/**
 * author :  chensen
 * data  :  2018/3/13
 * desc :
 */
class FullScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

//        //方式一
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //方式二
        //   window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //方式三 style.xml中配置
        //<style name="fullScreen" parent="Theme.AppCompat.DayNight.NoActionBar">
        //        <item name="android:windowFullscreen">true</item>
        //</style>
    }

}