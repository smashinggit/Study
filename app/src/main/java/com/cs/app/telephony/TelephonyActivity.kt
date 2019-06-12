package com.cs.app.telephony

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.widget.Toast
import com.cs.app.R
import com.cs.common.base.BaseActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telephony)

        mTelephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        check()
    }

    private fun check() {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission_group.PHONE)) {
            showState()
        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.PHONE)) {
                Toast.makeText(this, "没有权限，请前往设置中打开1", Toast.LENGTH_SHORT).show()
            } else {
                requestPermission()
            }
        }
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

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission_group.PHONE), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray) {

        when (requestCode) {
            100 -> {
                if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    showState()
                } else {
                    Toast.makeText(this, "没有权限，请前往设置中打开", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}