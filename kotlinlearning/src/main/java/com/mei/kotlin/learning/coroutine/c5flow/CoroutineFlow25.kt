package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * 类似于catch运算符，onCompletion 只会看到来自于Flow上游的异常，但是它看不到Flow下游的异常。
 *
 */

private fun myMethod(): Flow<Int> = flow {
    for (i in 1..10) {
        emit(i)
    }
}

// 1. 命令式
fun main() = runBlocking<Unit> {
    myMethod().onCompletion {
        println("Flow completed with $it")
    }.catch {
        println("Catch exception: $it")
    }.collect {
        check(it <= 1) {
            "collected $it"
        }
        println("collect $it")
    }

    println("finished")
}