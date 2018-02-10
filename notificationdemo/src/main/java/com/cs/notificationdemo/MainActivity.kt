package com.cs.notificationdemo

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var manager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        btn1.setOnClickListener { sendNormalNotification() }
        btn2.setOnClickListener { sendNormalNotificationWithAction() }
        btn3.setOnClickListener { updateNotification() }
        btn4.setOnClickListener { NotificationWithVibrate() }
        btn5.setOnClickListener { definNotification() }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when {
            intent?.getStringExtra("action") == "play" -> Log.d("tag", "收到指令 play")
            intent?.getStringExtra("action") == "next" -> Log.d("tag", "收到指令 next")
            else -> Log.d("tag", "没有收到指令")
        }
    }

    //自定义通知
    private fun definNotification() {
        var remoteView = RemoteViews(packageName, R.layout.notification_layout)

        //设置按钮1的点击事件
        var intentPlay = Intent(this, MainActivity::class.java)
        intentPlay.putExtra("action", "play")
        var clickPendingIntent = PendingIntent.getActivity(this, 10, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteView.setOnClickPendingIntent(R.id.btnPlay, clickPendingIntent)

        //设置按钮2的点击事件
        var intentNext = Intent(this, MainActivity::class.java)
        intentNext.putExtra("action", "next")
        var nextPendingIntent = PendingIntent.getActivity(this, 11, intentNext, PendingIntent.FLAG_UPDATE_CURRENT)
        remoteView.setOnClickPendingIntent(R.id.btnNext, nextPendingIntent)

        var builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContent(remoteView)
        manager.notify(6, builder.build())
    }


    //普通通知
    fun sendNormalNotification() {
        var builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("这里是标题")
                .setContentText("只有小图标、标题、内容 的通知")

        manager.notify(1, builder.build())
    }

    //带有跳转功能
    private fun sendNormalNotificationWithAction() {
        var intent = Intent(this, MainActivity::class.java)
        var pendingInten = PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        var builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("带Action的通知")
                .setContentText("点我会打开Mainactivity")
                .setAutoCancel(true)
                .setContentIntent(pendingInten)
        manager.notify(4, builder.build())
    }

    //更新通知
    private fun updateNotification() {
        var intent = Intent(this, MainActivity::class.java)
        var pendingInten = PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        var builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("带Action的通知（被更新了）")
                .setContentText("点我会打开Mainactivity（被更新了")
                .setAutoCancel(true)
                .setContentIntent(pendingInten)
        manager.notify(4, builder.build())
    }

    //带震动效果
    private fun NotificationWithVibrate() {
        var intent = Intent(this, MainActivity::class.java)
        var pendingInten = PendingIntent.getActivity(this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        var builder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("带震动的通知")
                .setContentText("点我会打开Mainactivity")
                .setAutoCancel(true)
                .setContentIntent(pendingInten)
        var notification = builder.build()
        notification.defaults = Notification.DEFAULT_ALL
        manager.notify(5, notification)

    }

}
