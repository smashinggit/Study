package com.cs.bitmap

import android.content.Intent
import android.os.Bundle
import com.cs.library_architecture.base.BaseActivity
import com.cs.library_architecture.utils.ScreentUtils
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
 * author : ChenSen
 * data : 2019/5/30
 * desc:
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnBitmap.setOnClickListener { startActivity(Intent(this, BitmapActivity::class.java)) }
        btnGlide.setOnClickListener { startActivity(Intent(this, GlideActivity::class.java)) }
        btnSubsampling.setOnClickListener { startActivity(Intent(this, SubSamplingActivity::class.java)) }

        ScreentUtils.showScreenInfo(this)
    }


}