package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.runBlocking

/**
 * Flow 组合操作
 * zip 操作符：把多个Flow进行合并，组合成一个新的数据并发射，组合会以数据少的Flow为准
 */

fun main() = runBlocking<Unit> {
    val nums = (1..5).asFlow()
    val strs = flowOf("one", "two", "three", "four")
    // 通过zip操作符，把多个Flow进行合并
    nums.zip(strs) { a, b ->
        "$a->$b" // 把两个Flow的结果进行合并，发出一个新的数据
    }.collect {
        println(it)
    }
}