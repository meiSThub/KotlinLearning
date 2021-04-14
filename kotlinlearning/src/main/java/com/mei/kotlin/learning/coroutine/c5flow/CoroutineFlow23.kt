package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking

/**
 * Flow完成的声明式方法
 *
 * 对于声明式方式来说，Flow提供了-个名为onCompletion中间操作，该操作会在Flow完成收集后得到调用
 */
private fun myMethod(): Flow<Int> = (1..10).asFlow()

// 2. 声明式
fun main() = runBlocking<Unit> {
    myMethod().onCompletion { // 通过onCompletion 监听Flow执行完成
        println("onCompletion")
    }.collect {
        println(it)
    }
}