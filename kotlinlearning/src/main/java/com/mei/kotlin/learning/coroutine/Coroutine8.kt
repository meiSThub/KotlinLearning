package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.*

/**
 * @date 2021/4/2
 * @author mxb
 * @desc   协程取消
 * @desired
 */
fun main() = runBlocking {
    val myJob = GlobalScope.launch {
        repeat(100) {
            println("hello:$it")
            delay(100)
        }
    }
    delay(1100)
    println("hello world")

//    myJob.join() // 等待协程执行完成
    myJob.cancelAndJoin()  // 取消任务并挂起协程，直到任务完成

    Thread.sleep(1000)
    coroutineScope {

    }
    println("welcome")
}