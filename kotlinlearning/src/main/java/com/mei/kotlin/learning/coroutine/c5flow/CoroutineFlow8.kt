package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

/**
 * Flow的中间运算符
 * Flow的中间运算符的思想与Java Stream完全一致。
 * Flow与Sequence之间在中间运算符上的重要差别在于:对于Flow来说，这些中间运算符内的代码块是可以调用挂起函数的。
 */

private suspend fun mapTo(input: Int): String {
    delay(200)
    return "output $input"
}

fun main() = runBlocking<Unit> {
    (1..10).asFlow().filter { it > 5 }
        .map { mapTo(it) }// Flow 相关的操作符可以调用挂起函数
        .collect { println(it) }
}