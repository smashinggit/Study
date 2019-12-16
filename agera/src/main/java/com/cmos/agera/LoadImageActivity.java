package com.cmos.agera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.agera.Function;
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

    //下载图片的地址(注，图片来自网络，仅供学习使用)
    private static final String IMAGE_URL = "http://dingyue.ws.126.net/S8bZlsJBtFYOf0xrTJfjPrpIPVtjL5MawSVqYPkey3KCd1548725430403compressflag.jpg";
    private static final ExecutorService NETWORK_EXECUTOR = Executors.newSingleThreadExecutor();//下载图片的线程
    private static final ExecutorService COMPRESS_EXECUTOR = Executors.newSingleThreadExecutor();//压缩图片的线程


    //网络请求客户端
    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    //1.定义一个Repository，数值的类型是Result<Bitmap>,因为从网络下载图片可能会失败，所以用Result对象封装
    Repository<Result<Bitmap>> repository = Repositories.repositoryWithInitialValue(Result.<Bitmap>absent())
            .observe()  //2. 这里我们不需要监听其他事件源
            .onUpdatesPerLoop()
            .goTo(NETWORK_EXECUTOR)//3.切换到现在图片的子线程
            .attemptGetFrom(() -> {
                runOnUiThread(() -> tvState.setText("正在子线程进行图片下载...")); //4. 在主线程更新提示语

                //5.进行网络请求
                Request request = new Request.Builder()
                        .url(IMAGE_URL)
                        .get()
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Thread.sleep(1000);//此处故意休眠1s，用于模拟网络延迟
                        return Result.success(response.body().byteStream()); //6.当网络请求成功时，返回一个成功的Result
                    } else {
                        //7. 网络请求失败时，返回一个失败的Result
                        return Result.failure(new Throwable("下载图片失败！" + response.code() + response.message()));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //8. 网络请求异常时，返回一个失败的Result
                    return Result.failure(new Throwable("下载图片异常！" + e.getMessage()));
                }
            })
            .orEnd(new Function<Throwable, Result<Bitmap>>() { //9.当上一步的指令执行失败时，走这个方法
                @NonNull
                @Override
                public Result<Bitmap> apply(@NonNull Throwable input) {
                    /**
                     * 10,输入值是上一步失败的原因，输出值我们根据具体需求返回
                     *比如，这里我们也可以返回一个默认的图片
                     * return  Result.success(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
                     *
                     */
                    return Result.failure(input); //把上一步的失败原因封装成一个失败的Result对象，返回(数据流结束)
                }
            })
            .goTo(COMPRESS_EXECUTOR)//11. 切换到压缩图片的线程
            .thenTransform(input -> { //12. 输入值是一个 InputStream对象，这里我用了lambda表达式减少代码量
                runOnUiThread(() -> tvState.setText("正在子线程进行图片压缩..."));

                //13. 压缩图片
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeStream(input, new Rect(0, 0, 0, 0), options);
                    Thread.sleep(1500); //此处故意休眠，用于模拟网络延迟
                    return Result.present(bitmap); //14. 压缩图片成功，返回Result对象
                } catch (Exception e) {
                    e.printStackTrace();
                    //15.压缩图片异常，返一个失败的Result对象
                    return Result.failure(new Throwable("压缩图片异常！" + e.getMessage()));
                }
            })
            .compile();

    Updatable updatable = new Updatable() {
        @Override
        public void update() {
            repository.get()
                    .ifSucceededSendTo(value -> { //17.当Repository中的Result是成功时
                        tvState.setText("加载图片完成");
                        ivImage.setImageBitmap(value);
                    })
                    .ifFailedSendTo(value -> tvState.setText(value.getMessage()));//18.当Repository中的Result是失败时

            repository.removeUpdatable(updatable);//21，接收到更新后，注销Updatable
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadimage);

        btnStart = findViewById(R.id.btnStart);
        tvState = findViewById(R.id.tvState);
        ivImage = findViewById(R.id.ivImage);

        btnStart.setOnClickListener(v -> {
            ivImage.setImageBitmap(null);
            repository.addUpdatable(updatable); //20，注册Updatable ，启动数据流
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        repository.addUpdatable(updatable);//22,不在此处注册
    }

    @Override
    protected void onPause() {
        super.onPause();
//        repository.removeUpdatable(updatable);//22，不在此处注销
    }
}
