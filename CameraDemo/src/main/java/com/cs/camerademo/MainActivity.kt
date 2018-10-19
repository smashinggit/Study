package com.cs.camerademo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cs.camerademo.camera1.CameraActivity
import com.cs.camerademo.camera2.CameraActivity2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btCapture.setOnClickListener { startActivity(Intent(this, CaptureActivity::class.java)) }
        btCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra(CameraActivity.TYPE_TAG, CameraActivity.TYPE_CAPTURE)
            startActivity(intent)
        }
        btCamera2.setOnClickListener {
            val intent = Intent(this, CameraActivity2::class.java)
            startActivity(intent)
        }
        btCameraRecord.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra(CameraActivity.TYPE_TAG, CameraActivity.TYPE_RECORD)
            startActivity(intent)
        }
    }
}

