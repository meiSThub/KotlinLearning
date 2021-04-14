package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.*

/**
 * @date 2021/4/7
 * @author mxb
 * @desc
 * @desired
 */

private fun log(logMessage: String) = println("[${Thread.currentThread().name}] $logMessage")

val threadLocal = ThreadLocal<String>()

/**
 * ThreadLocal 相关：通过ThreadLocal在不同协程之间进行值的传递
 *
 * ThreadLocal的扩展函数asContextElement，会把ThreadLocal封装成一个ThreadLocalElement对象，并根据方法参数修改
 * ThreadLocal的值，这样修改ThreadLocal之后，在这个协程作用域中的协程，通过ThreadLocal去获取的值，都是修改后的值，
 * 当跳出这个协程作用域回到最初的协程时，通过ThreadLocal获取到的值又是最开始设置的值，既通过ThreadLocal的扩展
 * 函数asContextElement修改的值，只在对应的协程作用域中有效，跳出该协程后就失效了。
 */
fun main() = runBlocking {
    threadLocal.set("hello")
    log("pre main, thread local value: ${threadLocal.get()}")
    val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "world")) {
        log("launch start, thread local value: ${threadLocal.get()}")
        yield()
        withContext(Dispatchers.IO) {
            log("with context, thread local value: ${threadLocal.get()}")
        }
        GlobalScope.launch(Dispatchers.IO) {
            log("global scope, thread local value: ${threadLocal.get()}")
        }
        log("after yield, thread local value: ${threadLocal.get()}")
    }
    job.join() // 等待子协程执行完成
    log("post main, thread local value: ${threadLocal.get()}")
}