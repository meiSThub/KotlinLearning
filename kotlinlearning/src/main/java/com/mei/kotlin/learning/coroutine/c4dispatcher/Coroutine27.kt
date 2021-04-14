package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


private fun log(logMessage: String) = println("[${Thread.currentThread().name}] $logMessage")

/**
 * CoroutineName上下文元素可以让我们对协程迸行命名，以便能够输出可读性更好的日志信息
 */
fun main() = runBlocking(CoroutineName("main")) {
    log("hello")
    val value1 = async(CoroutineName("Coroutine1")) {
        delay(100)
        log("coroutine log")
        10
    }

    val value2 = async(CoroutineName("Coroutine2")) {
        delay(200)
        log("coroutine log")
        20
    }
    println("The result is ${value1.await() + value2.await()}")
}