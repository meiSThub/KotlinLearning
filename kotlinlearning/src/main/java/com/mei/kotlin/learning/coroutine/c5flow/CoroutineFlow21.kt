package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * 通过 try{}catch{} 可以捕获Flow整个流程中发生的异常，包括：
 * 1. 发射阶段
 * 2. 转换阶段
 * 3. 收集阶段
 */
private fun myMethod(): Flow<String> = flow {
    for (i in 1..3) {
        println("emitting $i")
        emit(i)
    }
}.map {
    check(it <= 1) {
        "Crash on $it"
    }
    "string $it"
}

fun main() = runBlocking {
    try {
        myMethod().collect {
            println(it)
        }
    } catch (e: Exception) {
        println("Caught $e")
    }
}