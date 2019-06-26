package com.cs.architecture.jetpack.livedata

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cs.architecture.R
import kotlinx.android.synthetic.main.activity_livedata.*

/**
 *
 * author : ChenSen
 * data : 2019/6/24
 * desc:
 */
class LiveDataActivity : FragmentActivity() {

    lateinit var nameViewModel: NameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livedata)

        // Get the ViewModel.
        nameViewModel = ViewModelProviders.of(this).get(NameViewModel::class.java)

        // Create the observer which updates the UI.
        // Observe the LiveData, passing in this activity as the LifecycleOwner
        // and the observer.
        nameViewModel.name.observe(this, Observer {
            // Update the UI, in this case, a TextView.
            tvInfo.text = it
        })


        //change data
        btChange.setOnClickListener {
            val name = nameViewModel.name.value?.split("~")?.get(0)
            nameViewModel.name.value = name + "~" + System.currentTimeMillis()
        }
    }
}