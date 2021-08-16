package com.cs.app.data.file

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.annotation.RequiresApi
import com.cs.app.R
import com.cs.library_architecture.base.BaseActivity
import com.cs.library_architecture.extensions.toast
import kotlinx.android.synthetic.main.activity_file.*
import java.io.File

/**
 *
 * author : ChenSen
 * data : 2019/7/16
 * desc:
 *
 * Android系统分为内部存储和外部存储，
 * 内部存储是手机系统自带的存储，一般空间都比较小，
 * 外部存储一般是SD卡的存储，空间一般都比较大，但不一定可用或者剩余空间可能不足。
 *
 * 一般我们存储内容都会放在外部存储空间里。使用过程注意事项：
 * 1. 先判断SD卡是否可用，可用时优先使用SD卡的存储，不可用时用内部存储
 * 2. 存储在SD卡上时，可以在SD卡上新建任意一个目录存放，也可以存放在应用程序内部文件夹，
 *    区别是在SD卡的任意目录存放时内容不会随应用程序的卸载而消失，而在应用程序内部的内容会随应用程序卸载消失。
 * 3. 一般缓存文件放在应用程序内部，用户主动保存的文件放在SD卡上的文件夹里。
 *

 *
 * # 内部存储：
 * 部存储位于系统中很特殊的一个位置，对于设备中每一个安装的 App，
 * 系统都会在 data/data/packagename/xxx 自动创建与之对应的文件夹
 * 当一个应用卸载之后，内部存储中的这些文件也被删除。
 * 对于这个内部目录，用户是无法访问的，除非获取root权限
 *
 * context.getFilesDir()  //对应内部存储的路径为: data/data/packagename/files，
 * 但是对于有的手机如：华为，小米等获取到的路径为：data/user/0/packagename/files
 *
 * context.getCacheDir()  //此目录应该限制大小，并定期清除
 *
 * openFileOutput()
 *
 *
 * # 外部存储：
 * 手机机身自带的存储也是外部存储，如果再插入SD卡的话也叫外部存储，
 * 因此对于外部存储分为两部分：SD卡和扩展卡内存
 *
 *  getExternalFilesDirs()
 *
 *
 *
 */
class FileActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        createInternalFile()  //内部文件

        createExternalFile()   //外部文件

    }


    @SuppressLint("SetTextI18n")
    private fun createInternalFile() {
        val dir = "1. 内部文件目录 context.getFilesDir() -> ${filesDir.absolutePath}\n" +
                "2. 内部缓存目录 context.getCacheDir() -> ${cacheDir.absolutePath}"
        tvInternal.text = dir
        loge("context.getFilesDir() -> ${filesDir.absolutePath}")
        loge("context.getCacheDir() -> ${cacheDir.absolutePath}")

        btCreateInternal.setOnClickListener {
            val file = File(filesDir, "privateFile.txt")
            if (!file.exists()) {
                file.createNewFile()
                toast("创建内部文件成功！${file.absolutePath}")
                loge("创建内部文件成功！${file.absolutePath}")
            } else {
                toast("内部文件${file.absolutePath}已经存在")
            }
        }

        val fileOutput = openFileOutput("myFile", Context.MODE_PRIVATE)
                .use { it.write("Hello World".toByteArray()) }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    private fun createExternalFile() {

        // 获取手机的外部存储目录,包括下面的 扩展外部存储 和 SD卡存储
        // 如果没有插SD卡的话，得到的就是手机自身的扩展卡内存文件
        val availableDirs = StringBuilder("可用的外部存储目录: \n")
        val dirs = getExternalFilesDirs(Environment.MEDIA_MOUNTED)
        dirs.forEach {
            availableDirs.append("${it.absolutePath} \n")
            loge("可用的外部存储目录: -> ${it.absolutePath}")
        }
        tvAvailable.text = availableDirs


        // 1. 私有目录: 此目录路径需要通过context来获取，同时在app卸载之后，
        // 这些文件也会被删除。类似于内部存储
        val applicationDirs = "私有存储目录(应用卸载时随之删除):\n" +
                "1. 根目录 (Environment.getExternalStorageDirectory())  -> ${Environment.getExternalStorageDirectory().absolutePath} \n " +
                "2. 私有程序目录 -> \$根目录/Android/data/包名 \n" +
                "3. 私有文件目录(不带参数) (getExternalFilesDirs) -> ${getExternalFilesDirs(null)[0].absolutePath} \n" +
                "4. 私有文件目录(如：参数Picture) (getExternalFilesDirs(Environment.DIRECTORY_PICTURES)) -> ${getExternalFilesDirs(Environment.DIRECTORY_PICTURES)[0].absolutePath} \n" +
                "5. 私有缓存目录 (externalCacheDir) -> ${externalCacheDir?.absolutePath} \n"

        tvApplication.text = applicationDirs
        loge(applicationDirs)


        //2. 公共目录 我们可以在外部存储上新建任意文件夹，不过在6.0及之后的系统需要动态申请权限，
        // 这些目录的内容不会随着应用的卸载而消失
        val publicDirs = "公共存储目录 : \n" +
                "1. 根目录 (Environment.getExternalStorageDirectory())  -> ${Environment.getExternalStorageDirectory().absolutePath} \n " +
                "2. Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) ${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath} \n"

        tvPublic.text = publicDirs
        loge(publicDirs)


        //这两个方法返回的是私有空间，媒体不可见
        getExternalFilesDir(null)
                .also {
                    val externalPrivateFile = File(it, "externalPrivateFile")
                    if (!externalPrivateFile.exists())
                        externalPrivateFile.createNewFile()

                    externalPrivateFile.writeText("在外部存储中的私有数据，媒体看不到哦")
                }
        getExternalFilesDirs(Environment.DIRECTORY_DOCUMENTS)

    }

}