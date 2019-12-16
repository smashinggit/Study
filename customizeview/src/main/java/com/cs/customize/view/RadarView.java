package com.cs.customize.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/12/22.
 */

public class RadarView extends View {

    Paint mPaint;
    int mCenterX;
    int mCenterY;
    int mWidth;
    int mHeight;

    Paint mTitlePaint;
    Paint mValuePaint;
    int mCount = 6;//数据个数
    float mRadius;//最大半径
    float mAngle;//最大半径

    ArrayList<String> mTitles = new ArrayList<>();//标题
    ArrayList<Double> mData = new ArrayList<>();//分值
    float mMaxValue = 100;    //数据最大值

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);

        mTitlePaint = new Paint();
        mTitlePaint.setStyle(Paint.Style.FILL);
        mTitlePaint.setColor(Color.BLACK);
        mTitlePaint.setStrokeWidth(2);
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setTextAlign(Paint.Align.CENTER);
        mTitlePaint.setTextSize(40);

        mValuePaint = new Paint();
        mValuePaint.setColor(Color.RED);
        mValuePaint.setAntiAlias(true);
        mTitlePaint.setStrokeWidth(10);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCenterX = w / 2;
        mCenterY = h / 2;
        mRadius = Math.min(w, h) / 2 * 0.7f;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPoint(mCenterX, mCenterY, mPaint);
        drawPolygon(canvas);
        drawLines(canvas);
        drawTitle(canvas);
        drawRegion(canvas);
    }


    /**
     * 绘制多边形
     *
     * @param canvas
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        //中心与相邻两个内角相连的夹角角度
        mAngle = (float) (2 * Math.PI / mCount);
        //每个蛛丝之间的间距
        float r = mRadius / (mCount - 1);
        for (int i = 0; i < mCount; i++) {
            //当前半径
            float curR = r * i;
            path.reset();

            for (int j = 0; j < mCount; j++) {
                if (j == 0) {
                    path.moveTo(mCenterX + curR, mCenterY);
                } else {
                    //对于直角三角形sin(x)是对边比斜边，cos(x)是底边比斜边，tan(x)是对边比底边
                    //因此可以推导出:底边(x坐标)=斜边(半径)*cos(夹角角度)
                    //               对边(y坐标)=斜边(半径)*sin(夹角角度)
                    float x = (float) (mCenterX + curR * Math.cos(mAngle * j));
                    float y = (float) (mCenterY + curR * Math.sin(mAngle * j));
                    path.lineTo(x, y);
                }
            }
            path.close();
            canvas.drawPath(path, mPaint);
        }

    }

    /**
     * 绘制直线
     *
     * @param canvas
     */
    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < mCount; i++) {
            path.reset();
            path.moveTo(mCenterX, mCenterY);
            float x = (float) (mCenterX + mRadius * Math.cos(mAngle * i));
            float y = (float) (mCenterY + mRadius * Math.sin(mAngle * i));
            path.lineTo(x, y);
            canvas.drawPath(path, mPaint);
        }
    }

    /**
     * 绘制标题
     *
     * @param canvas
     */
    private void drawTitle(Canvas canvas) {
        if (mTitles.size() == 0) return;

        //相关知识点:http://mikewang.blog.51cto.com/3826268/871765/
        Paint.FontMetrics fontMetrics = mTitlePaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        //绘制文字时不让文字和雷达图形交叉,加大绘制半径
        float textRadius = mRadius + fontHeight;
        double pi = Math.PI;

        for (int i = 0; i < mCount; i++) {
            //当前绘制标题所在顶点角度
            float degrees = mAngle * i;
            float x = (float) (mCenterX + textRadius * Math.cos(degrees));
            float y = (float) (mCenterY + textRadius * Math.sin(degrees));
            // canvas.drawPoint(x,y,mPaint);

            float dis = mTitlePaint.measureText(mTitles.get(i)) / (mTitles.get(i).length());
            //从右下角开始顺时针画起,与真实坐标系相反
            if (degrees >= 0 && degrees < pi / 2) {//第四象限
                canvas.drawText(mTitles.get(i), x + dis, y, mTitlePaint);
            } else if (degrees >= (pi / 2) && degrees < pi) {//第三象限
                canvas.drawText(mTitles.get(i), x - dis, y, mTitlePaint);
            } else if (degrees >= pi && degrees < 3 * pi / 2) {//第二象限
                canvas.drawText(mTitles.get(i), x - dis, y, mTitlePaint);
            } else if (degrees >= 3 * pi / 2 && degrees <= 2 * pi) {//第一象限
                canvas.drawText(mTitles.get(i), x, y, mTitlePaint);
            }
        }
    }

    /**
     * 绘制覆盖区域
     *
     * @param canvas
     */
    private void drawRegion(Canvas canvas) {
        if (mData.size() == 0) return;

        mValuePaint.setAlpha(255);
        Path path = new Path();
        for (int i = 0; i < mCount; i++) {
            //计算该数值与最大值比例
            double perCentr = mData.get(i) / mMaxValue;
            //小圆点所在位置距离圆心的距离
            double perRadius = perCentr * mRadius;
            float x = (float) (mCenterX + perRadius * Math.cos(mAngle * i));
            float y = (float) (mCenterY + perRadius * Math.sin(mAngle * i));
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            //绘制小圆点
            canvas.drawCircle(x, y, 10, mValuePaint);
        }
        path.close();
        mValuePaint.setStyle(Paint.Style.STROKE);
        //绘制覆盖区域外的连线
        canvas.drawPath(path, mValuePaint);
        //填充覆盖区域
        mValuePaint.setAlpha(128);
        mValuePaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, mValuePaint);
    }

    public void setTitles(ArrayList<String> mTitles) {
        this.mTitles = mTitles;
        invalidate();
    }


    public void setData(ArrayList<Double> data) {
        this.mData = data;
        invalidate();
    }

    public void setMaxValue(float maxValue) {
        this.mMaxValue = mMaxValue;
        invalidate();
    }
}
