package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

/**
 * Flow Context (Flow上下文)
 *
 * Flow的收集动作总是发生在调用协程的上下文当中。这个特性叫做Context Preservation (上下文保留)
 */

private fun log(msg: String) = println("${Thread.currentThread().name} ,$msg")

private fun myMethod(): Flow<Int> = flow {
    log("started")
    for (i in 1..3) {
        emit(i)
    }
}

fun main() = runBlocking {
    // 这个程序有一个问题就是，如果Flow里面的代码是耗时的话，这样执行就相当于是在主线程中进行了耗时的操作
    // 这种显然是不合理的，那能否指定一下Flow里面的代码在哪执行呢？答案是可以的，可以参考下一个例子
    myMethod().collect { log("collect $it") }
}