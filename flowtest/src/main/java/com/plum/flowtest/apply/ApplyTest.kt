package com.plum.flowtest.apply

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis

/**
 * @author:meixianbing
 * @date:2023/4/19 下午3:57
 * @description:ApplyTest
 */

fun main() = runBlocking {
    testEmitAll()
    testBuffer6()
}

suspend fun testEmitAll() {
    println("==================== emitAll 方法 =====================")
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

/**
 * 下面的代码，即使加上了 buffer，也不会影响最终的时间，因为生产者几乎不耗时，所有的耗时都是消费者，所以有没有缓冲都没关系
 * 即 消费者 不影响生产者的 生成速度
 */
suspend fun testBuffer6() {
    println("==================== buffer 方法 =====================")
    val flow = flow {
        (1..3).forEach {
            println("emit $it")
            emit(it)
        }
    }
    val time = measureTimeMillis {
        flow.collect {
            delay(2000)
            println("collect:$it")
        }
    }
    println("use time:${time} ms")
}