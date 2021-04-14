package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.lang.RuntimeException

/**
 * onCompletion 中间操作的一个优势在于它有一个可空的Throwable参数，可用作确定F1ow的收集操作是正常完成还是异常完成的。
 */
private fun myMethod(): Flow<Int> = flow {
    emit(1)
    throw RuntimeException()
}

// 2. 声明式
fun main() = runBlocking<Unit> {
    myMethod().onCompletion { cause ->
        if (cause != null) {
            println("Flow completed Exceptionally")
        }
        println("onCompletion")
    }.catch {
        println("catch entered")
    }.collect {
        println("collect $it")
    }
}