package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * @date 2021/4/1
 * @author mxb
 * @desc
 * @desired
 */

/**
 * runBlocking开启的协程，会阻塞当前线程，直到协程任务完成
 *
 */
fun main() = runBlocking {
    repeat(100) {
        launch {
            delay(1000)
            println("coroutine index=$it")
        }
    }
    println("Hello world")
}



