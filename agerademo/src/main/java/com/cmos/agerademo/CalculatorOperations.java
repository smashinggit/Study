package com.cmos.agerademo;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.android.agera.Merger;
import com.google.android.agera.Result;

import java.util.concurrent.Executors;

/**
 * author : ChenSen
 * data : 2018/9/6
 * desc:
 */
public class CalculatorOperations {


    @NonNull
    public static Result<Result<String>> keepCpuBusy(Result<String> input) {
        String stringCounter = "0";

        for (int i = 0; i < 600_000; i++) {

            if (Thread.currentThread().isInterrupted())
                return Result.failure();

            // Show no love for our CPU and GC.
            Integer intCounter = Integer.valueOf(stringCounter);
            intCounter++;
            stringCounter = intCounter.toString();
        }
        return Result.present(input);
    }


    public static Result<Integer> attemptOperation(@NonNull Pair<Integer, Integer> operands,
                                                   @NonNull Result<Integer> operation) {

        return operation.ifSucceededAttemptMerge(operands, (operator, integerIntegerPair) -> {
            try {
                return Result.present(performOperation(integerIntegerPair, operator));
            } catch (RuntimeException e) {
                return Result.failure(e);
            }
        });
    }

    public static Integer performOperation(Pair<Integer, Integer> pair, Integer operation1) throws ArithmeticException {

        switch (operation1) {
            case 0:
                return pair.first + pair.second;
            case 1:
                return pair.first - pair.second;
            case 2:
                return pair.first * pair.second;
            case 3:
                return pair.first / pair.second;
            default:
                throw new RuntimeException("Invalid operation");
        }

    }

}
