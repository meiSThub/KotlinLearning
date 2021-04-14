package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

/**
 * 限定大小的中间操作符
 */
private fun myNumbers(): Flow<Int> = flow {
    emit(1)
    emit(2)
    println("hello world")
    emit(3)
}

private fun myNumbers2(): Flow<Int> = flow {
    try {
        emit(1)
        emit(2)
        println("hello world")
        emit(3)
    } catch (e: Exception) { // 这里的异常在实际开发中可以不用管
        e.printStackTrace()
    } finally {
        println("finally")
    }
}

fun main() = runBlocking {
    // take 去流中的前两个事件
    myNumbers().take(2).collect { println(it) }
    println("-----------")
    myNumbers2().take(2).collect { println(it) }
}
