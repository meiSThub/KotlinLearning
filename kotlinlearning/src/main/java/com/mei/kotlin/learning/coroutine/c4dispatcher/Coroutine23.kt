package com.mei.kotlin.learning.coroutine.c4dispatcher

import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * 增加协程信息打印，增加kotlin的jvm配置参数：-Dkotlinx.coroutines.debug
 * 配置步骤：run-> Edit Configurations->Templates->Kotlin->VM Options，增加参数：-Dkotlinx.coroutines.debug
 */
private fun log(logMessage: String) = println("[${Thread.currentThread().name}] $logMessage")

fun main() {
    newSingleThreadContext("thread1").use { context1 ->
        newSingleThreadContext("thread2").use { context2 ->
            runBlocking(context1) {
                log("Started in context1")
                withContext(context2) {
                    log("Working in context2")
                }
                log("End in context1")
            }
        }
    }
}