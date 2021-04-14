package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * 通过协程，实现CoroutineFlow1的效果
 *
 * 协程完成类似的效果
 * 1.不会阻塞主线程...
 * 2.计算结果会一次性返回给调用端
 */
private suspend fun myMethod(): List<String> {
    delay(100)
    return listOf("hello", "world", "welcome")
}

fun main() = runBlocking {
    myMethod().forEach { println(it) }
}