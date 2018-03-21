package com.cs.camerademo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cs.camerademo.camera1.CameraActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btCapture.setOnClickListener { startActivity(Intent(this, CaptureActivity::class.java)) }
        btCamera.setOnClickListener {
            var intent = Intent(this, CameraActivity::class.java)
            intent.putExtra(CameraActivity.TYPE_TAG, CameraActivity.TYPE_CAPTURE)
            startActivity(intent)
        }
        btCameraRecord.setOnClickListener {
            var intent = Intent(this, CameraActivity::class.java)
            intent.putExtra(CameraActivity.TYPE_TAG, CameraActivity.TYPE_RECORD)
            startActivity(intent)
        }
    }
}

