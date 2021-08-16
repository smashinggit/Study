package com.cs.camera.camera1

import android.app.Activity
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.RectF
import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Toast
import com.cs.camera.log

/**
 * author :  chensen
 * data  :  2018/3/17
 * desc :
 */
class CameraHelper(activity: Activity, surfaceView: SurfaceView) : Camera.PreviewCallback {

    private var mCamera: Camera? = null                   //Camera对象
    private lateinit var mParameters: Camera.Parameters   //Camera对象的参数
    private var mSurfaceView: SurfaceView = surfaceView   //用于预览的SurfaceView对象
    var mSurfaceHolder: SurfaceHolder                     //SurfaceHolder对象

    private var mActivity: Activity = activity
    private var mCallBack: CallBack? = null   //自定义的回调

    var mCameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK  //摄像头方向
    var mDisplayOrientation: Int = 0    //预览旋转的角度

    private var picWidth = 2160        //保存图片的宽
    private var picHeight = 3840       //保存图片的高

    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
        mCallBack?.onPreviewFrame(data)
    }

    fun takePic() {
        mCamera?.let {
            it.takePicture({}, null, { data, _ ->
                it.startPreview()
                mCallBack?.onTakePic(data)
            })
        }
    }

    private fun init() {
        mSurfaceHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                releaseCamera()
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                if (mCamera == null) {
                    openCamera(mCameraFacing)
                }
                startPreview()
            }
        })
    }

    //打开相机
    private fun openCamera(cameraFacing: Int = Camera.CameraInfo.CAMERA_FACING_BACK): Boolean {
        val supportCameraFacing = supportCameraFacing(cameraFacing)
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

    //配置相机参数
    private fun initParameters(camera: Camera) {
        try {
            mParameters = camera.parameters
            mParameters.previewFormat = ImageFormat.NV21

            //获取与指定宽高相等或最接近的尺寸
            //设置预览尺寸
            val bestPreviewSize = getBestSize(mSurfaceView.width, mSurfaceView.height, mParameters.supportedPreviewSizes)
            bestPreviewSize?.let {
                mParameters.setPreviewSize(it.width, it.height)
            }
            //设置保存图片尺寸
            val bestPicSize = getBestSize(picWidth, picHeight, mParameters.supportedPictureSizes)
            bestPicSize?.let {
                mParameters.setPictureSize(it.width, it.height)
            }
            //对焦模式
            if (isSupportFocus(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
                mParameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE

            camera.parameters = mParameters
        } catch (e: Exception) {
            e.printStackTrace()
            toast("相机初始化失败!")
        }
    }

    //开始预览
    fun startPreview() {
        mCamera?.let {
            it.setPreviewDisplay(mSurfaceHolder)
            setCameraDisplayOrientation(mActivity)
            it.startPreview()
            startFaceDetect()
        }
    }

    private fun startFaceDetect() {
        mCamera?.let {
            it.startFaceDetection()
            it.setFaceDetectionListener { faces, _ ->
                mCallBack?.onFaceDetect(transForm(faces))
                log("检测到 ${faces.size} 张人脸")
            }
        }
    }

    //判断是否支持某一对焦模式
    private fun isSupportFocus(focusMode: String): Boolean {
        var autoFocus = false
        val listFocusMode = mParameters.supportedFocusModes
        for (mode in listFocusMode) {
            if (mode == focusMode)
                autoFocus = true
            log("相机支持的对焦模式： $mode")
        }
        return autoFocus
    }

    //切换摄像头
    fun exchangeCamera() {
        releaseCamera()
        mCameraFacing = if (mCameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK)
            Camera.CameraInfo.CAMERA_FACING_FRONT
        else
            Camera.CameraInfo.CAMERA_FACING_BACK

        openCamera(mCameraFacing)
        startPreview()
    }

    //释放相机
    fun releaseCamera() {
        if (mCamera != null) {
            // mCamera?.stopFaceDetection()
            mCamera?.stopPreview()
            mCamera?.setPreviewCallback(null)
            mCamera?.release()
            mCamera = null
        }
    }

    //获取与指定宽高相等或最接近的尺寸
    private fun getBestSize(targetWidth: Int, targetHeight: Int, sizeList: List<Camera.Size>): Camera.Size? {
        var bestSize: Camera.Size? = null
        val targetRatio = (targetHeight.toDouble() / targetWidth)  //目标大小的宽高比
        var minDiff = targetRatio

        for (size in sizeList) {
            val supportedRatio = (size.width.toDouble() / size.height)
            log("系统支持的尺寸 : ${size.width} * ${size.height} ,    比例$supportedRatio")
        }

        for (size in sizeList) {
            if (size.width == targetHeight && size.height == targetWidth) {
                bestSize = size
                break
            }

            val supportedRatio = (size.width.toDouble() / size.height)
            if (Math.abs(supportedRatio - targetRatio) < minDiff) {
                minDiff = Math.abs(supportedRatio - targetRatio)
                bestSize = size
            }
        }
        log("目标尺寸 ：$targetWidth * $targetHeight ，   比例  $targetRatio")
        log("最优尺寸 ：${bestSize?.height} * ${bestSize?.width}")
        return bestSize
    }

    //设置预览旋转的角度
    private fun setCameraDisplayOrientation(activity: Activity) {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(mCameraFacing, info)
        val rotation = activity.windowManager.defaultDisplay.rotation

        var screenDegree = 0
        when (rotation) {
            Surface.ROTATION_0 -> screenDegree = 0
            Surface.ROTATION_90 -> screenDegree = 90
            Surface.ROTATION_180 -> screenDegree = 180
            Surface.ROTATION_270 -> screenDegree = 270
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mDisplayOrientation = (info.orientation + screenDegree) % 360
            mDisplayOrientation = (360 - mDisplayOrientation) % 360          // compensate the mirror
        } else {
            mDisplayOrientation = (info.orientation - screenDegree + 360) % 360
        }
        mCamera?.setDisplayOrientation(mDisplayOrientation)

        log("屏幕的旋转角度 : $rotation")
        log("setDisplayOrientation(result) : $mDisplayOrientation")
    }

    //判断是否支持某个相机
    private fun supportCameraFacing(cameraFacing: Int): Boolean {
        val info = Camera.CameraInfo()
        for (i in 0 until Camera.getNumberOfCameras()) {
            Camera.getCameraInfo(i, info)
            if (info.facing == cameraFacing) return true
        }
        return false
    }

    //将相机中用于表示人脸矩形的坐标转换成UI页面的坐标
    private fun transForm(faces: Array<Camera.Face>): ArrayList<RectF> {
        val matrix = Matrix()
        // Need mirror for front camera.
        val mirror = (mCameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        matrix.setScale(if (mirror) -1f else 1f, 1f)
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(mDisplayOrientation.toFloat())
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(mSurfaceView.width / 2000f, mSurfaceView.height / 2000f)
        matrix.postTranslate(mSurfaceView.width / 2f, mSurfaceView.height / 2f)

        val rectList = ArrayList<RectF>()
        for (face in faces) {
            val srcRect = RectF(face.rect)
            val dstRect = RectF(0f, 0f, 0f, 0f)
            matrix.mapRect(dstRect, srcRect)
            rectList.add(dstRect)
        }
        return rectList
    }


    private fun toast(msg: String) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show()
    }

    fun getCamera(): Camera? = mCamera

    fun addCallBack(callBack: CallBack) {
        this.mCallBack = callBack
    }

    interface CallBack {
        fun onPreviewFrame(data: ByteArray?)
        fun onTakePic(data: ByteArray?)
        fun onFaceDetect(faces: ArrayList<RectF>)
    }

    init {
        mSurfaceHolder = mSurfaceView.holder
        init()
    }
}