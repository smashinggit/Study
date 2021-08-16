package com.cs.library_architecture.permission

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * @Author : ChenSen
 * @Date : 2019/12/10 15:57
 *
 * @Desc : 动态权限相关的帮助类
 */
class PermissionHelper {

    companion object {
        const val PERMISSION_REQUEST_CODE_ALTER_WINDOW = 0x3003
        const val PERMISSION_REQUEST_CODE = 0x1001
        const val PERMISSION_SETTING_CODE = 0X2002
    }

    private val mGrantedPermissions = arrayListOf<String>()   // 已经获得的权限
    private val mDeniedPermissions = arrayListOf<String>()    // 没有获得的权限
    private var onGranted: (ArrayList<String>) -> Unit = {}  // 获得权限后的回调
    private var onDenied: (ArrayList<String>) -> Unit = {}   // 权限被拒绝的回调
    private lateinit var mContext: Activity


    fun with(context: Activity): PermissionHelper {
        this.mContext = context
        return this
    }

    /**
     * 检查权限 [permissions]，如果有权限未获得，则申请权限
     */
    fun checkPermissions(permissions: Array<String>): PermissionHelper {
        mGrantedPermissions.clear()
        mDeniedPermissions.clear()

        //逐个判断你要的权限是否已经获得
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(mContext, it) != PackageManager.PERMISSION_GRANTED) {
                mDeniedPermissions.add(it)
            } else {
                mGrantedPermissions.add(it)
            }
        }


        if (mGrantedPermissions.isNotEmpty()) {
            onGranted(mGrantedPermissions)
        }

        if (mDeniedPermissions.isNotEmpty()) {
            requestPermissions(permissions)
        }

        return this
    }

    /**
     * 申请权限 [permissions]
     */
    private fun requestPermissions(permissions: Array<String>) {
        ActivityCompat.requestPermissions(mContext, permissions, PERMISSION_REQUEST_CODE)
    }

    /**
     * 申请权限的结果
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE) {

            val grantedPermissions = ArrayList<String>()  //申请到的权限
            val deniedPermissions = ArrayList<String>()   //未申请到的权限

            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    deniedPermissions.add(permissions[i])
                } else {
                    grantedPermissions.add(permissions[i])
                }
            }

            if (grantedPermissions.isNotEmpty()) {
                onGranted(grantedPermissions)
            }

            if (deniedPermissions.isNotEmpty()) { //当有权限被拒绝的时候
                onDenied(deniedPermissions)
            }
        }
    }


    fun onGranted(onGranted: (ArrayList<String>) -> Unit): PermissionHelper {
        this.onGranted = onGranted
        return this
    }

    fun onDenied(onDenied: (ArrayList<String>) -> Unit): PermissionHelper {
        this.onDenied = onDenied
        return this
    }


    /**
     * 当用户拒绝并且勾选了不在提示，再次申请权限的时候就不会再弹出权限申请的对话框，
     * 而是直接在 Activity#onRequestPermissionsResult 返回 PackageManager.PERMISSION_DENIED
     *
     * 这种情况下，弹出一个标题为 [title]，内容为 [message] 的对话框，引导用户去设置页面打开权限
     *
     */
    fun showPermissionSettingDialog(title: String = "开启权限",
                                    message: String = "由于您拒绝了相关的权限，请您到应用设置页面打开相关的权限，否则程序的部分功能无法正常使用",
                                    onCancel: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定") { dialog, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    val uri = Uri.fromParts("package", mContext.packageName, null)
                    intent.data = uri
                    mContext.startActivityForResult(intent, PERMISSION_SETTING_CODE)
                    dialog.cancel()
                }
                .setNegativeButton("取消", onCancel)
                .show()
    }

    /**
     *请求悬浮窗权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun checkoutSystemAlterWindow(context: Activity, onGranted: () -> Unit) {
        if (Settings.canDrawOverlays(context)) {
            onGranted()
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            intent.data = Uri.parse("package:" + context.packageName)
            context.startActivityForResult(intent, PERMISSION_REQUEST_CODE_ALTER_WINDOW)
        }
    }
}