package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * @date 2021/4/8
 * @author mxb
 * @desc
 * @desired
 */
private fun log(msg: String) = println("${Thread.currentThread().name} ,$msg")

private fun myMethod(): Flow<Int> = flow {
    log("started")
    // 在Flow中开启一个新的协程，指定协程在默认线程池中运行，避免耗时任务阻塞主线程
    // 但运行这个程序就会报异常，异常提醒我们说：事件发射 和 收集 没有运行在同一个协程中，
    // 所以，想通过这种方式来改变 Flow 发射事件 所运行的线程是不合理的，但可以通过flowOn 来解决
    // flowOn 使用参考下一个案例
    withContext(Dispatchers.Default) {
        for (i in 1..3) {
            Thread.sleep(100)
            emit(i)
        }
    }
}

fun main() = runBlocking {
    myMethod().collect { log("collect $it") }
}