package com.plum.flowtest.apply

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * @author:meixianbing
 * @date:2023/4/19 下午7:39
 * @description:SharedFlowTes
 */

fun main() = runBlocking {
    sharedFlow()

    delay(2000)
}

suspend fun sharedFlow() {
    val sharedFlow = MutableSharedFlow<String>()
    GlobalScope.launch(Dispatchers.IO) {
        sharedFlow.emit("hello")
        delay(1000)
        sharedFlow.emit("world")
    }
    // 这个会一直阻塞当前协程
    sharedFlow.collect {
        println("collect:$it")
    }
}