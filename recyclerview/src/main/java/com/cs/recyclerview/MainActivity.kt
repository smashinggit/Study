package com.cs.recyclerview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cs.recyclerview.ui.DragActivity
import com.cs.recyclerview.ui.PicActivity
import com.cs.recyclerview.ui.SwipeActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPic.setOnClickListener {
            startActivity(Intent(this, PicActivity::class.java))
        }

        btnTouchDelete.setOnClickListener {
            startActivity(Intent(this, SwipeActivity::class.java))
        }

        btnDrag.setOnClickListener {
            startActivity(Intent(this, DragActivity::class.java))
        }

        btnPic.setOnClickListener {
            startActivity(Intent(this, PicActivity::class.java))
        }
    }
}
