package com.cs.camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.cs.camera.util.DecodeImgTask
import com.cs.camera.util.FileUtil
import kotlinx.android.synthetic.main.activity_capture.*
import java.io.File


/**
 * author :  chensen
 * data  :  2018/3/15
 * desc :
 */
class CaptureActivity : AppCompatActivity() {
    companion object {
        const val AUTHORITY = "com.cs.camera.fileProvider"

        const val REQUEST_CODE_CAPTURE_SMALL = 1
        const val REQUEST_CODE_CAPTURE_RAW = 2
        const val REQUEST_CODE_CAPTURE = 3
        const val REQUEST_CODE_CAPTURE_CROP = 4
        const val REQUEST_CODE_ALBUM = 5
        const val REQUEST_CODE_VIDEO = 6

        var imageFile: File? = null
        var imageCropFile: File? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)

        btnCaptureSmall.setOnClickListener { gotoCaptureSmall() }    //拍照(返回缩略图)
        btnCaptureRaw.setOnClickListener { gotoCaptureRaw() }        //拍照(返回原始图)
        btnCaptureAndClip.setOnClickListener { gotoCaptureCrop() }   //拍照 + 裁切
        btnAlbumAndClip.setOnClickListener { gotoGallery() }         //相册 + 裁切
        btnCaptureVideo.setOnClickListener { gotoCaptureVideo() }    //录视频 + 播放
    }


    //拍照(返回缩略图)
    private fun gotoCaptureSmall() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)?.let {
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_SMALL)
        }
    }

    //拍照(返回原始图)
    private fun gotoCaptureRaw() {
        imageFile = FileUtil.createImageFile()
        imageFile?.let {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val imgUri = FileProvider.getUriForFile(this, AUTHORITY, it)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(it))
            }

            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            intent.resolveActivity(packageManager)?.let {
                startActivityForResult(intent, REQUEST_CODE_CAPTURE_RAW)
            }
        }
    }

    // 拍照 + 裁切
    private fun gotoCaptureCrop() {
        imageFile = FileUtil.createImageFile()

        imageFile?.let {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val imgUri = FileProvider.getUriForFile(this, AUTHORITY, it)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(it))
            }

            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            intent.resolveActivity(packageManager)?.let {
                startActivityForResult(intent, REQUEST_CODE_CAPTURE)
            }
        }
    }

    //裁剪
    private fun gotoCrop(sourceUri: Uri) {
        imageCropFile = FileUtil.createImageFile(true)
        imageCropFile?.let {

            val intent = Intent("com.android.camera.action.CROP")
            intent.putExtra("crop", "true")
            intent.putExtra("aspectX", 1)    //X方向上的比例
            intent.putExtra("aspectY", 1)    //Y方向上的比例
            intent.putExtra("outputX", 500)  //裁剪区的宽
            intent.putExtra("outputY", 500)  //裁剪区的高
            intent.putExtra("scale ", true)  //是否保留比例
            intent.putExtra("return-data", false)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            intent.setDataAndType(sourceUri, "image/*")

            // 7.0 使用 FileProvider 并赋予临时权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            }

            // 11.0无法访问私有域，所以这里要确保裁剪后的文件保存在公有目录中
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // 这里要保证输出的文件是在公有目录
                // 由于此demo中默认保存的就是公有目录，所以这里不做任何操作，如果是自己的项目，请根据具体情况修改

            } else {
            }

            val outputUri = Uri.fromFile(it)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)            //设置输出

            Log.d("tag", "输入 $sourceUri")
            Log.d("tag", "输出 $outputUri")
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CROP)
        }
    }


    //打开系统相册
    private fun gotoGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_ALBUM)
    }


    //录制视频
    private fun gotoCaptureVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (intent.resolveActivity(packageManager) != null)
            startActivityForResult(intent, REQUEST_CODE_VIDEO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                REQUEST_CODE_CAPTURE_SMALL -> {
                    data?.data
                    val bitmap = data?.extras?.get("data") as Bitmap
                    ivResult.setImageBitmap(bitmap)
                }

                REQUEST_CODE_CAPTURE_RAW -> { //拍照成功后，压缩图片，显示结果
                    imageFile?.let {
                        DecodeImgTask(ivResult).execute(it.absolutePath)
                    }
                }

                REQUEST_CODE_CAPTURE -> {       //拍照成功后，裁剪
                    imageFile?.let {
                        val sourceUri = FileProvider.getUriForFile(this, AUTHORITY, it) //通过FileProvider创建一个content类型的Uri
                        gotoCrop(sourceUri)
                    }
                }

                REQUEST_CODE_CAPTURE_CROP -> {   //裁剪成功后，显示结果
                    imageCropFile?.let {
                        ivResult.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
                    }
                }

                REQUEST_CODE_ALBUM -> { //从相册选择照片后，裁剪
                    data?.let {
                        gotoCrop(it.data!!)
                    }
                }

                REQUEST_CODE_VIDEO -> {   //录制视频成功后播放
                    data?.let {
                        val uri = it.data
                        videoView.visibility = View.VISIBLE
                        videoView.setVideoURI(uri)
                        videoView.start()
                        Log.d("tag", "视频uri $uri")
                    }
                }
            }
        } else {
            Log.d("tag", "错误码 $resultCode")
        }
    }

//    Android7.0以上，相机调用时，intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)，
//    Uri就不能用Uri.fromFile(file)
//    而是要FileProvider.getUriForFile(activity, Constants.FILE_CONTENT_FILEPROVIDER, file);
//    但是裁剪的时候就不一样，裁剪继续使用 Uri.fromFile(file)。

    override fun onStop() {
        if (videoView.isPlaying)
            videoView.pause()
        super.onStop()
    }

}