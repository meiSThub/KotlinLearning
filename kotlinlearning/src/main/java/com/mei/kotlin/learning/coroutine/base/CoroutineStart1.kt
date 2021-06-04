package com.mei.kotlin.learning.coroutine.base

import kotlinx.coroutines.*

/**
 * @date 2021/4/22
 * @author mxb
 * @desc
 * @desired
 */
fun main() = runBlocking {
//    for (i in 1..1000) {
//        testCoroutineStart()
//        println("--------------------")
//    }
    testFold()
}

private fun testFold() {
    val list = listOf(1, 2, 3, 4, 5)
    val reduce = list.reduce { sum, element ->
        println("sum=$sum,element=$element")
        sum + element
    }
    println("reduce result $reduce")

    val fold = list.fold(0) { sum, element ->
        println("sum=$sum,element=$element")
        sum + element
    }
    println("fold result=$fold")
}

private fun testCoroutineStart() {
    val defaultJob = GlobalScope.launch {
        println("defaultJob -> CoroutineStart.DEFAULT")
    }
    defaultJob.cancel()
    val lazyJob = GlobalScope.launch(start = CoroutineStart.LAZY) {
        println("lazyJob -> CoroutineStart.LAZY")
    }
    val atomicJob = GlobalScope.launch(start = CoroutineStart.ATOMIC) {
        println("atomicJob -> CoroutineStart.ATOMIC挂起前")
        delay(100)
        println("atomicJob -> CoroutineStart.ATOMIC挂起后")
    }
    atomicJob.cancel()
    val undispatchedJob = GlobalScope.launch(start = CoroutineStart.UNDISPATCHED) {
        println("undispatchedJob -> CoroutineStart.UNDISPATCHED挂起前")
        delay(100)
        println("atomicJob -> CoroutineStart.UNDISPATCHED挂起后")
    }
    undispatchedJob.cancel()
}
