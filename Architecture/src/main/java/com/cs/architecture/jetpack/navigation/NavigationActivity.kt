package com.cs.architecture.jetpack.navigation

import android.os.Bundle
import androidx.navigation.findNavController
import com.cs.architecture.R
import com.cs.common.base.BaseActivity

/**
 *
 * author : ChenSen
 * data : 2019/7/31
 * desc:
 */
class NavigationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
    }
}