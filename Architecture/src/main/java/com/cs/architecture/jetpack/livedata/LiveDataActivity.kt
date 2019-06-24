package com.cs.architecture.jetpack.livedata

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.cs.architecture.R

/**
 *
 * author : ChenSen
 * data : 2019/6/24
 * desc:
 */
class LiveDataActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livedata)

        // Get the ViewModel.
        ViewModelProviders.of(this)
    }
}