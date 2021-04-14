package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.*

/**
 * @date 2021/4/1
 * @author mxb
 * @desc
 * @desired
 */
fun main() = runBlocking {
    val myJob = GlobalScope.launch {
        delay(1000)
        println("Kotlin Coroutine ,threadName=${Thread.currentThread()}")
    }

    println("hello ,threadName=${Thread.currentThread()}")
    myJob.join()
    println("world ,threadName=${Thread.currentThread()}")
}