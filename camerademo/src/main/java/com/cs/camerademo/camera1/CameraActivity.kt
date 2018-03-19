package com.cs.camerademo.camera1

import android.graphics.BitmapFactory
import android.graphics.Rect
import android.hardware.Camera
import android.os.Bundle
import android.view.WindowManager
import com.cs.camerademo.BaseActivity
import com.cs.camerademo.R
import com.cs.camerademo.util.BitmapUtils
import com.cs.camerademo.util.FileUtil
import kotlinx.android.synthetic.main.activity_camera.*
import okio.Okio
import kotlin.concurrent.thread

/**
 * author :  chensen
 * data  :  2018/3/18
 * desc :
 */
class CameraActivity : BaseActivity() {
    private lateinit var mCameraHelper: CameraHelper

    private var isFaceDetect = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        btnTakePic.setOnClickListener { mCameraHelper.takePic() }
        ivExchange.setOnClickListener { mCameraHelper.exchangeCamera() }

        mCameraHelper = CameraHelper(this, surfaceView)
        mCameraHelper.addCallBack(object : CameraHelper.CallBack {
            override fun onFaceDetect(faces: Array<Camera.Face>?) {
                faces?.let {
                    faceView.setFaces(faces)
                }
            }

            override fun onTakePic(data: ByteArray?) {
                savePic(data)
                btnTakePic.isClickable = true
            }

            override fun onPreviewFrame(data: ByteArray?) {
            }
        })
    }

    private fun savePic(data: ByteArray?) {
        thread {
            try {
                val temp = System.currentTimeMillis()
                val picFile = FileUtil.createCameraFile()
                if (picFile != null && data != null) {
                    val rawBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                    val resultBitmap = if (mCameraHelper.mCameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                        BitmapUtils.mirror(BitmapUtils.rotate(rawBitmap, 270f))
                    else
                        BitmapUtils.rotate(rawBitmap, 90f)

                    Okio.buffer(Okio.sink(picFile)).write(BitmapUtils.toByteArray(resultBitmap)).close()
                    runOnUiThread {
                        toast("图片已保存! ${picFile.absolutePath}")
                        log("图片已保存! 耗时：${System.currentTimeMillis() - temp}    路径：  ${picFile.absolutePath}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    toast("保存图片失败！")
                }
            }
        }
    }

    override fun onDestroy() {
        mCameraHelper.releaseCamera()
        super.onDestroy()
    }

}