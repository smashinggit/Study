package com.cs.httptest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.cs.httptest.http.HttpHelper
import okhttp3.Request
import okhttp3.internal.http.HttpHeaders

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }
}
