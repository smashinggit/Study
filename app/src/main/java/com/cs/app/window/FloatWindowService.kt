package com.cs.app.window

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Message
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * author : ChenSen
 * data : 2019/6/6
 * desc:
 */
class FloatWindowService : Service() {

    companion object {
        const val MSG_CREATE = 0
        const val MSG_UPDATE = 1
        const val MSG_CANCEL = 2

    }

    private val mHandler = Handler {
        handleMessage(it)
        false
    }

    private val mTimer = Timer()


    override fun onCreate() {
        super.onCreate()
        FloatWindowManager.createWindowView(this@FloatWindowService)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // 开启定时器，每隔0.5秒刷新一次
//        mTimer.scheduleAtFixedRate(MyTask(), 0, 500)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun handleMessage(it: Message?) {

        it?.apply {
            when (what) {
                MSG_CREATE -> {
                    FloatWindowManager.createWindowView(this@FloatWindowService)
                }
                MSG_UPDATE -> {

                }
                MSG_CANCEL -> {
                    FloatWindowManager.removeSmallWindow(this@FloatWindowService)
                }
            }
        }
    }


    override fun onDestroy() {
        mHandler.sendEmptyMessage(MSG_CANCEL)
        mTimer.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?) = null


    inner class MyTask : TimerTask() {
        override fun run() {
            // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
//            if (!FloatWindowManager.isShowing && isHome()) {

            if (!FloatWindowManager.isShowing) {
                mHandler.sendEmptyMessage(MSG_CREATE)
            } else if (FloatWindowManager.isShowing) {
                mHandler.sendEmptyMessage(MSG_UPDATE)
            }
        }
    }


    /**
     * 判断当前界面是否是桌面
     */
    fun isHome(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        //TODO 注意，getRunningTasks 在5.0之后只能获取到桌面程序和本身的信息，其他的应用信息无法获取
        val tasks = activityManager.getRunningTasks(1)
        val name = tasks[0].topActivity?.packageName

//        log("当前运行的程序包名 $name")
        return getHomes().contains(name)
    }


    /**
     * 获得桌面程序的包名
     */
    private fun getHomes(): List<String> {

        val names = ArrayList<String>()

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)

        val activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

        //属于桌面的应用:com.android.launcher(启动器)
        for (i in 0 until activities.size) {
            names.add(activities[i].activityInfo.packageName)
//            log("桌面应用程序的包名 ${names[i]}")
        }
        return names
    }
}