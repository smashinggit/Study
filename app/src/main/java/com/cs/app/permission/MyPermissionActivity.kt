package com.cs.app.permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import com.cs.app.R
import com.cs.library_architecture.base.BaseActivity
import com.cs.library_architecture.permission.PermissionHelper
import kotlinx.android.synthetic.main.activity_mypermission.*

/**
 * @Author : ChenSen
 * @Date : 2019/12/10 17:24
 *
 * @Desc :
 */
class MyPermissionActivity : BaseActivity() {

    companion object {
        const val CAMERA = Manifest.permission.CAMERA
        const val AUDIO = Manifest.permission.RECORD_AUDIO
        const val CONTACTS = Manifest.permission.READ_CONTACTS
        const val PHONE = Manifest.permission.READ_PHONE_STATE
        const val LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    }

    private val mPermissionHelper by lazy {
        PermissionHelper()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypermission)

        tvPermission.text = "需要申请的权限: \n" +
                "1、 相机 \n" +
                "2、 录音 \n" +
                "3、 读取手机联系人 \n" +
                "4、 访问手机状态 \n" +
                "5、 定位"

        btnRequest.setOnClickListener {
            checkPermissions(arrayOf(CAMERA, AUDIO, CONTACTS, PHONE, LOCATION))
        }
    }


    private fun checkPermissions(permissions: Array<String>) {
        tvGranted.text = ""
        tvDenied.text = ""
        mPermissionHelper
                .with(this)
                .onGranted {
                    handleGranted(it)
                }
                .onDenied {
                    handleDenied(it)
                    if (it.isNotEmpty()) {
                        showPermissionSettingDialog(it)
                    }
                }
                .checkPermissions(permissions)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PermissionHelper.PERMISSION_SETTING_CODE -> { //当从设置权限页面返回后，重新请求权限
                checkPermissions(arrayOf(CAMERA, AUDIO, CONTACTS, PHONE, LOCATION))
            }
        }
    }

    private fun showPermissionSettingDialog(deniedPermissions: ArrayList<String>) {
        val result = StringBuffer().append("您刚才拒绝了")
        deniedPermissions.forEach {
            when (it) {
                CAMERA -> result.append("相机、")
                AUDIO -> result.append("录音、")
                CONTACTS -> result.append("读取手机联系人、")
                PHONE -> result.append("访问手机状态、")
                LOCATION -> result.append("定位、")
            }
        }
        val message = result.substring(0, result.length - 1) + "的权限，" +
                "请您到应用设置页面打开相关的权限，否则程序的部分功能无法正常使用"

        mPermissionHelper.showPermissionSettingDialog(message = message,
                onCancel = DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
    }

    private fun handleGranted(denied: ArrayList<String>) {
        val result = StringBuffer().append("以下权限已获得: \n")
        denied.forEach {
            when (it) {
                CAMERA -> result.append("相机 \n")
                AUDIO -> result.append("录音 \n")
                CONTACTS -> result.append("读取手机联系人 \n")
                PHONE -> result.append("访问手机状态 \n")
                LOCATION -> result.append("定位 \n")
            }
        }
        tvGranted.text = result
    }

    private fun handleDenied(denied: ArrayList<String>) {
        val result = StringBuffer().append("以下权限被拒绝: \n")
        denied.forEach {
            when (it) {
                CAMERA -> result.append("相机 \n")
                AUDIO -> result.append("录音 \n")
                CONTACTS -> result.append("读取手机联系人 \n")
                PHONE -> result.append("访问手机状态 \n")
                LOCATION -> result.append("定位 \n")
            }
        }
        tvDenied.text = result
    }

}