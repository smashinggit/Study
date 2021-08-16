package com.cs.app.permission

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs.app.R
import com.cs.app.log
import kotlinx.android.synthetic.main.activity_permission.*


/**
 *
 * author : ChenSen
 * data : 2019/6/6
 * desc:
 */
class PermissionActivity : Activity() {

    companion object {
        private const val NUM = ContactsContract.CommonDataKinds.Phone.NUMBER
        private const val NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        private val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        const val PERMISSION_REQUEST_CODE = 100
        const val PERMISSION_SETTING_CODE = 200
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)

        //先检查权限
        hasPermission()
    }

    private fun initContactList() {
        val contacts = getContact()
        rvContact.adapter = MyAdapter(this, contacts)
        rvContact.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }


    private fun hasPermission() {
        //第一步. 检查权限
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)

        if (result == PackageManager.PERMISSION_GRANTED) {  //有权限
            log("PackageManager.PERMISSION_GRANTED")

            initContactList()

        } else {  //没有权限

            log("PackageManager.PERMISSION_DENIED")


            /**
             * shouldShowRequestPermissionRationale
             * 如果应用之前请求过该权限但用户拒绝了该方法就会返回true
             *
             * 如果用户之前拒绝了权限请求并且勾选了权限请求对话框的”不再询问”，该方法会返回false，
             * 如果设备策略禁止该应用获得该权限也会返回false
             *
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                log("shouldShowRequestPermissionRationale")

                // 向用户显示一个解释，要以异步非阻塞的方式
                // 该线程将等待用户响应！等用户看完解释后再继续尝试请求权限
                showPermissionExplainDialog()

            } else {
                log("请求权限")

                /**
                 * 当你的应用调用requestPermissions()方法时，系统会向用户展示一个标准对话框，
                 * 你的应用不能修改也不能自定义这个对话框，如果你需要给用户一些额外的信息和解释你就需要在
                 * 调用requestPermissions()之前像上面一样 "解释为什么应用需要这些权限"
                 */
                requestPermission()
            }
        }
    }

    // 不需要向用户解释了，我们可以直接请求该权限
    //第二步. 请求权限
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_CODE)
    }


    //第三步，当用户拒绝后，展示一个对话框，解释为什么需要此权限
    private fun showPermissionExplainDialog() {
        AlertDialog.Builder(this)
                .setTitle("权限申请")
                .setMessage("您刚才拒绝了读取联系人的权限，但是现在我需要这个权限，" +
                        "点击确定申请权限，点击取消将无法使用该功能")
                .setPositiveButton("确定") { dialog, _ ->
                    requestPermission()
                    dialog.cancel()
                }
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
    }

    //第四步，当用户拒绝并且勾选了不在提示，那么只能引导用户去设置页面打开权限
    private fun showPermissionSettingDialog() {
        AlertDialog.Builder(this)
                .setTitle("权限设置")
                .setMessage("您刚才拒绝了读取联系人的权限，请到应用设置页面更改应用的权限")
                .setPositiveButton("确定") { dialog, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, PERMISSION_SETTING_CODE)
                    dialog.cancel()
                }
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.cancel()
                }
                .show()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if (grantResults != null && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initContactList()
                    log("权限被赋予")

                } else {

                    // 权限请求被拒绝了,不能继续依赖该权限的相关操作了
                    Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show()
                    log("权限被拒绝")
                    showPermissionSettingDialog()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PERMISSION_SETTING_CODE -> { //第五步，当从设置权限页面返回后，重新请求权限
                log("从设置权限页面返回后，重新请求权限")
                requestPermission()
            }
        }

    }

    private fun getContact(): List<Contact> {
        val contacts = ArrayList<Contact>()

        val cursor = contentResolver.query(phoneUri, arrayOf(NUM, NAME), null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(NAME))
                val num = cursor.getString(cursor.getColumnIndex(NUM))
                contacts.add(Contact(name, num))
            }
        }
        return contacts
    }
}