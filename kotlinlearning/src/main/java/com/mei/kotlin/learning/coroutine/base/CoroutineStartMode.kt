package com.mei.kotlin.learning.coroutine.base

import kotlinx.coroutines.*

/**
 * @date 2021/6/4
 * @author mxb
 * @desc
 * @desired
 */
fun main() = runBlocking {
    repeat(10000){
        val defaultJob = GlobalScope.launch {
            println("CoroutineStart.DEFAULT")
        }
        defaultJob.cancel()
        val lazyJob = GlobalScope.launch(start = CoroutineStart.LAZY) {
            println("CoroutineStart.LAZY")
        }
        lazyJob.cancel()

        val atomicJob = GlobalScope.launch(start = CoroutineStart.ATOMIC) {
            println("CoroutineStart.ATOMIC 挂起之前")
            delay(200)
            println("CoroutineStart.ATOMIC 挂起之后")
        }
        atomicJob.cancel()

        val undispatchedJob = GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
            println("CoroutineStart.UNDISPATCHED 挂起之前")
            delay(200)
            println("CoroutineStart.UNDISPATCHED 挂起之后")
        }
        undispatchedJob.cancel()

        println("---------------------------------------->")
    }
}