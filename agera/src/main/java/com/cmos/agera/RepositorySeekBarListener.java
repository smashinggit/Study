package com.cmos.agera;

import android.widget.SeekBar;

import com.google.android.agera.MutableRepository;

/**
 * author : ChenSen
 * data : 2018/9/6
 * desc:
 */
public class RepositorySeekBarListener implements SeekBar.OnSeekBarChangeListener {
    private MutableRepository<Integer> mutableRepository;


    public RepositorySeekBarListener(MutableRepository<Integer> mutableRepository) {
        this.mutableRepository = mutableRepository;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser)
            mutableRepository.accept(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
