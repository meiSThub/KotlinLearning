package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.*

/**
 * 对于该示例来说，当我们在协程的finally块中使用了挂起函数时，会导致出现CancellationException异常，原因在于运行该代码块的
 * 协程已经被取消了。通常情况下，这并不会产生什么问题，因为大多数关闭操作（比如说取消一个job、关闭网络连接等）通常都是
 * 非阻塞的，并不需要使用挂起函数；然而，在极少情况下，当我们在一个取消的协程中进行挂起操作时，我们可以将相应的代码放置
 * 到withContext(Noncancellable){} 当中，在这种结构中，我们实际上使用了withContext函数与NonCancellable上下文。
 */
fun main() = runBlocking {
    val job = launch {
        try {
            repeat(100) {
                println("job:I am sleeping $it")
                delay(500)
            }
        } finally {
//            println("执行了finally块")
//            delay(400)
//            println("finally delay 之后的打印") // 不会被打印

            // 解决办法：
            withContext(NonCancellable) { // 这里执行的代码块，是不会被取消的
                println("执行了finally块")
                delay(400)
                println("finally delay 之后的打印") // 会打印
            }
            // NonCancellable：一个不可取消的永远出去激活状态的Job，它是专门设计给withContext方法使用的，
            // 为了防止不能被取消的代码块 被取消执行了
        }
    }
    delay(1300)
    println("hello world")
    job.cancelAndJoin() // job 里面执行的 计算代码块，检查协程的取消状态，所以当我们调用job的取消方法后，
    // isActive 就变为false了，从而不会执行循环操作了，达到了协程取消的目的。
    println("welcome")
}