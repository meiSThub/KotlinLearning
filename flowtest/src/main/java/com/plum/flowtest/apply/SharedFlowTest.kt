package com.plum.flowtest.apply

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * @author:meixianbing
 * @date:2023/4/19 下午7:39
 * @description:SharedFlowTes
 */

fun main() = runBlocking {
    // sharedFlow()

    emitWaitForCollect()
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

suspend fun emitWaitForCollect() {
    println("================ emit需要等待collect消费完，才能生产下一个数据 ===================")
    //构造热流
    val flow = MutableSharedFlow<String>()

    //开启协程
    GlobalScope.launch {
        //接收数据(消费者)
        flow.collect {
            println("collect: $it")
            delay(2000)
        }
    }

    //发送数据(生产者)
    delay(200)//保证消费者先执行
    println("emit 1 ${System.currentTimeMillis()}")
    flow.emit("hello world1")
    delay(100)
    println("emit 2 ${System.currentTimeMillis()}")
    flow.emit("hello world2")
    delay(100)
    println("emit 3 ${System.currentTimeMillis()}")
    flow.emit("hello world3")
    delay(100)
    println("emit 4 ${System.currentTimeMillis()}")
    flow.emit("hello world4")
}