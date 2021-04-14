package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * buffer的主要作用在于对发射的数据进行缓存，减少等待时间。
 *
 * buffer与flow0n之间存在一定的关系:
 * 实际上，flowOn运算符本质上在遇到需要改变CoroutineDispatcher时也会使用同样的缓冲机制，只不过该示例并没有
 * 改变执行上下文而已。
 */

private fun myMethod(): Flow<Int> = flow {
    for (i in 1..4) {
        delay(100) // 模拟耗时操作
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
    val costTime = measureTimeMillis {
        // 在执行Flow的收集操作之前，先进行buffer操作，这样就会使得上游发射的数据先缓存到缓冲池中，collect操作
        // 去缓存池中取数据，这样就避免了生产者与消费者效率不一致的问题
        myMethod().buffer().collect {
            delay(200)
            println(it)
        }
    }
    println("costTime $costTime")
}