package com.plum.flowtest.apply

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author:meixianbing
 * @date:2023/4/19 下午8:34
 * @description:StateFlow
 */
fun main() = runBlocking {
    val stateFlow = MutableStateFlow(0)

    GlobalScope.launch {
        stateFlow.emit(1)
        delay(500)
        stateFlow.emit(2)
        delay(500)
        stateFlow.emit(2)
    }

    GlobalScope.launch {
        stateFlow.collect {
            println("StateFlow,collect:$it")
        }
    }

    delay(2000)
}