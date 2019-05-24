package com.cs.blacklist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

/**
 *
 * author : ChenSen
 * data : 2019/5/22
 * desc:
 */
class PhoneBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.apply {
            Log.e("tag", "PhoneStateReceiver  action  $action")
            Log.e("tag", "PhoneStateReceiver  getResultData  $resultData")


            // 去电，可以用定时挂断
            // 双卡的手机可能不走这个Action
            if (this.action == Intent.ACTION_NEW_OUTGOING_CALL) {

                val phoneNum = this.getStringExtra(Intent.EXTRA_PHONE_NUMBER)

                Log.e("tag", "呼出电话 $phoneNum")
            } else {

                // 来电去电都会走

                // 获取当前电话状态
                val state = getStringExtra(TelephonyManager.EXTRA_STATE)
                Log.e("tag", "PhoneStateReceiver onReceive state:  $state")


                // 获取电话号码
                val incomingNum = getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                Log.e("tag", "来电号码 $incomingNum")

                when (state) {

                    TelephonyManager.EXTRA_STATE_RINGING -> {

                        if (incomingNum.contains("555")) {
                            Toast.makeText(context, "拦截号码 $incomingNum", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}