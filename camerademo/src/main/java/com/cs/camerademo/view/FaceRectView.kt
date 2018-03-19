package com.cs.camerademo.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.hardware.Camera
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View

/**
 * author :  chensen
 * data  :  2018/3/19
 * desc :
 */
class FaceRectView : View {
    lateinit var mPaint: Paint
    private var mCorlor = "#42ed45"
    var mFaces: Array<Camera.Face>? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mPaint = Paint()
        mPaint.color = Color.parseColor(mCorlor)
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, context.resources.displayMetrics)
        mPaint.isAntiAlias = true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mFaces?.let {
            for (face in it) {
                var rawRect = face.rect
                var adjustRect = Rect(width-rawRect.top,height- rawRect.left, width-rawRect.bottom, rawRect.right)
                canvas.drawRect(face.rect, mPaint)
               // canvas.drawRect(adjustRect, mPaint)
                Log.d("tag", "${rawRect.left}  ${rawRect.top}  ${rawRect.right}  ${rawRect.bottom}   左眼 ${face.leftEye}  右眼 ${face.rightEye} 嘴巴${face.mouth}  可信度 ${face.score} ")
                Log.d("tag", "${adjustRect.left}  ${adjustRect.top}  ${adjustRect.right}  ${adjustRect.bottom}   ")
            }
        }
    }

    fun setFaces(faces: Array<Camera.Face>) {
        this.mFaces = faces
        invalidate()
    }
}