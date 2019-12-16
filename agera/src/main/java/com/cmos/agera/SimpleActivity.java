package com.cmos.agera;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.agera.MutableRepository;
import com.google.android.agera.Repositories;
import com.google.android.agera.Updatable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * author : ChenSen
 * data : 2019/5/17
 * desc:
 */
public class SimpleActivity extends Activity {

    Button btnTest;
    TextView tvHello;

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);


    //1. 创建一个 Updatable (观察者)
    Updatable updatable = new Updatable() {
        @Override
        public void update() {
            //4. 从数据仓库中获取数据
            String currentTime = mutableRepository.get();
            tvHello.setText(currentTime);
        }
    };

    //1. 创建一个 MutableRepository (被观察者&& 数据提供者)
    MutableRepository<String> mutableRepository = Repositories.mutableRepository(getCurrentTime());


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);

        btnTest = findViewById(R.id.btnTest);
        tvHello = findViewById(R.id.tvHello);

        //3. 当点击按钮时，更新仓库 MutableRepository  中的值
        btnTest.setOnClickListener(v -> mutableRepository.accept(getCurrentTime()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //2. 为数据仓库 MutableRepository 添加一个观察者 Updatable
        mutableRepository.addUpdatable(updatable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //5. 移除观察者
        mutableRepository.removeUpdatable(updatable);
    }

    private String getCurrentTime() {
        return format.format(new Date());
    }
}
