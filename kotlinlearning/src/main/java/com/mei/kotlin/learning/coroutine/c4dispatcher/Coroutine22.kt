package com.mei.kotlin.learning.coroutine

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 * @date 2021/4/7
 * @author mxb
 * @desc
 * @desired
 */

private fun log(logMessage: String) = println("[${Thread.currentThread().name}] $logMessage")

/**
 * 增加协程信息打印，增加kotlin的jvm配置参数：-Dkotlinx.coroutines.debug
 * 配置步骤：run-> Edit Configurations->Templates->Kotlin->VM Options，增加参数：-Dkotlinx.coroutines.debug
 */
fun main() = runBlocking<Unit> {
    val a = async {
        log("hello world")
        10
    }

    val b = async {
        log("welcome")
        20
    }
    log("The result is ${a.await() + b.await()}")
}