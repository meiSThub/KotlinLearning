package com.mei.kotlin.learning.coroutine

import kotlin.concurrent.thread

/**
 * @date 2021/4/2
 * @author mxb
 * @desc
 * @desired
 */
fun main() {
    repeat(100) {
        thread {
            Thread.sleep(1000)
            println("coroutine index=$it")
        }
    }
    println("hello world")
}