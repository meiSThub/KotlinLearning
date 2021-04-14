package com.mei.kotlin.learning.functions

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * @date 2021/4/2
 * @author mxb
 * @desc
 * @desired
 */

/**
 * 被 suspend 修饰的函数叫做挂起函数
 * 挂起函数：suspend function
 * 挂起函数可以像普通函数一样用在协程中，不过它的一个特性在于可以使用其他的挂起函数
 * 重点：挂起函数只能用在协程中或者另一个挂起函数中，普通函数无法调用挂起函数
 */
fun main() {
    println("main function start")
    runBlocking {
        delay(1000)
        hello()
    }
    println("main function end")
}

suspend fun hello() {
    delay(3000)
    println("hello suspend function")
}

fun world() {
//    hello() // 普通函数不能调用挂起函数
}