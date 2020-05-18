package com.cs.bitmap

import android.os.Bundle
import com.cs.library_architecture.base.BaseActivity
import com.davemorrissey.labs.subscaleview.ImageSource
import kotlinx.android.synthetic.main.activity_subsampling.*

/**
 *
 * author : ChenSen
 * data : 2019/5/30
 * desc:  使用加载超大图的库
 *
 */
class SubSamplingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subsampling)

//        ivSubSamplingView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP) //设置缩放类型
//        ivSubSamplingView.setDoubleTapZoomScale(1f)
        ivSubSamplingView.setImage(ImageSource.asset("qmsht.jpg"))


        //设置预览图
        //注意，目标图的大小必须先声明
//        ivSubSamplingView.setImage(
//                ImageSource.asset("qmsht.jpg").dimensions(30000, 926),
//                ImageSource.asset("pic2.jgp"))

    }


}