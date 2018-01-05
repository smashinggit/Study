package com.cs.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_videowaite)

        var list = listOf("aa", "bb", "cca")

//        list.takeIf {
//            it.contains("aa")
//        }

        for (i in list) {
            print(i)
        }


    }


}
