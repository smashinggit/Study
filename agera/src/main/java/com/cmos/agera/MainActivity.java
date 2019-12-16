package com.cmos.agera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Button;

import com.google.android.agera.Function;
import com.google.android.agera.Merger;
import com.google.android.agera.Predicate;
import com.google.android.agera.Receiver;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.RepositoryConfig;
import com.google.android.agera.Result;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;


/**
 * author : ChenSen
 * data : 2019/5/17
 * desc:
 */
public class MainActivity extends Activity {

    Button btnSimple;
    Button btnCalculate;
    Button btnLoadImage;
    Button btnHttp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSimple = findViewById(R.id.btnSimple);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnLoadImage = findViewById(R.id.btnLoadImage);
        btnHttp = findViewById(R.id.btnHttp);


        btnSimple.setOnClickListener(v -> startActivity(new Intent(this, SimpleActivity.class)));
        btnCalculate.setOnClickListener(v -> startActivity(new Intent(this, CalculateActivity.class)));
        btnLoadImage.setOnClickListener(v -> startActivity(new Intent(this, LoadImageActivity.class)));
        btnHttp.setOnClickListener(v -> startActivity(new Intent(this, HttpActivity.class)));

    }

    //编译表达式
    Repository<String> repository =
            Repositories.repositoryWithInitialValue("init")  //1.声明RepositoryCompiler，初始值为"init"，返回REventSource实例
                    .observe()          //2. 指定事件源(Observable)，可以多个或者不指定，返回RFrequency实例
                    .onUpdatesPerLoop()  //3. 设置通知频率，返回RFlow实例
                    .getFrom(new Supplier<Double>() {  //4. 设置数据源(Supplier)，返回RFlow或RTermination实例
                        @NonNull
                        @Override
                        public Double get() {
                            //通过getFrom方法，获得一个数据值。当前值可以与声明时的初始值不同(数值和类型都可以不同)
                            //当前数据流的值为 5000000.00
                            return 5000000.00d;
                        }
                    })
                    .check(new Predicate<Double>() {
                        @Override
                        public boolean apply(@NonNull Double value) {
                            return false;
                        }
                    })
                    .orSkip()
                    .mergeIn(new Supplier<String>() { //5. 将当前数据流中的值 和 一个通过 Supplier 提供的新值进行合并，并返回一个值
                        @NonNull
                        @Override
                        public String get() {
                            //这里是新提供的一个值
                            return "祝大家的银行卡里余额为：";
                        }
                    }, new Merger<Double, String, String>() {
                        @NonNull
                        @Override
                        public String merge(@NonNull Double integer, @NonNull String tAdd) {
                            //这里将前数据流中的值和新值合并，并返回
                            //新值为 "祝大家的银行卡里余额为："
                            //前数据流中的值 为 5000000.00d
                            //合并后的值为 "祝大家的银行卡里余额为：5000000.00"
                            return tAdd + integer;
                        }
                    })
                    .sendTo(new Receiver<String>() { //6. 将当前数据流中的值复制一份发送到 Receiver 对象中
                        @Override
                        public void accept(@NonNull String value) {
                            Log.d("tag", value);
                        }
                    })
                    .thenTransform(new Function<String, String>() {//7. 将数据流中的值进行最后转换
                        @NonNull
                        @Override
                        public String apply(@NonNull String input) {
                            //注意，转换后的数据类型必须与初始化时的数据类型相同
                            return input + " 吼吼吼~";
                        }
                    })
                    .onDeactivation(RepositoryConfig.CANCEL_FLOW)
                    .compile();

    Updatable updatable = new Updatable() {
        @Override
        public void update() {
            Log.d("tag", repository.get());
        }
    };


    // Attempt && Result 讲解

    Repository repository2 = Repositories.repositoryWithInitialValue(0)
            .observe()
            .onUpdatesPerLoop()
            //1.我们使用thenAttemptGetFrom方法，去处理一个可能发生异常的操作
            .thenAttemptGetFrom(new Supplier<Result<? extends Integer>>() {
                @NonNull
                @Override
                public Result<? extends Integer> get() {
                    try {
                        int num = 1 / 0;
                        //2. 如果不发生异常，返回一个表示成功的Result
                        return Result.success(num);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //3.如果发生了异常，返回一个表示失败的Result
                        return Result.failure(e);
                    }
                }
            })
            //4.对异常情况进行处理
            .orEnd(new Function<Throwable, Integer>() {
                @NonNull
                @Override
                public Integer apply(@NonNull Throwable input) {
                    //5. 当发生异常的时候，返回一个-1
                    return -1;
                }
            })
            .compile();

    Updatable updatable2 = new Updatable() {
        @Override
        public void update() {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        repository.addUpdatable(updatable);
        repository2.addUpdatable(updatable2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        repository.removeUpdatable(updatable);
        repository2.removeUpdatable(updatable2);
    }

}
