package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.*

/**
 * @date 2021/4/2
 * @author mxb
 * @desc
 * @desired
 */
/**
 * 全局协程：GlobalScope启动的协程，属于全局协程，类似于守护线程（damon thread）
 * 使用GlobalScope启动的活动协程并不会保持进程的生命，他们就像是守护线程一样
 *
 * kotlinx.coroutines 包下的所有挂起函数都是可取消的，如delay
 * 他们会检测协程的取消状态，当取消时就会抛出CancellationException异常
 * 不过，如果协程正在处于某个计算过程当中，并且没有检查取消状态，那么它就是无法被取消的
 */
fun main() = runBlocking {
    val start = System.currentTimeMillis()
    val job = launch {
        // 1.计算代码块
        var nextPrintTime = start;
        var i = 0
        while (i < 20) {
            if (System.currentTimeMillis() > nextPrintTime) {
                println("job:I am sleeping ${i++}")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300)
    println("hello world")
    job.cancelAndJoin() // job 里面执行的 计算代码块，没有一个是方法和代码是检查了协程的取消状态的，所以即使这里取消了协程，
    // 那么job里面的 计算代码块 还是会一直执行，直到 计算代码块 执行完成，即依旧会循环执行20次，才会取消协程，
    // 相当于中途取消协程操作失效了
    println("welcome")
}

