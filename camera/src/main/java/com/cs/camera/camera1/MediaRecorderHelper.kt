package com.cs.camera.camera1

import android.app.Activity
import android.hardware.Camera
import android.media.MediaRecorder
import android.util.Log
import android.view.Surface
import android.widget.Toast
import com.cs.camera.util.FileUtil

/**
 *
 * author : ChenSen
 * data : 2018/3/21
 * desc:
 */
class MediaRecorderHelper(var mContext: Activity, private var mCamera: Camera, private var rotation: Int, private var surface: Surface) {
    var mMediaRecorder: MediaRecorder? = null
    var isRunning = false
    private val filePath = FileUtil.createVideoFile()?.absolutePath

    fun startRecord() {
        try {
            mMediaRecorder = MediaRecorder()
            mMediaRecorder?.let {
                mCamera.unlock() //一定要调用
                it.reset()
                it.setCamera(mCamera)            //给Recorder设置Camera对象
                it.setOrientationHint(rotation)  //改变保存后的视频文件播放时是否横屏(不加这句，视频文件播放的时候角度是反的)
                it.setAudioSource(MediaRecorder.AudioSource.MIC)      //设置从麦克风采集声音
                it.setVideoSource(MediaRecorder.VideoSource.CAMERA)   //设置从摄像头采集图像
                it.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) //设置视频的输出格式为MP4
                it.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT) //设置音频的编码格式
                it.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT) // 设置视频的编码格式
                it.setVideoSize(3840, 2160)// 设置视频大小
                it.setVideoFrameRate(60)  // 设置帧率
                //it.setMaxDuration(10000) //设置最大录像时间为10s
                it.setPreviewDisplay(surface) //设置
                it.setOutputFile(filePath) //设置输出文件
                it.prepare()
                it.start()
                isRunning = true
                Log.d("tag", "开始录制视频")
                Log.d("tag", "视频保存路径：$filePath")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopRecord() {
        mMediaRecorder?.stop()
        isRunning = false
        Log.d("tag", "停止录制视频")
        Toast.makeText(mContext, "视频保存路径：$filePath", Toast.LENGTH_LONG).show()
    }


    fun release() {
        if (mMediaRecorder != null) {
            mMediaRecorder?.release()
            mMediaRecorder = null
            isRunning = false
        }
    }
}