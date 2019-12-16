package com.cs.app.thread

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import com.cs.app.R
import com.cs.library_architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_handlerthread.*

/**
 *
 * author : ChenSen
 * data : 2019/6/3
 * desc:
 *
 *
 * // 步骤1：创建HandlerThread实例对象
 * // 传入参数 = 线程名字，作用 = 标记该线程
 * HandlerThread mHandlerThread = new HandlerThread("handlerThread");
 *
 *
/  步骤2：启动线程
 * mHandlerThread.start();
 *
 * // 步骤3：创建工作线程Handler & 复写handleMessage（）
 * // 作用：关联HandlerThread的Looper对象、实现消息处理操作 & 与其他线程进行通信
 * // 注：消息处理操作（HandlerMessage（））的执行线程 = mHandlerThread所创建的工作线程中执行
 * Handler workHandler = new Handler( handlerThread.getLooper() ) {
 * @Override
 * public boolean handleMessage(Message msg) {
 * ...//消息处理
 * return true;
 * }
 * });
 *
 * // 步骤4：使用工作线程Handler向工作线程的消息队列发送消息
 * // 在工作线程中，当消息循环时取出对应消息 & 在工作线程执行相关操作
 * // a. 定义要发送的消息
 * Message msg = Message.obtain();
 * msg.what = 2; //消息的标识
 * msg.obj = "B"; // 消息的存放
 * // b. 通过Handler发送消息到其绑定的消息队列
 * workHandler.sendMessage(msg);
 *
 *
 * // 步骤5：结束线程，即停止线程的消息循环
 * mHandlerThread.quit();
 *
 */
class HandlerThreadActivity : BaseActivity() {

    private lateinit var mHandlerThread: HandlerThread
    private lateinit var mWorkHandler: Handler

    // 创建与主线程关联的Handler
    private val mMainHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handlerthread)


        // 步骤1：创建HandlerThread实例对象
        mHandlerThread = HandlerThread("workThread")

        // 步骤2：启动线程
        mHandlerThread.start()

        // 步骤3：创建工作线程Handler & 复写handleMessage（）
        // 作用：关联HandlerThread的Looper对象、实现消息处理操作 & 与其他线程进行通信
        // 注：消息处理操作（HandlerMessage（））的执行线程 = mHandlerThread所创建的工作线程中执行
        mWorkHandler = Handler(mHandlerThread.looper) {

            //这里是子线程
            when (it.what) {
                1 -> {
                    Thread.sleep(1000)
                    mMainHandler.post {
                        //这里是主线程
                        text1.text = "我爱学习"
                    }
                }
                2 -> {
                    Thread.sleep(3000)
                    mMainHandler.post {
                        //这里是主线程
                        text1.text = "我爱学习"
                    }
                }
            }
            true
        }



        button1.setOnClickListener {
            // 步骤4：使用工作线程Handler向工作线程的消息队列发送消息
            val msg = Message.obtain()
            msg.what = 1
            //通过Handler发送消息到其绑定的消息队列
            mWorkHandler.sendMessage(msg)
        }

        button2.setOnClickListener {
            // 步骤4：使用工作线程Handler向工作线程的消息队列发送消息
            val msg = Message.obtain()
            msg.what = 2
            //通过Handler发送消息到其绑定的消息队列
            mWorkHandler.sendMessage(msg)
        }

        button3.setOnClickListener {
            mHandlerThread.quit()
        }

    }
}