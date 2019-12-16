package com.cs.app.telephony

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.cs.app.R
import com.cs.library_architecture.base.BaseActivity
import com.cs.library_architecture.permission.PermissionHelper
import kotlinx.android.synthetic.main.activity_telephony.*


/**
 *
 * author : ChenSen
 * data : 2019/6/8
 * desc:
 */
class TelephonyActivity : BaseActivity() {
    private val phoneType = arrayOf("未知", "2G", "3G", "4G")
    private val simState = arrayOf("状态未知", "无SIM卡", "被PIN加锁", "被PUK加锁", "被NetWork PIN加锁", "已准备好")
    lateinit var mTelephonyManager: TelephonyManager
    private val mPermissionHelper by lazy {
        PermissionHelper()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telephony)

        mTelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        check()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun check() {
        mPermissionHelper.with(this)
                .onGranted {
                    showState()
                }
                .onDenied {
                    Toast.makeText(this, "没有权限，请前往设置中打开1", Toast.LENGTH_SHORT).show()
                }
                .checkPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    private fun showState() {
        val state = "设备编号：${mTelephonyManager.deviceId} \n " +
                "软件版本：${mTelephonyManager.deviceSoftwareVersion} \n" +
                "运营商代号: ${mTelephonyManager.networkOperator} \n" +
                "运营商名称：${mTelephonyManager.networkOperatorName} \n" +
                "网络类型： ${phoneType[mTelephonyManager.phoneType]} \n" +
                "设备当前位置：${mTelephonyManager.cellLocation} \n" +
                "SIM卡的国别：${mTelephonyManager.simCountryIso} \n" +
                "SIM卡序列号：${mTelephonyManager.simSerialNumber} \n" +
                "SIM卡状态：${simState[mTelephonyManager.simState]}"

        tvState.text = state
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}