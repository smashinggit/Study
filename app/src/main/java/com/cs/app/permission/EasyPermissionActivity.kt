package com.cs.app.permission

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import com.cs.app.R
import com.cs.library_architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_permission.*
import pub.devrel.easypermissions.EasyPermissions

/**
 *
 * author : ChenSen
 * data : 2019/6/12
 * desc: EasyPermission 的使用
 */
class EasyPermissionActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {


    companion object {
        private const val NUM = ContactsContract.CommonDataKinds.Phone.NUMBER
        private const val NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        private val phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        const val PERMISSION_REQUEST_CAMERA_AND_LOCATION_AND_CONTACT = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        checkPermission()
    }

    private fun checkPermission() {

        //2. 检测是否有权限
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA)
        ) {
            // Already have permission, do the thing
            initContactList()

        } else {
            //3. 没有权限
            EasyPermissions.requestPermissions(this, "为确保程序正常运行，请打开相关权限",
                    PERMISSION_REQUEST_CAMERA_AND_LOCATION_AND_CONTACT,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA)
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        // Some permissions have been denied
        log("onPermissionsDenied $requestCode $perms")

        //4.如果用户勾选了不在提示，那么app将无法在申请到权限，只能引导用户去设置里打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            showPermissionSettingDialog()
        } else {
            Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        // Some permissions have been granted
        log("onPermissionsGranted $requestCode $perms")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //1. 初始设置
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    private fun showPermissionSettingDialog() {
        AlertDialog.Builder(this)
                .setTitle("权限设置")
                .setMessage("您刚才拒绝了读取联系人的权限，请到应用设置页面更改应用的权限")
                .setPositiveButton("确定") { dialog, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, PermissionActivity.PERMISSION_SETTING_CODE)
                    dialog.cancel()
                }
                .setNegativeButton("取消") { dialog, _ ->
                    dialog.cancel()
                    Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //5.从设置页面返回后。重新请权限
        when (requestCode) {
            PermissionActivity.PERMISSION_SETTING_CODE -> { //第五步，当从设置权限页面返回后，重新请求权限
                log("从设置权限页面返回后，重新请求权限")
                checkPermission()
            }
        }
    }


    //展示联系人
    private fun initContactList() {
        val contacts = getContact()
        rvContact.adapter = MyAdapter(this, contacts)
        rvContact.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun getContact(): List<Contact> {
        val contacts = ArrayList<Contact>()

        val cursor = contentResolver.query(phoneUri, arrayOf(NUM, NAME), null, null, null)
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndex(NAME))
            val num = cursor.getString(cursor.getColumnIndex(NUM))
            contacts.add(Contact(name, num))
        }
        return contacts
    }

}