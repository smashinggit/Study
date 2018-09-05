package com.cmos.agerademo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.agera.Observable;
import com.google.android.agera.Receiver;
import com.google.android.agera.Updatable;

import java.util.function.Supplier;

public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView textView;

    Observable observable = new Observable() {
        @Override
        public void addUpdatable(@NonNull Updatable updatable) {

        }

        @Override
        public void removeUpdatable(@NonNull Updatable updatable) {

        }
    };

    Updatable updatable = new Updatable() {
        @Override
        public void update() {

        }
    };

    Supplier<String> supplier = new Supplier<String>() {
        @Override
        public String get() {
            return "haha";
        }
    };

    Receiver<String> receiver = new Receiver<String>() {
        @Override
        public void accept(@NonNull String value) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btnTest);
        textView = findViewById(R.id.textView);


    }
}
