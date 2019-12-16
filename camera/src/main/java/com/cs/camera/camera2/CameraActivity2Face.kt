package com.cs.camera.camera2

import android.graphics.RectF
import android.hardware.camera2.params.Face
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import com.cs.camera.R
import kotlinx.android.synthetic.main.activity_camera2_face.*


/**
 * author :  chensen
 * data  :  2018/3/18
 * desc :
 */
class CameraActivity2Face : AppCompatActivity(), Camera2HelperFace.FaceDetectListener {
    private lateinit var camera2HelperFace: Camera2HelperFace

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera2_face)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        camera2HelperFace = Camera2HelperFace(this, textureView)
        camera2HelperFace.setFaceDetectListener(this)

        btnTakePic.setOnClickListener { camera2HelperFace.takePic() }
        ivExchange.setOnClickListener { camera2HelperFace.exchangeCamera() }
    }

    override fun onFaceDetect(faces: Array<Face>, facesRect: ArrayList<RectF>) {
        faceView.setFaces(facesRect)
    }

    override fun onDestroy() {
        super.onDestroy()
        camera2HelperFace.releaseCamera()
        camera2HelperFace.releaseThread()
    }

}

