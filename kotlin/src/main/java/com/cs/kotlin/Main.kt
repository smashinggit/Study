package com.cs.kotlin

import kotlinx.coroutines.*

/**
 * @Desc
 * @Author ChenSen
 * @Date 2020/5/8-22:09
 *
 */
object Main {

    @JvmStatic
    fun main(args: Array<String>) {
//        GlobalScope.launch {
//            val deferred1 = async(start = CoroutineStart.LAZY) { work1() }
//            val deferred2 = async(start = CoroutineStart.LAZY) { work2() }
//
//            println("work1() + work2() = ${deferred1.await() + deferred2.await()}")
//        }


        Thread.sleep(3000)


    }

    private  fun work1() {
        Thread.sleep(1000) //模拟耗时操作
        println("do work1 in ${Thread.currentThread().name}")
    }

    private  fun work2() {
        Thread.sleep(1000) //模拟耗时操作
        println("do work2 in ${Thread.currentThread().name}")
    }

    private fun ui1(){
        println("update ui1 in ${Thread.currentThread().name}")
    }

    private fun ui2(){
        println("update ui2 in ${Thread.currentThread().name}")
    }
}


