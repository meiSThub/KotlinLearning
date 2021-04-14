package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * @date 2021/4/6
 * @author mxb
 * @desc 挂起函数组合
 * @desired
 */
fun main() = runBlocking {
    val costTime = measureTimeMillis {
        // 这样执行两个挂起函数，是串行的执行的，这样其实效率并不高，要是能够并行执行效率就会高很多
        val value1 = intValue1()
        val value2 = intValue2()
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