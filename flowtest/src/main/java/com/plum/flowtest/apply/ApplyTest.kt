package com.plum.flowtest.apply

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter

/**
 * @author:meixianbing
 * @date:2023/4/19 下午3:57
 * @description:ApplyTest
 */

fun main() = runBlocking {
    val playState = MutableStateFlow("")
    val playItem = MutableStateFlow("")

    GlobalScope.launch {
        // 从给定流中收集所有值并将它们发送到收集器。它是 flow.collect { value -> emit(value) } 的简写。
        // 即 playState 发射的值，会转发给 playItem，所以playItem可以收集到发送给playState的值
        playItem.emitAll(playState)
        println("emitAll")
    }
    GlobalScope.launch {
        playState.collect {
            println("playState:${it}")
        }
    }

    GlobalScope.launch {
        playItem.filter { it.isNotEmpty() }.collect {
            println("playItem:$it")
        }
        delay(200)
    }

    delay(10)
    playState.emit("state(6)")
    delay(10)
    playState.emit("state(2)")
}