package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() {
    GlobalScope.launch {
        println("coroutine start：currThread=${Thread.currentThread().name}")
        delay(1000)
        println("Kotlin coroutine：currThread=${Thread.currentThread().name}")
    }

    println("hello ：currThread=${Thread.currentThread().name}")
    Thread.sleep(2000)
    println("world ：currThread=${Thread.currentThread().name}")

}