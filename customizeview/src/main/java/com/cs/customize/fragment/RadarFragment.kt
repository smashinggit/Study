package com.cs.customize.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.cs.customize.R
import kotlinx.android.synthetic.main.fragment_rader.*
import java.util.ArrayList

/**
 * Created by Lenovo on 2017/12/22.
 */
class RadarFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_rader, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    fun init() {

        var mTitles = ArrayList<String>()
        mTitles.add("生存")
        mTitles.add("团战")
        mTitles.add("发育")
        mTitles.add("输出")
        mTitles.add("推进")
        mTitles.add("战绩(KDA)")

        var mData = ArrayList<Double>()
        mData.add(60.0)
        mData.add(100.0)
        mData.add(45.0)
        mData.add(85.0)
        mData.add(99.0)
        mData.add(66.0)

        radarview.setTitles(mTitles)
        radarview.setData(mData)


        seek1.progress = mData[0].toInt()
        seek2.progress = mData[1].toInt()
        seek3.progress = mData[2].toInt()
        seek4.progress = mData[3].toInt()
        seek5.progress = mData[4].toInt()
        seek6.progress = mData[5].toInt()

        seek1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mData[0] = progress.toDouble()
                radarview.setData(mData)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        seek2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mData[1] = progress.toDouble()
                radarview.setData(mData)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        seek3.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mData[2] = progress.toDouble()
                radarview.setData(mData)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        seek4.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mData[3] = progress.toDouble()
                radarview.setData(mData)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        seek5.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mData[4] = progress.toDouble()
                radarview.setData(mData)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        seek6.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mData[5] = progress.toDouble()
                radarview.setData(mData)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })


    }


}