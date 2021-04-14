package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 除去不同的协程构建器所提供的协程作用域（coroutine scope）外，我们还可以通过coroutineScope builder来声明自己的协程作用域。
 * 该构建器会创建一个协程作用域，并且会等待所有启动的子协程全部完成后自身才会完成。
 *
 * runBlocking与coroutineScope之间的主要差别在于，后者在等待所有子协程完成其任务时并不会阻塞当前的线程。而runBlocking会阻塞当前线程
 * 1. runBlocking 并非挂起函数，也就是说，调用它的线程会一直位于该函数之中，直到协程执行完毕为止。
 * 2. coroutineScope 是挂起函数；也就是说，如果中的协程挂起，那么coroutineScope函数也会挂起。
 * 这样，创建coroutineScope的外层函数就可以继续在同一个线程中执行了，该线程会 逃离 coroutineScope之外，并且可以做其他一些事情。
 */
fun main() = runBlocking {
    launch {
        delay(1000)
        println("my job1，threadName=${Thread.currentThread()}")
    }
    println("person，threadName=${Thread.currentThread()}")
    coroutineScope {// coroutineScope 不会阻塞当前线程，所以在等待内部协程完成的时候，会继续轮询coroutineScope之前的代码，
        // 所以上面的协程才能在延时1s后，打印出 my job1的日志输出
        launch {
            delay(10000) // 这里延迟了10s
            println("my job2，threadName=${Thread.currentThread()}")
        }

        delay(3000) // 这里延迟了3s
        // coroutineScope自身所在的协程延迟了3s，比子协程延迟时间要少，所以 hello world 会先执行
        // 等时间到了在执行 my job2 打印
        println("hello world，threadName=${Thread.currentThread()}") // 所以
    }
    println("welcome，threadName=${Thread.currentThread()}")
}