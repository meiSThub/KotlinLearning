package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * Buffer 缓冲
 */

// 这里的Flow没有使用flowOn来切换协程，所以Flow的发射和收集都是运行在同一个协程中的
// 这样就导致Flow每发射一个元素，都要等待收集操作处理完成，才会接着发射下一个元素，这样无形之中就增加了方法处理的耗时
// 那如何解决这种串行操作带来的耗时呢？答案是增加buffer缓冲，详情可参考下一个案例
private fun myMethod(): Flow<Int> = flow {
    for (i in 1..4) {
        delay(100) // 模拟耗时操作
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
    val costTime = measureTimeMillis {
        myMethod().collect {
            delay(200)
            println(it)
        }
    }
    println("costTime $costTime")
}