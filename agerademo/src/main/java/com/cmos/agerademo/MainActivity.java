package com.cmos.agerademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.agera.MutableRepository;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.RepositoryConfig;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    SeekBar seekBar1;
    SeekBar seekBar2;
    TextView textView1;
    TextView textView2;
    TextView tvResult;
    Button btnTest;

    ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    MutableRepository<Integer> mValue1Repo = Repositories.mutableRepository(0);
    MutableRepository<Integer> mValue2Repo = Repositories.mutableRepository(0);
    MutableRepository<Result<Integer>> mOperationSelector = Repositories.mutableRepository(Result.<Integer>absent());

    Repository<Result<String>> mResultRepository =
            Repositories.repositoryWithInitialValue(Result.<String>absent())
                    .observe(mValue1Repo, mValue2Repo, mOperationSelector)
                    .onUpdatesPerLoop()
                    .goTo(mExecutor)
                    .attemptTransform(CalculatorOperations::keepCpuBusy)  //模拟占用CPU和内存
                    .orEnd(Result::failure)
                    .getFrom(mValue1Repo)
                    //  .mergeIn(mValue2Repo, (integer, tAdd) -> new Pair<>(integer, tAdd))
                    .mergeIn(mValue2Repo, Pair::new)
                    // .attemptMergeIn(mOperationSelector, (pair, integerOperator) -> CalculatorOperations.attemptOperation(pair, integerOperator))
                    .attemptMergeIn(mOperationSelector, CalculatorOperations::attemptOperation)
                    .orEnd(Result::failure)
                    .thenTransform(input -> Result.present(input.toString()))
                    //  .onConcurrentUpdate(RepositoryConfig.SEND_INTERRUPT)
                    .compile();

    Updatable mValue1Updatable = () -> textView1.setText(mValue1Repo.get().toString());
    Updatable mValue2Updatable = () -> textView2.setText(mValue2Repo.get().toString());
    Updatable mResultUpdatable = () -> mResultRepository.get()
            .ifSucceededSendTo(value -> tvResult.setText(value))
            .ifFailedSendTo(value -> Toast.makeText(this, value.getLocalizedMessage(), Toast.LENGTH_SHORT).show())
            .ifFailedSendTo(value -> {
                if (value instanceof ArithmeticException)
                    tvResult.setText("DIV#0");
                else
                    tvResult.setText("N/A");
            });


    ViewClickedObservable viewClickedObservable = new ViewClickedObservable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = findViewById(R.id.rgOperator);
        seekBar1 = findViewById(R.id.sb1);
        seekBar2 = findViewById(R.id.sb2);
        textView1 = findViewById(R.id.tv1);
        textView2 = findViewById(R.id.tv2);
        tvResult = findViewById(R.id.tvResult);
        btnTest = findViewById(R.id.btnTest);

        btnTest.setOnClickListener(viewClickedObservable);
        viewClickedObservable.addUpdatable(() ->
                Toast.makeText(this, "test", Toast.LENGTH_SHORT).show());

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbAdd:
                    mOperationSelector.accept(Result.present(0));
                    break;
                case R.id.rbSub:
                    mOperationSelector.accept(Result.present(1));
                    break;
                case R.id.rbMult:
                    mOperationSelector.accept(Result.present(2));
                    break;
                case R.id.rbDiv:
                    mOperationSelector.accept(Result.present(3));
                    break;
            }
        });

        ((RadioButton) findViewById(R.id.rbAdd)).setChecked(true);

        seekBar1.setOnSeekBarChangeListener(new RepositorySeekBarListener(mValue1Repo));
        seekBar2.setOnSeekBarChangeListener(new RepositorySeekBarListener(mValue2Repo));


        mValue1Repo.addUpdatable(mValue1Updatable);
        mValue2Repo.addUpdatable(mValue2Updatable);
        mResultRepository.addUpdatable(mResultUpdatable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mValue1Repo.removeUpdatable(mValue1Updatable);
        mValue2Repo.removeUpdatable(mValue2Updatable);
        mResultRepository.removeUpdatable(mResultUpdatable);
    }

}
