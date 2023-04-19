package com.plum.flowtest.operator.callback

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * @author:meixianbing
 * @date:2023/4/18 下午7:14
 * @description:回调操作符
 */
fun main() = runBlocking {

    onStart()

    onCompletion()

    onEach()

    onEmpty()

    onSubscription()
}

/**
 * 在上游流开始之前被调用。 可以发出额外元素,也可以处理其他事情，比如发埋点
 */
suspend fun onStart() {
    println("==================== onStart 方法====================")
    flow {
        emit("success")
    }.onStart {
        println("onStart 方法被调用")
        emit("emit on onStart")
    }.collect {
        println("collect: $it")
    }
}

/**
 * 在流取消或者结束时调用。可以执行发送元素，发埋点等操作
 */
suspend fun onCompletion() {
    println("==================== onCompletion 方法====================")
    flow {
        emit("success")
    }.onCompletion {
        println("onCompletion 方法被调用")
    }.collect {
        println("collect:$it")
    }
}

/**
 * 在上游向下游发出元素之前调用
 * 即每个发射操作都会执行onEach操作
 */
suspend fun onEach() {
    println("==================== onEach 方法====================")
    flow {
        for (i in 1..3) emit(i)
    }.onEach {
        println("onEach 方法被调用:$it")
    }.collect {
        println("collect:$it")
    }
}

/**
 * 当流完成却没有发出任何元素时回调。 可以用来兜底.。
 */
suspend fun onEmpty() {
    println("==================== onEmpty 方法====================")
    emptyFlow<String>().onEmpty {
        println("onEmpty 方法被调用")
        emit(" emit on onEmpty->兜底数据")
    }.collect {
        println("collect:$it")
    }
}

/**
 * SharedFlow 专属操作符 （StateFlow是SharedFlow 的一种特殊实现）
 * 在建立订阅之后 回调。 和 onStart 有些区别 ，SharedFlow 是热流，因此如果在onStart里发送值，则下游可能接收不到。
 */
suspend fun onSubscription() {
    println("==================== onSubscription 方法====================")
    MutableStateFlow("success").onSubscription {
        println("onSubscription 方法被调用")
        emit("emit on onSubscription")
    }.collect {
        println("collect:$it")
    }
}
