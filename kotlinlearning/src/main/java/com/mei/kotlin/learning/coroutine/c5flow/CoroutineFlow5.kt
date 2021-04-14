package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking

/**
 * @date 2021/4/8
 * @author mxb
 * @desc
 * @desired
 */

private fun myMethod(): Flow<Int> = flow {
    println("myMethod executed")
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}

/**
 * 只有当调用了Flow对象上的终止操作(如collect) 之后，Flow才会真正执行
 * 当我们调用了myMethod方法后，它实际上是立刻返回的，并不会去执行其中的代码。
 */
fun main() = runBlocking<Unit> {
    println("hello")
    val myFlow = myMethod()
    println("world")
    // 如果不调用Flow的终止方法，如：collect，Flow里面的代码是不会执行的
    // 只有调用了Flow的相关终止方法，Flow里面的代码才会真正的去执行
    myFlow.collect {
        println("$it")
    }

    println("--------------")

    // 终止方法可以调用多次
    myFlow.collect {
        println(it)
    }

}