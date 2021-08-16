package com.cs.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cs.app.data.database.DataBaseActivity
import com.cs.app.data.file.FileActivity
import com.cs.app.permission.MyPermissionActivity
import com.cs.app.permission.PermissionActivity
import com.cs.app.telephony.TelephonyActivity
import com.cs.app.thread.HandlerThreadActivity
import com.cs.app.ui.UiActivity
import com.cs.app.window.FloatWindowService
import com.cs.library_architecture.extensions.toast
import com.cs.library_architecture.permission.PermissionHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mPermissionHelper by lazy {
        PermissionHelper()
    }

    private var hasPermission = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("tag", "${resources.displayMetrics.widthPixels}  ${resources.displayMetrics.heightPixels}")

        btnHandlerThread.setOnClickListener { startActivity(Intent(this, HandlerThreadActivity::class.java)) }
        btnWindowManager.setOnClickListener {
            if (hasPermission)
                startService(Intent(this, FloatWindowService::class.java))
            else {
                toast("请打开相关权限")
                checkAlertWindow()
            }
        }

        btnPermission.setOnClickListener { startActivity(Intent(this, PermissionActivity::class.java)) }
        btnMyPermission.setOnClickListener { startActivity(Intent(this, MyPermissionActivity::class.java)) }
        btnTelephony.setOnClickListener { startActivity(Intent(this, TelephonyActivity::class.java)) }
        btnFile.setOnClickListener { startActivity(Intent(this, FileActivity::class.java)) }
        btnDataBase.setOnClickListener { startActivity(Intent(this, DataBaseActivity::class.java)) }
        btnUI.setOnClickListener { startActivity(Intent(this, UiActivity::class.java)) }

        checkAlertWindow()
    }

    private fun checkAlertWindow() {
        mPermissionHelper.with(this)
                .checkoutSystemAlterWindow(this) {
                    hasPermission = true
                }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
        log("onRequestPermissionsResult  ${permissions[0]}    ${grantResults[0]}")
    }
}
