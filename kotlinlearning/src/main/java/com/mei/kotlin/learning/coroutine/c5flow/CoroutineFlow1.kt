package com.mei.kotlin.learning.coroutine.c5flow

import kotlinx.coroutines.runBlocking

/**
 * 直接返回一个集合的做法有两个特点:
 * 1.方法本身是阻塞的:即主线程会进入到该方法中执行。一直执行到该方法返回为止:
 * 2.集合本身是一次性返回给调用端的，即集合中的全部元素均已经获取到后才一起返回给调用端。
 */
private fun myMethod(): List<String> = listOf("hello", "world")

fun main() = runBlocking {
    myMethod().forEach { println(it) }
}