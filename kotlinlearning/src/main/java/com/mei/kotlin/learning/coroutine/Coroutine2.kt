package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @date 2021/4/1
 * @author mxb
 * @desc
 * @desired
 */
fun main() = runBlocking {
    GlobalScope.launch {
        delay(1000)
        println("kotlin coroutine ,threadName=${Thread.currentThread().name}")
    }
    println("hello,threadName=${Thread.currentThread().name}")
    delay(2000)
    println("world,threadName=${Thread.currentThread().name}")
}
