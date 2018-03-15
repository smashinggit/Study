package com.cs.camerademo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import com.cs.camerademo.util.FileUtil
import kotlinx.android.synthetic.main.activity_capture.*

/**
 * author :  chensen
 * data  :  2018/3/15
 * desc :
 */
class CaptureActivity : AppCompatActivity() {
    companion object {
        val REQUEST_CODE_CAPTURE_SMALL = 1
        val REQUEST_CODE_CAPTURE_RAW = 2
        val REQUEST_CODE_CAPTURE_CLIP = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)

        //拍照(返回缩略图)
        btnCaptureSmall.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)?.let {
                startActivityForResult(intent, REQUEST_CODE_CAPTURE_SMALL)
            }
        }

        //拍照(返回原始图)
        btnCaptureRaw.setOnClickListener {
            var imageFile = FileUtil.createImageFile()
            imageFile?.let {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(it))

                intent.resolveActivity(packageManager)?.let {
                    startActivityForResult(intent, REQUEST_CODE_CAPTURE_RAW)
                }
            }
        }

        btnCaptureAndClip.setOnClickListener { }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                REQUEST_CODE_CAPTURE_SMALL -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    ivResult.setImageBitmap(bitmap)
                }

                REQUEST_CODE_CAPTURE_RAW -> {
                    var bitmap = data?.extras?.get("data") as Bitmap
                    ivResult.setImageBitmap(bitmap)
                }

            }
        }

    }
}