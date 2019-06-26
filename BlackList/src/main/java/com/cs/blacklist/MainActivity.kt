package com.cs.blacklist

import android.app.Service
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //监听方式一
//        val manager: TelephonyManager = getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
//
//        manager.listen(object : PhoneStateListener() {
//
//
//            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
//                super.onCallStateChanged(state, phoneNumber)
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                    Log.e("tag", "是否有权 ${manager.hasCarrierPrivileges()}")
//                }
//
//
//                when (state) {
//                    TelephonyManager.CALL_STATE_RINGING -> {
//
//                        Log.e("tag", "呼入电话 $phoneNumber ")
//
//                    }
//                    TelephonyManager.CALL_STATE_IDLE -> {
//                        Log.e("tag", "空闲电话")
//                    }
//                    TelephonyManager.CALL_STATE_OFFHOOK -> {
//                        Log.e("tag", "挂断")
//                    }
//                }
//            }
//
//        }, PhoneStateListener.LISTEN_CALL_STATE)

    }
}
