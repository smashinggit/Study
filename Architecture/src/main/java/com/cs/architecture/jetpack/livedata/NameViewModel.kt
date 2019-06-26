package com.cs.architecture.jetpack.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * author : ChenSen
 * data : 2019/6/24
 * desc:
 * LiveData is a wrapper that can be used with any data,
 * including objects that implement Collections, such as List.
 * A LiveData object is usually stored within a ViewModel object and
 * is accessed via a getter method
 *
 */

class NameViewModel : ViewModel() {

    // Create a LiveData with a String
    val name = MutableLiveData<String>()
            .apply {
                value = "chensen"
            }


    // Rest of the ViewModel...


}