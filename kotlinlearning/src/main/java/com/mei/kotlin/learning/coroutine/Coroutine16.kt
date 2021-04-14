package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * 使用async与await实现并发
 *
 * 从概念上来说，async就像是launch一样，他会开启一个单独的协程，这个协程是个轻量级线程，可以与其他协程并发工作。
 * 区别在于，launch会返回一个Job，但是Job并不会只有任何结果值，而async会返回一个Deffered，这是一个轻量级的非阻塞的future，
 * 它代表一个promise，可以在稍后提供一个结果值。
 *
 * 可以通过在一个deferrd值上调用 .await() 方法来获取最终的结果值，Deferred 也是个Job，因此可以在需要时对其进行取消
 *
 */
fun main() = runBlocking {
    val costTime = measureTimeMillis { // 统计代码执行的时间，返回值就是执行时间
        // 通过async开启一个异步协程，使程序进行并发执行
        val deferred1 = async { intValue1() }
        val deferred2 = async { intValue2() }
        val value1 = deferred1.await()
        val value2 = deferred2.await()
        println("$value1 + $value2 = ${value1 + value2}")
    }
    println("total time=$costTime")
}

private suspend fun intValue1(): Int {
    delay(2000)
    return 15
}

private suspend fun intValue2(): Int {
    delay(3000)
    return 20
}