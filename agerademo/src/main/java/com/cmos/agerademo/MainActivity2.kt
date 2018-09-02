package com.cmos.agerademo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.agera.*
import com.google.android.agera.Function
import kotlin.concurrent.thread

/**
 *
 * author : ChenSen
 * data : 2018/6/22
 * desc:
 */
class MainActivity2 : AppCompatActivity() {

    var countDownRepository = Repositories.mutableRepository(0)
    var listener = ViewCliclPbservable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        thread {
            for (i in 0..10) {
                Thread.sleep(1000)
                countDownRepository.accept(i)
                Log.d("tag", "$i")
            }
        }

        val repository = Repositories.repositoryWithInitialValue(Result.absent<Int>())
                .observe(countDownRepository)
                .onUpdatesPerLoop()
                .attemptGetFrom(object : Supplier<Result<String>> {
                    override fun get(): Result<String> {
                        return Result.success("hello")
                    }
                })
                .orSkip()
                .thenTransform(object : Function<String, Result<Int>> {
                    override fun apply(input: String): Result<Int> {
                        return if ((countDownRepository.get() / 2) == 0)
                            Result.success(1)
                        else
                            Result.failure()
                    }
                }).compile()


        repository.addUpdatable(object : Updatable {
            override fun update() {
                repository.get().ifSucceededSendTo(object : Receiver<Int> {
                    override fun accept(value: Int) {
                        Log.d("tag", "result " + value)

                    }
                }).ifFailedSendTo(object : Receiver<Throwable> {
                    override fun accept(value: Throwable) {
                        Log.d("tag", "result2 " + value)

                    }
                })
            }
        })


    }
}