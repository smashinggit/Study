package com.cs.app

import android.content.Intent
import android.os.Bundle
import android.os.HandlerThread
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.cs.app.life.MyActivityManager
import com.cs.app.permission.EasyPermissionActivity
import com.cs.app.permission.PermissionActivity
import com.cs.app.telephony.TelephonyActivity
import com.cs.app.thread.HandlerThreadActivity
import com.cs.app.window.FloatWindowManager
import com.cs.app.window.FloatWindowService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("tag", "${resources.displayMetrics.widthPixels}  ${resources.displayMetrics.heightPixels}")

        btnHandlerThread.setOnClickListener { startActivity(Intent(this, HandlerThreadActivity::class.java)) }
        btnWindowManager.setOnClickListener { startService(Intent(this, FloatWindowService::class.java)) }
        btnPermission.setOnClickListener { startActivity(Intent(this, PermissionActivity::class.java)) }
        btnEasyPermission.setOnClickListener { startActivity(Intent(this, EasyPermissionActivity::class.java)) }
        btnTelephony.setOnClickListener { startActivity(Intent(this, TelephonyActivity::class.java)) }
    }

}
