package com.cmos.agera;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.android.agera.Function;
import com.google.android.agera.Merger;
import com.google.android.agera.Receiver;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.RepositoryConfig;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

/**
 * author : ChenSen
 * data : 2019/5/17
 * desc:
 */
public class Test extends Activity {

    Repository<String> repository =
            Repositories.repositoryWithInitialValue("init")
                    .observe()
                    .onUpdatesPerLoop()
                    .getFrom(new Supplier<Float>() {
                        @NonNull
                        @Override
                        public Float get() {
                            return 50000000.00f;
                        }
                    })
                    .mergeIn(new Supplier<String>() {
                        @NonNull
                        @Override
                        public String get() {
                            return "祝大家的银行卡里余额为：";
                        }
                    }, new Merger<Float, String, String>() {
                        @NonNull
                        @Override
                        public String merge(@NonNull Float integer, @NonNull String tAdd) {
                            return tAdd + integer;
                        }
                    })
                    .sendTo(new Receiver<String>() {
                        @Override
                        public void accept(@NonNull String value) {
                            Log.d("tag", value);
                        }
                    })
                    .thenTransform(new Function<String, String>() {
                        @NonNull
                        @Override
                        public String apply(@NonNull String input) {
                            return input + " 吼吼吼~";
                        }
                    })
                    .onDeactivation(RepositoryConfig.CANCEL_FLOW)
                    .onConcurrentUpdate(RepositoryConfig.CANCEL_FLOW)
                    .compile();

    Updatable updatable = new Updatable() {
        @Override
        public void update() {
            Log.d("tag", repository.get());
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        repository.addUpdatable(updatable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        repository.removeUpdatable(updatable);
    }
}
