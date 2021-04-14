package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

/**
 * Flow 完成
 *
 * Kotlin 提供了两种方式来实现Flow的完成
 * 1. 命令式
 * 2. 声明式
 */

private fun myMethod(): Flow<Int> = (1..10).asFlow()

// 1. 命令式
fun main() = runBlocking<Unit> {
    try {
        myMethod().collect {
            println(it)
        }
    } finally {
        // 通过finally块来监听 Flow 的完成，这种方式称为命令式
        println("finally")
    }
}