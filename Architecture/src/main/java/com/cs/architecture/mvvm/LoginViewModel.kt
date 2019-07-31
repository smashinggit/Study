package com.cs.architecture.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * author : ChenSen
 * data : 2019/7/3
 * desc:
 */
class LoginViewModel : ViewModel() {

    val name = MutableLiveData<String>()
    val passwd = MutableLiveData<String>()
    val sex: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().also {
            it.value = 2
        }
    }

    val sexChekedId: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().also {


        }
    }


}