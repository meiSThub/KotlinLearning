package com.plum.flowtest.function

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException
import kotlin.system.measureTimeMillis

/**
 * @author:meixianbing
 * @date:2023/4/19 下午2:48
 * @description:功能性操作符
 */
fun main() = runBlocking {
    cancellable()
    catch()
    retryWhen()
    retry()
    buffer()
    conflate()
    flowOn()
}

/**
 * 接收的的时候判断 协程是否被取消 ，如果已取消，则抛出异常
 */
suspend fun cancellable() {
    println("==================== cancellable 方法====================")
    val calTime = measureTimeMillis {
        val job = flowOf(1, 2, 3, 4).cancellable().onEach {
            println("onEach:$it")
            delay(100)
        }.launchIn(CoroutineScope(SupervisorJob()))

        delay(200)
        println("flow job is canceled")
        job.cancel() // job取消之后，Flow就不在执行了
    }
    println("calTime=$calTime")
}

/**
 * 对上游异常进行捕获 ，对下游无影响
 *
 * 上游 指的是 此操作符（catch）之前的流
 *
 * 下游 指的是此操作符（catch）之后的流
 */
suspend fun catch() {
    println("==================== catch 方法====================")
    val calTime = measureTimeMillis {
        flowOf(1, 2, 3, 4).onEach {
            println("onEach:$it")
            if (it % 3 == 0) it / 0
        }.catch {
            println("catch 捕获异常：${it.message}")
        }.collect {
            println("collect:$it")
            // if (it % 2 == 0) throw IOException() // 这里如果抛出异常，catch无法捕获，collect 是 catch 的下游
        }
    }
    println("calTime=$calTime")
}


/**
 * 有条件的进行重试 ，lambda 中有两个参数: 一个是 异常原因，一个是当前重试的 index (从0开始).
 */
suspend fun retryWhen() {
    println("==================== retryWhen 方法====================")
    val calTime = measureTimeMillis {
        flowOf(1, 2, 3, 4).onEach {
            throw IOException("/ by zero")
        }.retryWhen { cause, attempt ->
            println("重试下标：$attempt")
            if (attempt > 2) return@retryWhen false // 即不在重试
            cause is IOException// 如果是IO异常，重试一下
        }.catch {
            println("捕获异常：${it.message}")
        }.collect {
            println("collect:$it")
            // if (it % 2 == 0) throw IOException() // 这里如果抛出异常，catch无法捕获，collect 是 catch 的下游
        }
    }
    println("calTime=$calTime")
}

/**
 * 重试机制 ，当流发生异常时可以重新执行。retryWhen 的简化版。
 *
 * retries: ``Long`` = Long.MAX_VALUE 指定重试次数，以及控制是否继续重试.(默认为true)
 */
suspend fun retry() {
    println("==================== retry 方法====================")
    val calTime = measureTimeMillis {
        flowOf(1, 2, 3, 4).onEach {
            throw IOException("/ by zero")
        }.retry(3) { cause ->
            println("重试：${cause.message}")
            cause is IOException// 如果是IO异常，重试一下
        }.catch {
            println("捕获异常：${it.message}")
        }.collect {
            println("collect:$it")
        }
    }
    println("calTime=$calTime")
}

/**
 * 如果操作符的代码需要相当****长时间来执行 ，可使用buffer操作符在执行期间为其创建一个单独的协程
 * capacity: Int = BUFFERED 缓冲区的容量
 * onBufferOverflow: BufferOverflow = BufferOverflow.``SUSPEND **溢出的话执行的操作
 * 有三个选择 ： SUSPEND 挂起， DROP_OLDEST 丢掉旧的，DROP_LATEST 丢掉新的
 */
suspend fun buffer() {
    println("==================== buffer 方法====================")
    val calTime = measureTimeMillis {
        println("no buffer")
        flowOf("a", "b", "c")
            .onEach { println("onEach:$it") }
            .collect {
                println("collect:$it")
            }
        // buffer 增加一个缓存池，当上游发送来的数据，先进入缓存区，
        println("has buffer")
        flowOf("a", "b", "c")
            .onEach { println("onEach:$it") }
            .buffer()
            .collect {
                println("collect:$it")
            }
    }
    println("calTime=$calTime")
}

/**
 * 仅保留最新值, 内部就是 buffer``(``CONFLATED``)
 */
suspend fun conflate() {
    println("==================== conflate 方法====================")
    val calTime = measureTimeMillis {
        // buffer 增加一个缓存池，当上游发送来的数据，先进入缓存区，
        flow {
            repeat(30) {
                delay(100)
                emit(it)
            }
        }.conflate().collect {
            delay(1000) // 模拟处理耗时
            println("collect:$it")
        }
    }
    println("calTime=$calTime")
}

/**
 * 指定上游操作的执行线程 。 想要切换执行线程 就用它！
 */
suspend fun flowOn() {
    println("==================== flowOn 方法====================")
    val calTime = measureTimeMillis {
        // 默认线程
        flowOf(1, 2, 3).onEach {
            println("onEach,coroutine name:${currentCoroutineContext()[CoroutineName]}")
        }.collect {
            println("collect:$it")
        }

        // 指定上游操作的执行线程
        flowOf(1, 2, 3).onEach {
            println("onEach,coroutine name:${currentCoroutineContext()[CoroutineName]}")
        }.flowOn(Dispatchers.IO + CoroutineName("IO线程")).collect {
            println("collect:$it")
        }
    }
    println("calTime=$calTime")
}
