package com.cmos.agerademo;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.agera.Function;
import com.google.android.agera.MutableRepository;
import com.google.android.agera.Observable;
import com.google.android.agera.Receiver;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView textView;

    MutableRepository<Integer> integerMutableRepository = Repositories.mutableRepository(0);

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btnTest);
        textView = findViewById(R.id.textView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        integerMutableRepository.accept(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        final Repository<Result<Integer>> repository = Repositories.repositoryWithInitialValue(Result.<Integer>absent())
                .observe(integerMutableRepository)
                .onUpdatesPerLoop()
                .goTo(Executors.newSingleThreadExecutor())
                .attemptGetFrom(new Supplier<Result<String>>() {
                    @NonNull
                    @Override
                    public Result<String> get() {
                        return Result.success("hello");
                    }
                })
                .orSkip()
                .thenTransform(new Function<String, Result<Integer>>() {
                    @NonNull
                    @Override
                    public Result<Integer> apply(@NonNull String input) {
                        if (integerMutableRepository.get() / 2 == 0)
                            return Result.success(1);
                        else
                            return Result.failure(new Throwable("不相等"));
                    }
                }).compile();


        repository.addUpdatable(new Updatable() {
            @Override
            public void update() {
                repository.get().ifSucceededSendTo(new Receiver<Integer>() {
                    @Override
                    public void accept(@NonNull Integer value) {
                        Log.d("tag", "result " + value);
                    }
                }).ifFailedSendTo(new Receiver<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable value) {
                        Log.d("tag", "Throwable " + value);
                    }
                });

            }
        });

    }
}
