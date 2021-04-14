package com.mei.kotlin.learning.coroutine.c4dispatcher

import kotlinx.coroutines.*

/**
 * @date 2021/4/7
 * @author mxb
 * @desc
 * @desired
 */

/**
 * 模拟Android中的Activity
 */
class Activity : CoroutineScope by CoroutineScope(Dispatchers.Default) {
    // Activity销毁的时候，把在Activity中启动的所有协程也都取消掉
    fun destroy() {
        cancel()
    }

    // 模拟通过协程执行耗时操作
    fun doSomeThing() {
        repeat(8) { // 启动8个协程
            launch {
                delay((it + 1) * 300L)
                println("Coroutine $it is finished")
            }
        }
    }
}

/**
 * 这里模拟在Android中，当一个Activity销毁当时候，把在Activity中启动的所有协程也都取消掉，避免内存泄漏的发生
 *
 */
fun main() = runBlocking {
    val activity = Activity()
    activity.doSomeThing() // 启动协程，执行耗时任务
    println("启动协程")
    delay(1300) //
    println("销毁Activity")
    activity.destroy() // Activity 被销毁了，这个时候就会把在Activity中启动的协程也都取消掉
    delay(9000)
}