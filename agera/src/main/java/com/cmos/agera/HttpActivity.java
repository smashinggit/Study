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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmos.agera.http.Api;
import com.cmos.agera.http.Http;
import com.google.android.agera.Function;
import com.google.android.agera.Merger;
import com.google.android.agera.MutableRepository;
import com.google.android.agera.Receiver;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.RepositoryConfig;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;

import okhttp3.ResponseBody;

/**
 * author : ChenSen
 * data : 2019/5/24
 * desc:
 */
public class HttpActivity extends Activity {

    Button btnStart;
    TextView tvState;
    ImageView ivImage;
    TextView tvProjectTree;
    TextView tvProjectList;

    //1. 定义一个 MutableRepository ,当点击按钮时，改变其值，监听它的 Repository 就会重新执行
    MutableRepository<Long> mutableRepository = Repositories.mutableRepository(System.currentTimeMillis());

    //2. 定义一个 Repository 用于执行3个任务
    Repository<Result<JSONObject>> repository = Repositories.repositoryWithInitialValue(Result.<JSONObject>absent())
            .observe(mutableRepository) //3. 监听mutableRepository
            .onUpdatesPerLoop()
            .goTo(Http.THREAD_POOL) //4. 切换到子线程执行
            .attemptGetFrom(Http.createService(Api.class).getAgeraPic(Http.IMAGE_URL)) //5.从接口中拿到数据
            .orEnd(new Function<Throwable, Result<JSONObject>>() {
                @NonNull
                @Override
                public Result<JSONObject> apply(@NonNull Throwable input) {
                    return Result.failure(input);//6. 当上一步骤(即网络请求)发送异常时，返回错误信息
                }
            })
            .sendTo(new Receiver<ResponseBody>() {//7. 将拿到的数据进行处理
                @Override
                public void accept(@NonNull ResponseBody value) {
                    try {
                        Thread.sleep(1500);  //模拟网络延迟，便于观察
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //8. 压缩图片
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeStream(value.byteStream(), new Rect(0, 0, 0, 0), options);
                    runOnUiThread(() -> { //9. 在主线程中更新UI
                        ivImage.setImageBitmap(bitmap);
                        tvState.setText("正在请求项目列表...");
                    });
                }
            })
            .attemptGetFrom(Http.createService(Api.class).getProjectTree())//10,请求项目列表接口
            .orEnd(new Function<Throwable, Result<JSONObject>>() {
                @NonNull
                @Override
                public Result<JSONObject> apply(@NonNull Throwable input) {
                    return Result.failure(); //11. 当上一步骤(即网络请求)发送异常时，返回错误信息
                }
            })
            .sendTo(new Receiver<JSONObject>() { //12. 将拿到的数据进行更新UI
                @Override
                public void accept(@NonNull JSONObject value) {
                    try {
                        Thread.sleep(1500);  //模拟网络延迟，便于观察
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(() -> {  //13. 在主线程中更新UI
                        tvProjectTree.setText(value.toJSONString());
                        tvState.setText("正在请求项目详情...");
                    });
                }
            })
            .attemptTransform(new Function<JSONObject, Result<JSONObject>>() { //13.将拿到的数据进行下一次接口请求
                @NonNull
                @Override
                public Result<JSONObject> apply(@NonNull JSONObject input) {

                    if ("0".equals(input.getString("errorCode"))) {
                        JSONArray objects = input.getJSONArray("data");
                        JSONObject object0 = objects.getJSONObject(0);

                        // 14. 接口获取成功，拿到其中的id字段，进行下一个接口请求
                        return Http.createService(Api.class).getProjectList(object0.getInteger("id")).get();
                    } else {
                        //15. 接口获取数据失败
                        return Result.failure(new Throwable("获取项目分类列表失败!" + input.getString("errorCode")));
                    }
                }
            })
            .orEnd(new Function<Throwable, Result<JSONObject>>() {
                @NonNull
                @Override
                public Result<JSONObject> apply(@NonNull Throwable input) {
                    return Result.failure(input); //16. 上一步操作发生失败时的处理
                }
            })
            .thenTransform(new Function<JSONObject, Result<JSONObject>>() {//17.对于数据流的最后一步处理
                @NonNull
                @Override
                public Result<JSONObject> apply(@NonNull JSONObject input) {
                    if ("0".equals(input.getString("errorCode"))) {
                        JSONArray objects = input.getJSONObject("data").getJSONArray("datas");
                        JSONObject object0 = objects.getJSONObject(0);
                        return Result.success(object0); //18. 请求成功，返回项目详情
                    } else {
                        //19. 请求失败的处理
                        return Result.failure(new Throwable("获取项目详情失败!" + input.getString("errorCode")));
                    }
                }
            })
            .onConcurrentUpdate(RepositoryConfig.CANCEL_FLOW)//20.设定等数据流正在执行时收到更新请求时，取消当前数据流执行
            .notifyIf(new Merger<Result<JSONObject>, Result<JSONObject>, Boolean>() {
                @NonNull
                @Override
                public Boolean merge(@NonNull Result<JSONObject> jsonObjectResult, @NonNull Result<JSONObject> jsonObjectResult2) {
                    return true; //21，设定永远通知 Updatable 更新
                }
            })
            .compile();

    Updatable updatable = new Updatable() {
        @Override
        public void update() {
            repository.get()
                    .ifSucceededSendTo(new Receiver<JSONObject>() {//22.收到成功的结果，更新UI
                        @Override
                        public void accept(@NonNull JSONObject value) {

                            try {
                                Thread.sleep(1000);  //模拟网络延迟，便于观察
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            tvState.setText("任务执行完毕");
                            tvProjectList.setText(value.toJSONString());
                        }
                    })
                    .ifFailedSendTo(new Receiver<Throwable>() {//23，收到失败的结果，更新UI
                        @Override
                        public void accept(@NonNull Throwable value) {
                            tvState.setText(value.getMessage());
                        }
                    });
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http);
        ivImage = findViewById(R.id.ivImage);

        btnStart = findViewById(R.id.btnStart);
        tvState = findViewById(R.id.tvState);
        ivImage = findViewById(R.id.ivImage);
        tvProjectTree = findViewById(R.id.tvProjectTree);
        tvProjectList = findViewById(R.id.tvProjectList);

        btnStart.setOnClickListener(v -> {

            mutableRepository.accept(System.currentTimeMillis()); //24，当我们点击按钮时，改变mutableRepository中的值

            tvState.setText("正在下载图片...");
            ivImage.setImageBitmap(null);
            tvProjectTree.setText("");
            tvProjectList.setText("");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        repository.addUpdatable(updatable); //25.注册 updatable
        tvState.setText("正在下载图片...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        repository.removeUpdatable(updatable); //26. 注销updatable
    }


    /**
     * *****************************简化代码*****************************************
     */
    Repository<Result<JSONObject>> repository2 = Repositories.repositoryWithInitialValue(Result.<JSONObject>absent())
            .observe(mutableRepository)
            .onUpdatesPerLoop()
            .goTo(Http.THREAD_POOL)
            .attemptGetFrom(Http.createService(Api.class).getAgeraPic(Http.IMAGE_URL))
            .orEnd(Result::failure)
            .sendTo(value -> {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeStream(value.byteStream(), new Rect(0, 0, 0, 0), options);
                runOnUiThread(() -> {
                    ivImage.setImageBitmap(bitmap);
                    tvState.setText("正在请求项目列表...");
                });
            })
            .attemptGetFrom(Http.createService(Api.class).getProjectTree())
            .orEnd(Result::failure)
            .sendTo(value ->
                    runOnUiThread(() -> {
                        tvProjectTree.setText(value.toJSONString());
                        tvState.setText("正在请求项目详情...");
                    })
            )
            .attemptTransform(input -> {

                if ("0".equals(input.getString("errorCode"))) {
                    JSONArray objects = input.getJSONArray("data");
                    JSONObject object0 = objects.getJSONObject(0);

                    return Http.createService(Api.class).getProjectList(object0.getInteger("id")).get();
                } else {
                    return Result.failure(new Throwable("获取项目分类列表失败!" + input.getString("errorCode")));
                }
            })
            .orEnd(Result::failure)
            .thenTransform(input -> {
                if ("0".equals(input.getString("errorCode"))) {
                    JSONArray objects = input.getJSONObject("data").getJSONArray("datas");
                    JSONObject object0 = objects.getJSONObject(0);
                    return Result.success(object0);
                } else {
                    return Result.failure(new Throwable("获取项目详情失败!" + input.getString("errorCode")));
                }
            })
            .onConcurrentUpdate(RepositoryConfig.CANCEL_FLOW)
            .notifyIf((newValue, oldValue) -> true)
            .compile();

    Updatable updatable2 = () -> {
        repository2.get()
                .ifSucceededSendTo(value -> {
                    tvState.setText("任务执行完毕");
                    tvProjectList.setText(value.toJSONString());
                })
                .ifFailedSendTo(value -> tvState.setText(value.getMessage()));
    };

}
