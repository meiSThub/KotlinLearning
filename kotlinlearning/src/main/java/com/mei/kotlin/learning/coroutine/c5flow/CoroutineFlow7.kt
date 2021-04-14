package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

/**
 * Flow 构建器（Flow builder）
 * 1. flow 是最经常被使用的一种流构建器
 * 2. flowOf 构建器可用于定义能够发射固定数量值的流
 * 3. 对于各种集合与序列来说，他们都提供了asFlow() 扩展函数来将自身转换为Flow
 *
 * inline, noinine, crossinline
 * non-local returns: 非局部返回，实际上表示的是在一个方法内部，我们可以在其中通过一个lambda表达式的返回
 * 来直接将外层方法返回。
 *
 * crossinline 的作用实际上就是表示被标记的lambda表达式是不允许非局部返回的。
 */

fun main() = runBlocking {

    // 1. flow 构建器
    flow<Int> {
        for (i in 1..4) {
            emit(i)
        }
    }.collect { println(it) }

    println("------------")

    // 2. flowOf 构建器
    flowOf(1, 2, 3).collect { println(it) }

    println("------------")

    // 3. 各种集合与序列 的扩展函数 asFlow 构建器
    (1..10).asFlow().collect {
        println(it)
    }

}


