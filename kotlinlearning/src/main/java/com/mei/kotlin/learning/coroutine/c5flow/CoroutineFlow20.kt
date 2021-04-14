package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

/**
 * Flow 异常处理
 *
 */
private fun myMethod(): Flow<Int> = flow {
    for (i in 1..3) {
        println("emitting $i")
        emit(i)
    }
}

fun main() = runBlocking {
    try {
        myMethod().collect {
            println(it)
            // check方法，如果参数是false，就会报一个异常，并并lambda表达式中的信息作为异常信息的一部分
            // 用check方法很方便的模拟一个异常
            check(it <= 1) {
                "Collect $it"
            }
        }
    } catch (e: Exception) {
        println("Caught $e")
    } finally {
    }
}
