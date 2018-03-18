package com.cs.camerademo.camera1

import android.app.Activity
import android.graphics.ImageFormat
import android.hardware.Camera
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast

/**
 * author :  chensen
 * data  :  2018/3/17
 * desc :
 */
class CameraHelper : Camera.PreviewCallback {

    private var mCamera: Camera? = null
    private var mSurfaceView: SurfaceView
    private var mSurfaceHolder: SurfaceHolder
    private var mActivity: Activity
    private var mCallBack: CallBack? = null
    var mCameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK

    private var picWidth = 1080        //保存图片的宽
    private var picHeight = 1920       //保存图片的高
    private var surfaceViewWidth = 0   //预览区域的宽
    private var surfaceViewHeight = 0  //预览区域的高

    constructor(activity: Activity, surfaceView: SurfaceView) {
        mSurfaceView = surfaceView
        mSurfaceHolder = mSurfaceView.holder
        mActivity = activity
        init()
    }

    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
        mCallBack?.onPreviewFrame(data, camera)
    }

    fun takePic() {
        mCamera?.let {
            it.takePicture({}, null, { data, camera ->
                it.startPreview()
                mCallBack?.onTakePic(data, camera)
            })
        }
    }

    private fun init() {
        mSurfaceHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                releaseCamera()
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                surfaceViewWidth = mSurfaceView.width
                surfaceViewHeight = mSurfaceView.height
                if (mCamera == null) {
                    openCamera(mCameraFacing)
                }
                startPreview()
            }
        })
    }

    private fun openCamera(cameraFacing: Int = Camera.CameraInfo.CAMERA_FACING_BACK): Boolean {
        var supportCameraFacing = supportCameraFacing(cameraFacing)
        if (supportCameraFacing) {
            try {
                mCamera = Camera.open(cameraFacing)
                initParameters(mCamera!!)
                mCamera?.setPreviewCallback(this)
            } catch (e: Exception) {
                e.printStackTrace()
                toast("打开相机失败!")
                return false
            }
        }
        return supportCameraFacing
    }

    private fun initParameters(camera: Camera) {
        try {
            val parameters = camera.parameters
            parameters.previewFormat = ImageFormat.NV21
            val bestPreviewSize = getBestSize(mSurfaceView.width, mSurfaceView.height, parameters.supportedPreviewSizes)
            bestPreviewSize?.let {
                parameters.setPreviewSize(it.width, it.height)
            }
            val bestPicSize = getBestSize(picWidth, picHeight, parameters.supportedPictureSizes)
            bestPicSize?.let {
                parameters.setPictureSize(it.width, it.height)
            }
            camera.parameters = parameters
        } catch (e: Exception) {
            e.printStackTrace()
            toast("相机初始化失败!")
        }
    }

    fun startPreview() {
        mCamera?.let {
            it.setPreviewDisplay(mSurfaceHolder)
            setCameraDisplayOrientation(mActivity)
            it.startPreview()
        }
    }

    fun exchangeCamera() {
        releaseCamera()
        mCameraFacing = if (mCameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK)
            Camera.CameraInfo.CAMERA_FACING_FRONT
        else
            Camera.CameraInfo.CAMERA_FACING_BACK

        openCamera(mCameraFacing)
        startPreview()
    }

    fun releaseCamera() {
        if (mCamera != null) {
            mCamera?.stopPreview()
            mCamera?.setPreviewCallback(null)
            mCamera?.release()
            mCamera = null
        }
    }

    private fun getBestSize(targetWidth: Int, targetHeight: Int, sizeList: List<Camera.Size>): Camera.Size? {
        var bestSize: Camera.Size? = null
        var targetRatio = (targetHeight.toDouble() / targetWidth)  //目标大小的宽高比
        var minDiff = targetRatio

        for (size in sizeList) {
            if (size.width == targetHeight && size.height == targetWidth) {
                bestSize = size
                break
            }

            var supportedRatio = (size.width.toDouble() / size.height)
            if (Math.abs(supportedRatio - targetRatio) < minDiff) {
                minDiff = Math.abs(supportedRatio - targetRatio)
                bestSize = size
            }
            log("系统支持的尺寸 : ${size.width} * ${size.height} ,    比例$supportedRatio")
        }
        log("目标尺寸 ：$targetWidth * $targetHeight ，   比例  $targetRatio")
        log("最优尺寸 ：${bestSize?.height} * ${bestSize?.width}")
        return bestSize
    }

    private fun setCameraDisplayOrientation(activity: Activity) {
        var info = Camera.CameraInfo()
        Camera.getCameraInfo(mCameraFacing, info)
        val rotation = activity.windowManager.defaultDisplay.rotation

        var screenDegree = 0
        when (rotation) {
            Surface.ROTATION_0 -> screenDegree = 0
            Surface.ROTATION_90 -> screenDegree = 90
            Surface.ROTATION_180 -> screenDegree = 180
            Surface.ROTATION_270 -> screenDegree = 270
        }

        var result: Int
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + screenDegree) % 360
            result = (360 - result) % 360          // compensate the mirror
        } else {
            result = (info.orientation - screenDegree + 360) % 360
        }
        mCamera?.setDisplayOrientation(result)

        log("屏幕的旋转角度 : $rotation")
        log("setDisplayOrientation(result) : $result")
    }

    private fun supportCameraFacing(cameraFacing: Int): Boolean {
        var info = Camera.CameraInfo()
        for (i in 0 until Camera.getNumberOfCameras()) {
            Camera.getCameraInfo(i, info)
            if (info.facing == cameraFacing) return true
        }
        return false
    }


    private fun toast(msg: String) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun log(msg: String) {
        Log.e("tag", msg)
    }

    fun addCallBack(callBack: CallBack) {
        this.mCallBack = callBack
    }

    interface CallBack {
        fun onPreviewFrame(data: ByteArray?, camera: Camera?)
        fun onTakePic(data: ByteArray?, camera: Camera?)
    }
}