package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.*

/**
 * 使用finally 来关闭资源
 * join 和 cancelAndJoin 都会等待所有清理动作完成才会继续往下执行
 */
fun main() = runBlocking {
    val start = System.currentTimeMillis()
    val job = launch {
        try {
            repeat(100) {
                println("job:I am sleeping $it")
                delay(500)
            }
        } finally {
            println("执行来finally块")
        }
    }
    delay(1300)
    println("hello world")
    job.cancelAndJoin() // job 里面执行的 计算代码块，检查协程的取消状态，所以当我们调用job的取消方法后，
    // isActive 就变为false了，从而不会执行循环操作了，达到了协程取消的目的。
    println("welcome")
}