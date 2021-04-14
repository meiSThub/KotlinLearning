package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Dispatchers.Unconfined
 * Dispatchers.Unconfined协程分发器会在调用者线程中去启动协程，但仅仅会持续到第一个挂起点; 当挂起结束后程序恢复执行时，
 * 它会继续协程代码的执行，但这时执行协程的线程是由之前所调用的挂起函数来决定的(这个挂起函数是由哪个线程来执行的，
 * 那么后面的代码就会由这个线程来执行)。Dispatchers.Unconfined协程分发器适用于这样的一些协程:它既不会消耗CPU时间，
 * 同时也不会更新任何共享的数据(特定于具体的线程)
 *
 * Dispatchers.Unconfined是一种高级的机制，它对于某些特殊情况是很有帮助作用的:协程执行的分发是不需要的，
 * 或者会产生意料之外的副作用，这是因为协程中的操作必须要立刻执行。
 *
 * 我们在日常的代码编写中，几乎不会使用Dispatchers.Unconfined这种协程分发器。
 */
fun main() = runBlocking<Unit> {
    println("runBlocking,thread:${Thread.currentThread().name}")
    launch(Dispatchers.Unconfined) {
        println("Dispatchers.Unconfined,thread:${Thread.currentThread().name}")
        // delay方法前面的代码，运行在runBlocking所在的线程中，即主线程
        delay(200) //
        // delay方法和delay方法之后的代码，运行在其它线程中
        println("Dispatchers.Unconfined,thread:${Thread.currentThread().name}")
    }

    // 没有指定协程分发器，默认运行在父协程runBlocking一样的线程中
    launch {
        println("No params,thread:${Thread.currentThread().name}")
        delay(200)
        println("No params,thread:${Thread.currentThread().name}")
    }
}