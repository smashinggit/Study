package com.cs.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videowaite)

        Log.d("tag","${resources.displayMetrics.widthPixels}  ${resources.displayMetrics.heightPixels}")
    }

}
