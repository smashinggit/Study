package com.cmos.agerademo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.agera.BaseObservable;
import com.google.android.agera.Receiver;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * author : ChenSen
 * data : 2019/5/20
 * desc:
 */
public class LoadImageActivity extends Activity {

    Button btnStart;
    TextView tvState;
    ImageView ivImage;


    private static final String IMAGE_URL = "http://dingyue.ws.126.net/S8bZlsJBtFYOf0xrTJfjPrpIPVtjL5MawSVqYPkey3KCd1548725430403compressflag.jpg";
    private static final ExecutorService NETWORK_EXECUTOR = Executors.newSingleThreadExecutor();
    private static final ExecutorService COMPRESS_EXECUTOR = Executors.newSingleThreadExecutor();


    //网络请求
    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    Repository<Result<Bitmap>> repository = Repositories.repositoryWithInitialValue(Result.<Bitmap>absent())
            .observe()
            .onUpdatesPerLoop()
            .goTo(NETWORK_EXECUTOR)
            .attemptGetFrom(() -> {
                runOnUiThread(() -> tvState.setText("正在子线程进行图片下载..."));

                Request request = new Request.Builder()
                        .url(IMAGE_URL)
                        .get()
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Thread.sleep(1000);
                        return Result.present(response.body().byteStream());
                    } else {
                        return Result.failure(new Throwable("下载图片失败！" + response.code() + response.message()));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.failure(new Throwable("下载图片异常！" + e.getMessage()));
                }
            })
            .orEnd(Result::failure)
            .goTo(COMPRESS_EXECUTOR)
            .thenTransform(input -> {
                runOnUiThread(() -> tvState.setText("正在子线程进行图片压缩..."));

                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeStream(input, new Rect(0, 0, 0, 0), options);
                    Thread.sleep(1500);
                    return Result.present(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.failure(new Throwable("压缩图片异常！" + e.getMessage()));
                }
            })
            .compile();

    Updatable updatable = new Updatable() {
        @Override
        public void update() {
            repository.get()
                    .ifSucceededSendTo(value -> {
                        tvState.setText("加载图片完成");
                        ivImage.setImageBitmap(value);
                    })
                    .ifFailedSendTo(value -> tvState.setText(value.getMessage()));

            repository.removeUpdatable(updatable);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadimage);

        btnStart = findViewById(R.id.btnStart);
        tvState = findViewById(R.id.tvState);
        ivImage = findViewById(R.id.ivImage);

        btnStart.setOnClickListener(v -> repository.addUpdatable(updatable));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        repository.addUpdatable(updatable);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        repository.removeUpdatable(updatable);
    }
}
