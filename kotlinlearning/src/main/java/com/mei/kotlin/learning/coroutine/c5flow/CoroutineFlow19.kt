package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * flatMapConcat:把嵌套操作打平，变成一维的
 *
 * Flow<Flow<Int>> -> Flow<Int>
 */
private fun myMethod(i: Int): Flow<String> = flow {
    emit("$i: first")
    kotlinx.coroutines.delay(500)
    emit("$i: second")
}

fun main() = runBlocking<Unit> {

    (1..3).asFlow().onEach {
        delay(100)
    }.flatMapConcat {
        myMethod(it)
    }.collect {
        println(it)
    }

}