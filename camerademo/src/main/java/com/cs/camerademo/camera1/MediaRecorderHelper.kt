package com.cs.camerademo.camera1

import android.hardware.Camera
import android.media.MediaRecorder
import android.util.Log
import com.cs.camerademo.util.FileUtil

/**
 *
 * author : ChenSen
 * data : 2018/3/21
 * desc:
 */
class MediaRecorderHelper(private var mCamera: Camera) {
    var mMediaRecorder: MediaRecorder? = null

    fun startRecord() {
        mMediaRecorder = MediaRecorder()
        mMediaRecorder?.let {
            var filePath = FileUtil.createVideoFile()?.absolutePath
            mCamera.unlock() //一定要调用
            it.reset()
            it.setCamera(mCamera)
            it.setAudioSource(MediaRecorder.AudioSource.MIC)
            it.setVideoSource(MediaRecorder.VideoSource.CAMERA)
            it.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            it.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            it.setOutputFile(filePath)
            it.prepare()
            it.start()
            Log.d("tag", "开始录制视频")
            Log.d("tag", "视频保存路径：${filePath}")
        }
    }


    fun stopRecord() {
        mMediaRecorder?.stop()
        Log.d("tag", "停止录制视频")
    }


    fun release() {
        if (mMediaRecorder != null) {
            mMediaRecorder?.stop()
            mMediaRecorder?.release()
            mMediaRecorder = null
        }
    }
}