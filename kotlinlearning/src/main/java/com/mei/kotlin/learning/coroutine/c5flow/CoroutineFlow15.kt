package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking

/**
 * flowOn
 *
 * 借助于 flowOn ,可以让Flow在发射元素时所处的上下文与收集(终止操作)时所处的上下文是不同的。即发射和收集运行在不同的协程中
 *
 * 值得注意的是: flowOn运算符改变了Flow本身默认的顺序性。
 *
 * 现在，收集操作实际上是发生在-个协程当中，而发射操作发生在另一个协程当中(这一点至关重要) 。
 *
 * flowOn运算符本质上会改变上下文中的CoroutineDispatcher,并且为上游的flow创建另外-个协程
 */
private fun log(msg: String) = println("${Thread.currentThread().name}, $msg")

private fun myMethod(): Flow<Int> = flow {
    log("started")
    for (i in 1..3) {
        Thread.sleep(100)
        log("emit $i")
        emit(i)
    }
}.flowOn(Dispatchers.Default) // 通过 flowOn 指定一个协程分发器，这样就可以让 Flow 的发射程序运行在指定的线程中

fun main() = runBlocking {
    myMethod().collect { log("collect $it") }
}