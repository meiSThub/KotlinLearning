package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.*
import org.jetbrains.annotations.TestOnly

/**
 * @date 2021/4/1
 * @author mxb
 * @desc
 * @desired
 */

fun main() {
    GlobalScope.launch {
        delay(1000)
        println("kotlin coroutine")
    }
    println("hello")
    runBlocking {
        delay(2000)
    }
    println("world")
}

