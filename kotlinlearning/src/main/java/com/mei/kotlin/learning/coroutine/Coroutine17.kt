package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * @date 2021/4/6
 * @author mxb
 * @desc
 * @desired
 */
fun main() {
    val costTime = measureTimeMillis {
        val value1 = intValue1Async()
        val value2 = intValue2Async()
        runBlocking {
            println("value1=${value1.await()} value2=${value2.await()}")
        }
    }
    println("cost time $costTime")
}

private suspend fun intValue1(): Int {
    delay(2000)
    return 15
}

private suspend fun intValue2(): Int {
    delay(3000)
    return 20
}

/**
 * 异步风格的函数
 */
private fun intValue1Async() = GlobalScope.async {
    intValue1()
}

/**
 * 异步风格的函数
 */
private fun intValue2Async() = GlobalScope.async {
    intValue2()
}