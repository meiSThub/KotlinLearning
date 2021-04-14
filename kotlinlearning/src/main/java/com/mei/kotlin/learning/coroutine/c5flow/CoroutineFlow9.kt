package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking

/**
 * Flow的transform 操作符
 * transform 操作符更加强大与随意，可以做任何操作，并且可以发送多次
 */

private suspend fun transformTo(input: Int): String {
    delay(200)
    return "output $input"
}

fun main() = runBlocking<Unit> {
    (1..3).asFlow().transform {
        emit(transformTo(it))
        emit("----------")
    }.collect { println(it) }
}
