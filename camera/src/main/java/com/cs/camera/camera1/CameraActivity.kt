package com.cs.camera.camera1

import android.graphics.RectF
import android.hardware.Camera
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.cs.camera.R
import com.cs.camera.toast
import com.cs.camera.util.BitmapUtils
import kotlinx.android.synthetic.main.activity_camera.*

/**
 * author :  chensen
 * data  :  2018/3/18
 * desc :
 */
class CameraActivity : AppCompatActivity() {
    companion object {
        const val TYPE_TAG = "type"
        const val TYPE_CAPTURE = 0
        const val TYPE_RECORD = 1
    }

    var lock = false //控制MediaRecorderHelper的初始化

    private lateinit var mCameraHelper: CameraHelper
    private var mMediaRecorderHelper: MediaRecorderHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mCameraHelper = CameraHelper(this, surfaceView)
        mCameraHelper.addCallBack(object : CameraHelper.CallBack {
            override fun onFaceDetect(faces: ArrayList<RectF>) {
                faceView.setFaces(faces)
            }

            override fun onTakePic(data: ByteArray?) {
                savePic(data)
                btnTakePic.isClickable = true
            }

            override fun onPreviewFrame(data: ByteArray?) {
                if (!lock) {
                    mCameraHelper.getCamera()?.let {
                        mMediaRecorderHelper = MediaRecorderHelper(this@CameraActivity, mCameraHelper.getCamera()!!, mCameraHelper.mDisplayOrientation, mCameraHelper.mSurfaceHolder.surface)
                    }
                    lock = true
                }
            }
        })

        if (intent.getIntExtra(TYPE_TAG, 0) == TYPE_RECORD) { //录视频
            btnTakePic.visibility = View.GONE
            btnStart.visibility = View.VISIBLE
        }

        btnTakePic.setOnClickListener { mCameraHelper.takePic() }
        ivExchange.setOnClickListener { mCameraHelper.exchangeCamera() }
        btnStart.setOnClickListener {
            ivExchange.isClickable = false
            btnStart.visibility = View.GONE
            btnStop.visibility = View.VISIBLE
            mMediaRecorderHelper?.startRecord()
        }
        btnStop.setOnClickListener {
            btnStart.visibility = View.VISIBLE
            btnStop.visibility = View.GONE
            ivExchange.isClickable = true
            mMediaRecorderHelper?.stopRecord()
        }
    }

    private fun savePic(data: ByteArray?) {

        BitmapUtils.savePic(data, "camera1", mCameraHelper.mCameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT, { savedPath, time ->
            runOnUiThread {
                toast("图片保存成功！ 保存路径：$savedPath 耗时：$time")
            }
        }, { msg ->
            runOnUiThread {
                toast("图片保存失败！ $msg")
            }
        })
    }

    override fun onDestroy() {
        mCameraHelper.releaseCamera()
        mMediaRecorderHelper?.let {
            if (it.isRunning)
                it.stopRecord()
            it.release()
        }
        super.onDestroy()
    }

}