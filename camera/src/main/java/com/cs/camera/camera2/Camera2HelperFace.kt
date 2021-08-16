package com.cs.camera.camera2

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.Face
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.Surface
import android.view.TextureView
import com.cs.camera.log
import com.cs.camera.toast
import com.cs.camera.util.BitmapUtils
import com.cs.camera.view.AutoFitTextureView
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * author : ChenSen
 * data : 2018/11/7
 * desc:
 */
class Camera2HelperFace(val mActivity: Activity, private val mTextureView: AutoFitTextureView) {

    companion object {
        const val PREVIEW_WIDTH = 1080                                        //预览的宽度
        const val PREVIEW_HEIGHT = 1440                                       //预览的高度
        const val SAVE_WIDTH = 720                                            //保存图片的宽度
        const val SAVE_HEIGHT = 1280                                          //保存图片的高度
    }

    private lateinit var mCameraManager: CameraManager
    private var mImageReader: ImageReader? = null
    private var mCameraDevice: CameraDevice? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null

    private var mCameraId = "0"
    private lateinit var mCameraCharacteristics: CameraCharacteristics

    private var mCameraSensorOrientation = 0                                            //摄像头方向
    private var mCameraFacing = CameraCharacteristics.LENS_FACING_BACK              //默认使用后置摄像头
    private val mDisplayRotation = mActivity.windowManager.defaultDisplay.rotation  //手机方向
    private var mFaceDetectMode = CaptureResult.STATISTICS_FACE_DETECT_MODE_OFF     //人脸检测模式

    private var canTakePic = true                                                       //是否可以拍照
    private var canExchangeCamera = false                                               //是否可以切换摄像头
    private var openFaceDetect = true                                                   //是否开启人脸检测
    private var mFaceDetectMatrix = Matrix()                                            //人脸检测坐标转换矩阵
    private var mFacesRect = ArrayList<RectF>()                                         //保存人脸坐标信息
    private var mFaceDetectListener: FaceDetectListener? = null                         //人脸检测回调

    private var mCameraHandler: Handler
    private val handlerThread = HandlerThread("CameraThread")

    private var mPreviewSize = Size(PREVIEW_WIDTH, PREVIEW_HEIGHT)                      //预览大小
    private var mSavePicSize = Size(SAVE_WIDTH, SAVE_HEIGHT)                            //保存图片大小

    interface FaceDetectListener {
        fun onFaceDetect(faces: Array<Face>, facesRect: ArrayList<RectF>)
    }

    fun setFaceDetectListener(listener: FaceDetectListener) {
        this.mFaceDetectListener = listener
    }

    init {
        handlerThread.start()
        mCameraHandler = Handler(handlerThread.looper)

        mTextureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                configureTransform(width, height)
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                releaseCamera()
                return true
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                configureTransform(width, height)
                initCameraInfo()
            }
        }
    }

    /**
     * 初始化
     */
    private fun initCameraInfo() {
        mCameraManager = mActivity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraIdList = mCameraManager.cameraIdList
        if (cameraIdList.isEmpty()) {
            mActivity.toast("没有可用相机")
            return
        }

        for (id in cameraIdList) {
            val cameraCharacteristics = mCameraManager.getCameraCharacteristics(id)
            val facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)

            if (facing == mCameraFacing) {
                mCameraId = id
                mCameraCharacteristics = cameraCharacteristics
            }
            log("设备中的摄像头 $id")
        }

        val supportLevel = mCameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
        if (supportLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
            mActivity.toast("相机硬件不支持新特性")
        }

        //获取摄像头方向
        mCameraSensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!

        //获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
        val configurationMap = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

        val savePicSize = configurationMap?.getOutputSizes(ImageFormat.JPEG)          //保存照片尺寸
        val previewSize = configurationMap?.getOutputSizes(SurfaceTexture::class.java) //预览尺寸

        val exchange = exchangeWidthAndHeight(mDisplayRotation, mCameraSensorOrientation)

        mSavePicSize = getBestSize(
                if (exchange) mSavePicSize.height else mSavePicSize.width,
                if (exchange) mSavePicSize.width else mSavePicSize.height,
                if (exchange) mSavePicSize.height else mSavePicSize.width,
                if (exchange) mSavePicSize.width else mSavePicSize.height,
                savePicSize?.toList() ?: emptyList())

        mPreviewSize = getBestSize(
                if (exchange) mPreviewSize.height else mPreviewSize.width,
                if (exchange) mPreviewSize.width else mPreviewSize.height,
                if (exchange) mTextureView.height else mTextureView.width,
                if (exchange) mTextureView.width else mTextureView.height,
                previewSize?.toList() ?: emptyList())

        mTextureView.surfaceTexture?.setDefaultBufferSize(mPreviewSize.width, mPreviewSize.height)

        log("预览最优尺寸 ：${mPreviewSize.width} * ${mPreviewSize.height}, 比例  ${mPreviewSize.width.toFloat() / mPreviewSize.height}")
        log("保存图片最优尺寸 ：${mSavePicSize.width} * ${mSavePicSize.height}, 比例  ${mSavePicSize.width.toFloat() / mSavePicSize.height}")

        //根据预览的尺寸大小调整TextureView的大小，保证画面不被拉伸
        val orientation = mActivity.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            mTextureView.setAspectRatio(mPreviewSize.width, mPreviewSize.height)
        else
            mTextureView.setAspectRatio(mPreviewSize.height, mPreviewSize.width)

        mImageReader = ImageReader.newInstance(mPreviewSize.width, mPreviewSize.height, ImageFormat.JPEG, 1)
        mImageReader?.setOnImageAvailableListener(onImageAvailableListener, mCameraHandler)

        if (openFaceDetect)
            initFaceDetect()

        openCamera()
    }

    private val onImageAvailableListener = OnImageAvailableListener {

        val image = it.acquireNextImage()
        val byteBuffer = image.planes[0].buffer
        val byteArray = ByteArray(byteBuffer.remaining())
        byteBuffer.get(byteArray)
        image.close()

        BitmapUtils.savePic(byteArray, "camera2",mCameraSensorOrientation == 270, { savedPath, time ->
            mActivity.runOnUiThread {
                mActivity.toast("图片保存成功！ 保存路径：$savedPath 耗时：$time")
            }
        }, { msg ->
            mActivity.runOnUiThread {
                mActivity.toast("图片保存失败！ $msg")
            }
        })
    }


    /**
     * 初始化人脸检测相关信息
     */
    private fun initFaceDetect() {

        val faceDetectCount = mCameraCharacteristics.get(CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT)    //同时检测到人脸的数量
        val faceDetectModes = mCameraCharacteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES)  //人脸检测的模式

        if (faceDetectModes == null) {
            mActivity.toast("相机硬件不支持人脸检测")
            return
        }

        mFaceDetectMode = when {
            faceDetectModes.contains(CaptureRequest.STATISTICS_FACE_DETECT_MODE_FULL) -> CaptureRequest.STATISTICS_FACE_DETECT_MODE_FULL
            faceDetectModes.contains(CaptureRequest.STATISTICS_FACE_DETECT_MODE_SIMPLE) -> CaptureRequest.STATISTICS_FACE_DETECT_MODE_FULL
            else -> CaptureRequest.STATISTICS_FACE_DETECT_MODE_OFF
        }

        if (mFaceDetectMode == CaptureRequest.STATISTICS_FACE_DETECT_MODE_OFF) {
            mActivity.toast("相机硬件不支持人脸检测")
            return
        }

        val activeArraySizeRect = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE)!! //获取成像区域
        val scaledWidth = mPreviewSize.width / activeArraySizeRect.width().toFloat()
        val scaledHeight = mPreviewSize.height / activeArraySizeRect.height().toFloat()
        val mirror = mCameraFacing == CameraCharacteristics.LENS_FACING_FRONT

        mFaceDetectMatrix.setRotate(mCameraSensorOrientation.toFloat())
        mFaceDetectMatrix.postScale(if (mirror) -scaledWidth else scaledWidth, scaledHeight)
        if (exchangeWidthAndHeight(mDisplayRotation, mCameraSensorOrientation))
            mFaceDetectMatrix.postTranslate(mPreviewSize.height.toFloat(), mPreviewSize.width.toFloat())


        log("成像区域  ${activeArraySizeRect.width()}  ${activeArraySizeRect.height()} 比例: ${activeArraySizeRect.width().toFloat() / activeArraySizeRect.height()}")
        log("预览区域  ${mPreviewSize.width}  ${mPreviewSize.height} 比例 ${mPreviewSize.width.toFloat() / mPreviewSize.height}")

        for (mode in faceDetectModes) {
            log("支持的人脸检测模式 $mode")
        }
        log("同时检测到人脸的数量 $faceDetectCount")
    }

    /**
     * 打开相机
     */
    @SuppressLint("MissingPermission")
    private fun openCamera() {
        mCameraManager.openCamera(mCameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                log("onOpened")
                mCameraDevice = camera
                createCaptureSession(camera)
            }

            override fun onDisconnected(camera: CameraDevice) {
                log("onDisconnected")
            }

            override fun onError(camera: CameraDevice, error: Int) {
                log("onError $error")
                mActivity.toast("打开相机失败！$error")
            }
        }, mCameraHandler)
    }

    /**
     * 创建预览会话
     */
    private fun createCaptureSession(cameraDevice: CameraDevice) {

        val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

        val surface = Surface(mTextureView.surfaceTexture)
        captureRequestBuilder.addTarget(surface)  // 将CaptureRequest的构建器与Surface对象绑定在一起
        captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)      // 闪光灯
        captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE) // 自动对焦
        if (openFaceDetect && mFaceDetectMode != CaptureRequest.STATISTICS_FACE_DETECT_MODE_OFF)
            captureRequestBuilder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, CameraCharacteristics.STATISTICS_FACE_DETECT_MODE_SIMPLE)//人脸检测

        // 为相机预览，创建一个CameraCaptureSession对象
        cameraDevice.createCaptureSession(arrayListOf(surface, mImageReader?.surface), object : CameraCaptureSession.StateCallback() {
            override fun onConfigureFailed(session: CameraCaptureSession) {
                mActivity.toast("开启预览会话失败！")
            }

            override fun onConfigured(session: CameraCaptureSession) {
                mCameraCaptureSession = session
                session.setRepeatingRequest(captureRequestBuilder.build(), mCaptureCallBack, mCameraHandler)
            }

        }, mCameraHandler)
    }

    private val mCaptureCallBack = object : CameraCaptureSession.CaptureCallback() {

        override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
            super.onCaptureCompleted(session, request, result)
            if (openFaceDetect && mFaceDetectMode != CaptureRequest.STATISTICS_FACE_DETECT_MODE_OFF)
                handleFaces(result)

            canExchangeCamera = true
            canTakePic = true
        }

        override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {
            super.onCaptureFailed(session, request, failure)
            log("onCaptureFailed")
            mActivity.toast("开启预览失败！")
        }
    }

    /**
     * 处理人脸信息
     */
    private fun handleFaces(result: TotalCaptureResult) {
        val faces = result.get(CaptureResult.STATISTICS_FACES)
        mFacesRect.clear()

        if (faces != null) {
            for (face in faces) {
                val bounds = face.bounds
                val left = bounds.left
                val top = bounds.top
                val right = bounds.right
                val bottom = bounds.bottom

                val rawFaceRect = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
                mFaceDetectMatrix.mapRect(rawFaceRect)

                val resultFaceRect = if (mCameraFacing == CaptureRequest.LENS_FACING_FRONT)
                    rawFaceRect
                else
                    RectF(rawFaceRect.left, rawFaceRect.top - mPreviewSize.width, rawFaceRect.right, rawFaceRect.bottom - mPreviewSize.width)

                mFacesRect.add(resultFaceRect)

                log("原始人脸位置: ${bounds.width()} * ${bounds.height()}   ${bounds.left} ${bounds.top} ${bounds.right} ${bounds.bottom}   分数: ${face.score}")
                log("转换后人脸位置: ${resultFaceRect.width()} * ${resultFaceRect.height()}   ${resultFaceRect.left} ${resultFaceRect.top} ${resultFaceRect.right} ${resultFaceRect.bottom}   分数: ${face.score}")
            }
        }

        mActivity.runOnUiThread {
            if (faces != null) {
                mFaceDetectListener?.onFaceDetect(faces, mFacesRect)
            }
        }
        log("onCaptureCompleted  检测到 ${faces?.size} 张人脸")
    }


    /**
     * 拍照
     */
    fun takePic() {
        if (mCameraDevice == null || !mTextureView.isAvailable || !canTakePic) return

        mCameraDevice?.apply {

            val captureRequestBuilder = createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureRequestBuilder.addTarget(mImageReader!!.surface)

            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE) // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)      // 闪光灯
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, mCameraSensorOrientation)      //根据摄像头方向对保存的照片进行旋转，使其为"自然方向"
            mCameraCaptureSession?.capture(captureRequestBuilder.build(), null, mCameraHandler)
                    ?: mActivity.toast("拍照异常！")
        }
    }

    /**
     * 切换摄像头
     */
    fun exchangeCamera() {
        if (mCameraDevice == null || !canExchangeCamera || !mTextureView.isAvailable) return

        mCameraFacing = if (mCameraFacing == CameraCharacteristics.LENS_FACING_FRONT)
            CameraCharacteristics.LENS_FACING_BACK
        else
            CameraCharacteristics.LENS_FACING_FRONT

        mPreviewSize = Size(PREVIEW_WIDTH, PREVIEW_HEIGHT) //重置预览大小
        releaseCamera()
        initCameraInfo()
    }

    /**
     *
     * 根据提供的参数值返回与指定宽高相等或最接近的尺寸
     *
     * @param targetWidth   目标宽度
     * @param targetHeight  目标高度
     * @param maxWidth      最大宽度(即TextureView的宽度)
     * @param maxHeight     最大高度(即TextureView的高度)
     * @param sizeList      支持的Size列表
     *
     * @return  返回与指定宽高相等或最接近的尺寸
     *
     */
    private fun getBestSize(targetWidth: Int, targetHeight: Int, maxWidth: Int, maxHeight: Int, sizeList: List<Size>): Size {
        val bigEnough = ArrayList<Size>()     //比指定宽高大的Size列表
        val notBigEnough = ArrayList<Size>()  //比指定宽高小的Size列表

        for (size in sizeList) {

            //宽<=最大宽度  &&  高<=最大高度  &&  宽高比 == 目标值宽高比
            if (size.width <= maxWidth && size.height <= maxHeight
                    && size.width == size.height * targetWidth / targetHeight) {

                if (size.width >= targetWidth && size.height >= targetHeight)
                    bigEnough.add(size)
                else
                    notBigEnough.add(size)
            }
            log("系统支持的尺寸: ${size.width} * ${size.height} ,  比例 ：${size.width.toFloat() / size.height}")
        }

        log("最大尺寸 ：$maxWidth * $maxHeight, 比例 ：${targetWidth.toFloat() / targetHeight}")
        log("目标尺寸 ：$targetWidth * $targetHeight, 比例 ：${targetWidth.toFloat() / targetHeight}")

        //选择bigEnough中最小的值  或 notBigEnough中最大的值
        return when {
            bigEnough.size > 0 -> Collections.min(bigEnough, CompareSizesByArea())
            notBigEnough.size > 0 -> Collections.max(notBigEnough, CompareSizesByArea())
            else -> sizeList[0]
        }
    }

    /**
     * 根据提供的屏幕方向 [displayRotation] 和相机方向 [sensorOrientation] 返回是否需要交换宽高
     */
    private fun exchangeWidthAndHeight(displayRotation: Int, sensorOrientation: Int): Boolean {
        var exchange = false
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    exchange = true
                }
            Surface.ROTATION_90, Surface.ROTATION_270 ->
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    exchange = true
                }
            else -> log("Display rotation is invalid: $displayRotation")
        }

        log("屏幕方向  $displayRotation")
        log("相机方向  $sensorOrientation")
        return exchange
    }


    fun releaseCamera() {
        mCameraCaptureSession?.close()
        mCameraCaptureSession = null

        mCameraDevice?.close()
        mCameraDevice = null

        mImageReader?.close()
        mImageReader = null

        canExchangeCamera = false
    }

    fun releaseThread() {
        handlerThread.quitSafely()
        handlerThread.join()
    }


    /**
     * Configures the necessary [android.graphics.Matrix] transformation to `mTextureView`.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private fun configureTransform(viewWidth: Int, viewHeight: Int) {
        val rotation = mActivity.windowManager.defaultDisplay.rotation
        val matrix = Matrix()
        val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        val bufferRect = RectF(0f, 0f, mPreviewSize.height.toFloat(), mPreviewSize.width.toFloat())
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
            val scale = Math.max(
                    viewHeight.toFloat() / mPreviewSize.height,
                    viewWidth.toFloat() / mPreviewSize.width)
            matrix.postScale(scale, scale, centerX, centerY)
            matrix.postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180f, centerX, centerY)
        }
        mTextureView.setTransform(matrix)
        log("configureTransform $viewWidth  $viewHeight")
    }

    private class CompareSizesByArea : Comparator<Size> {
        override fun compare(size1: Size, size2: Size): Int {
            return java.lang.Long.signum(size1.width.toLong() * size1.height - size2.width.toLong() * size2.height)
        }
    }
}