package com.mei.kotlin.learning.coroutine.c4dispatcher

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 当我们希望既指定协程的名字，又要指定协程运行的线程，这个时候通过单个协程上下文已经不能满足要求了，
 * 这个时候就需要对多个协程上下文进行合并，可以使用运算符：+ 进行上下文的合并
 */
fun main() = runBlocking<Unit> {
    // CoroutineContext 内部对运算符：+ 进行了重载，详情看 CoroutineContext#plus方法
    launch(CoroutineName("MyCoroutine") + Dispatchers.Default) {
        println("Thread:${Thread.currentThread().name}")
    }
    println("Thread:${Thread.currentThread().name}")
}