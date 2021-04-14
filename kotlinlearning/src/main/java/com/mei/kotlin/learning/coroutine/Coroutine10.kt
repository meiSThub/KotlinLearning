package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.*

/***
 * 有两种方式可以让计算代码块变为可取消的：
 * 1. 周期性调用一个挂起函数，该挂起函数会检查取消状态，比如说使用yield函数
 * 2. 显示的检查取消状态，
 * 如下案例会使用第二种方式
 * 其中，isActive是协程的一个扩展属性，它是通过CoroutineScope对象添加的
 */
fun main() = runBlocking {
    val start = System.currentTimeMillis()
    val job = launch {
        // 1.计算代码块
        var nextPrintTime = start
        var i = 0
        while (isActive) { // 检查协程的取消状态，协程是否活着
            if (System.currentTimeMillis() > nextPrintTime) {
                println("job:I am sleeping ${i++}")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300)
    println("hello world")
    job.cancelAndJoin() // job 里面执行的 计算代码块，检查协程的取消状态，所以当我们调用job的取消方法后，
    // isActive 就变为false了，从而不会执行循环操作了，达到了协程取消的目的。
    println("welcome")
}
