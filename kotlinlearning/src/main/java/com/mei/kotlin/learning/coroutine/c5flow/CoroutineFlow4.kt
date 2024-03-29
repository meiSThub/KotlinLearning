package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @date 2021/4/8
 * @author mxb
 * @desc
 * @desired
 */

/**
 * 以流的形式，发射数据
 *
 * @return
 */
private fun myMethod(): Flow<Int> = flow {
    for (i in 1..4) {
        delay(100)
        emit(i)
    }
}

/**
 * 如果返回List<String>结果类型，那么就表示只能一次性返回所有值。要想能够表示可以异步计算的流式的值，我们就可以使用
 * Flow<String>类型，它非常类似于Sequence<String>类型，但其值是异步计算的。
 * 1. Flow构建器是通过f1ow方法来实现的。
 * 2. 位于flow{ }构建器中的代码是可以挂起的。
 * 3. myMethod方法无需再使用suspend标识符，值是通过 emit 函数来发射出来的
 * 4. Flow里面的值是通过collect方法来收集的，即接收
 *
 */
fun main() = runBlocking<Unit> {
    launch {
        for (i in 1..4) {
            println("hello $i")
            delay(200)
        }
    }

    // Flow里面的代码与协程里面的代码，是同步执行的，即Flow的执行不会阻塞当前线程
    myMethod().collect { println("flow $it") }
}
