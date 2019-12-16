package com.cmos.agera;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Pair;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.agera.Function;
import com.google.android.agera.Merger;
import com.google.android.agera.MutableRepository;
import com.google.android.agera.Receiver;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.RepositoryConfig;
import com.google.android.agera.Result;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author : ChenSen
 * data : 2019/5/23
 * desc:
 */
public class CalculateActivity extends Activity {

    TextView tvNum1;  //数值1
    TextView tvNum2;  //数值2
    TextView tvResult; //计算结果
    TextView tvNumTip1; //SeekBar上的数值提示
    TextView tvNumTip2; //SeekBar上的数值提示
    TextView tvOperator; //操作符
    RadioGroup rgOperator;
    SeekBar sb1;
    SeekBar sb2;

    ExecutorService mExecutor = Executors.newSingleThreadExecutor(); //计算线程

    //1，定义MutableRepository，分别用于保存数值1，数值2，操作符和计算结果
    MutableRepository<Integer> mValue1Repo = Repositories.mutableRepository(0);
    MutableRepository<Integer> mValue2Repo = Repositories.mutableRepository(0);
    MutableRepository<String> mOperatorRepo = Repositories.mutableRepository("+");
    MutableRepository<String> mResultRepo = Repositories.mutableRepository("0");

    //2. 定义Repository，用于计算
    Repository<Result<Integer>> mTaskRepo = Repositories.repositoryWithInitialValue(Result.<Integer>absent())
            .observe(mValue1Repo, mValue2Repo, mOperatorRepo)// 4. 监听数值1，数值2和操作符，即当它们有变化时启动数据流重新计算
            .onUpdatesPerLoop()
            .goTo(mExecutor) //5. 切换到子线程
            .getFrom(new Supplier<Integer>() { //6，拿到数值1
                @NonNull
                @Override
                public Integer get() {
                    return mValue1Repo.get();
                }
            })
            .mergeIn(new Supplier<Integer>() { //7. 将数值1与数值2合并，返回一个Pair对象
                @NonNull
                @Override
                public Integer get() {
                    return mValue2Repo.get();
                }
            }, new Merger<Integer, Integer, Pair<Integer, Integer>>() {
                @NonNull
                @Override
                public Pair<Integer, Integer> merge(@NonNull Integer integer, @NonNull Integer tAdd) {
                    return new Pair<>(integer, tAdd);
                }
            })
            .thenTransform(new Function<Pair<Integer, Integer>, Result<Integer>>() {//8.进行计算操作，最后返回一个Result对象
                @NonNull
                @Override
                public Result<Integer> apply(@NonNull Pair<Integer, Integer> input) {
                    int result = 0;

                    switch (mOperatorRepo.get()) {
                        case "+":
                            result = input.first + input.second;
                            break;

                        case "-":
                            result = input.first - input.second;
                            break;

                        case "*":
                            result = input.first * input.second;
                            break;

                        case "/":   //9. 判断除数是否为0
                            if (input.second != 0) {
                                result = input.first / input.second;
                            } else {
                                return Result.failure(new Throwable("除数不能为0")); //返回失败的Result
                            }
                            break;
                    }
                    return Result.success(result); //返回成功的Result
                }
            })
            .onConcurrentUpdate(RepositoryConfig.CANCEL_FLOW)  //10，当正在执行时监听到更新，取消当前数据流
            .compile();//11，编译成 Repository


    //数值1的 Updatable
    Updatable mValue1Updatable = () -> {
        tvNum1.setText(mValue1Repo.get() + "");
        tvNumTip1.setText(mValue1Repo.get() + "");
    };

    //数值2的 Updatable
    Updatable mValue2Updatable = () -> {
        tvNum2.setText(mValue2Repo.get() + "");
        tvNumTip2.setText(mValue2Repo.get() + "");
    };

    //操作符的 Updatable
    Updatable mOperatorUpdatable = () -> {
        tvOperator.setText(mOperatorRepo.get());
    };

    //计算结果的 Updatable
    Updatable mResultUpdatable = () -> {
        tvResult.setText(mResultRepo.get() + "");
    };

    //计算任务的 Updatable
    Updatable mTaskUpdatable = () -> {

            mTaskRepo.get()//11，get方法得到的是一个Result对象
                .ifSucceededSendTo(new Receiver<Integer>() { //当Result成功时
                    @Override
                    public void accept(@NonNull Integer value) {
                        mResultRepo.accept(value + "");
                    }
                })
                .ifFailedSendTo(new Receiver<Throwable>() {//当Result失败时
                    @Override
                    public void accept(@NonNull Throwable value) {
                        mResultRepo.accept(value.getMessage());
                    }
                });
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal);

        tvNum1 = findViewById(R.id.tvNum1);
        tvNum2 = findViewById(R.id.tvNum2);
        tvResult = findViewById(R.id.tvResult);
        tvNumTip1 = findViewById(R.id.tvNumTip1);
        tvNumTip2 = findViewById(R.id.tvNumTip2);
        tvOperator = findViewById(R.id.tvOperator);
        rgOperator = findViewById(R.id.rgOperator);
        sb1 = findViewById(R.id.sb1);
        sb2 = findViewById(R.id.sb2);


        rgOperator.setOnCheckedChangeListener((group, checkedId) -> {

            switch (checkedId) {
                case R.id.rbAdd:
                    mOperatorRepo.accept("+");  //更新操作符仓库中的值
                    break;
                case R.id.rbSub:
                    mOperatorRepo.accept("-");
                    break;
                case R.id.rbMult:
                    mOperatorRepo.accept("*");
                    break;
                case R.id.rbDiv:
                    mOperatorRepo.accept("/");
                    break;
            }

        });

        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mValue1Repo.accept(progress); //更新数值1符仓库中的值
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mValue2Repo.accept(progress); //更新数值2仓库中的值
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册 Updatable
        mValue1Repo.addUpdatable(mValue1Updatable);
        mValue2Repo.addUpdatable(mValue2Updatable);
        mOperatorRepo.addUpdatable(mOperatorUpdatable);
        mResultRepo.addUpdatable(mResultUpdatable);
        mTaskRepo.addUpdatable(mTaskUpdatable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //移除 Updatable
        mValue1Repo.removeUpdatable(mValue1Updatable);
        mValue2Repo.removeUpdatable(mValue2Updatable);
        mOperatorRepo.removeUpdatable(mOperatorUpdatable);
        mResultRepo.removeUpdatable(mResultUpdatable);
        mTaskRepo.removeUpdatable(mTaskUpdatable);
    }
}
