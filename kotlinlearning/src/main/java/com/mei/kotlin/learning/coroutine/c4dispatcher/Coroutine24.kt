package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking

/**
 *
 * Job的使用方式以及在Context中的具体作用
 * 协程的Job是旧属于其上下文(context) 的一部分，Kotlin为我们提供了一种简洁的手段来通过协程上下文获取到协程自身的Job对象
 * 我们可以通过coroutineContext[Job]表达式来访问上下文中的Job对象
 */
fun main() = runBlocking {
    val job = coroutineContext[Job]
    println("job-> $job")
    println("isActive:${job?.isActive}")
    println("isActive:${coroutineContext.isActive}")
}