package com.cs.camerademo.camera2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.cs.camerademo.R

/**
 * author :  chensen
 * data  :  2018/3/18
 * desc :
 */
class CameraActivity2 : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera2)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)



    }

}


