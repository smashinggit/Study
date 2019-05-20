package com.cmos.agerademo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;


/**
 * author : ChenSen
 * data : 2019/5/17
 * desc:
 */
public class MainActivity extends Activity {

    Button btnSimple;
    Button btnLoadImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSimple = findViewById(R.id.btnSimple);
        btnLoadImage = findViewById(R.id.btnLoadImage);


        btnSimple.setOnClickListener(v -> startActivity(new Intent(this, SimpleActivity.class)));
        btnLoadImage.setOnClickListener(v -> startActivity(new Intent(this, LoadImageActivity.class)));

    }


}
