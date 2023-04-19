package com.plum.flowtest.combine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * @author:meixianbing
 * @date:2023/4/18 下午9:31
 * @description:组合操作符
 */

fun main() = runBlocking {
    combine()
    combineTransform()
    zip()
    merge()
    flattenConcat()
    flattenMerge()
    flatMapConcat()
    flatMapLatest()
    flatMapMerge()
}

/**
 * 组合每个流最新发出的值。
 *
 * |1-------------------------------100->1|101-----------------------------200->2|
 * 1-15->a | 16-30->b | 31-45->c
 *
 * 所以在1-100毫秒内，flow1发射最新值1，与flow2发射的最新值c 结合
 * 在101-200毫秒内，flow2发射最新值2，与flow2发射的最新值c结合
 */
suspend fun combine() {
    println("==================== combine 方法====================")
    val flow1 = flowOf(1, 2).onEach { delay(100) }
    val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
    val calTime = measureTimeMillis {
        flow1.combine(flow2) { num, s ->
            "$num->$s"
        }.collect {
            println("collect: $it")
        }
    }
    println("calTime=$calTime")
}

/**
 * 顾名思义 combine + transform****
 */
suspend fun combineTransform() {
    println("==================== combineTransform 方法====================")
    val flow1 = flowOf(1, 2).onEach { delay(100) }
    val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
    val calTime = measureTimeMillis {
        flow1.combineTransform(flow2) { num, s ->
            emit("$num->$s")
        }.collect {
            println("collect: $it")
        }
    }
    println("calTime=$calTime")
}


/**
 * 对两个流进行组合，分别从二者取值，一旦一个流结束了，那整个过程就结束了。
 *
 * 合并两个Flow，已其中发射数据少的Flow为准，当发射数据少的Flow发送完成并结束后，则整个过程就结束了，
 * 即：组合成的新Flow的发射次数为 两个Flow的 最小值
 */
suspend fun zip() {
    println("==================== zip 方法====================")
    val flow1 = flowOf(1, 2).onEach { delay(100) }
    val flow2 = flowOf("a", "b", "c", "d")
    val calTime = measureTimeMillis {
        flow1.zip(flow2) { num, s ->
            "$num->$s"
        }.collect {//  flow1和flow2发射次数的最新值是flow1发射两次，所以组合成的新Flow也只会发射两次
            println("collect: $it")
        }
    }
    println("calTime=$calTime")
}

/**
 * 合并多个流成 一个流。 可以用在 多级缓存加载上
 *
 * 注：在不保留元素顺序的情况下将给定的流合并为单个流。所有流并发合并，不限制同时收集的流数。
 */
suspend fun merge() {
    println("==================== merge 方法====================")
    val flow1 = flowOf(1, 2).onEach { delay(10) }
    val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
    val flow3 = flowOf("+", "-", "?").onEach { delay(5) }
    val calTime = measureTimeMillis {
        listOf(flow1, flow2, flow3).merge().collect {
            println("collect: $it")
        }
    }
    println("calTime=$calTime")
}

/**
 * 以顺序方式将给定的流展开为单个流 ，是Flow<Flow<T>>的扩展函数
 */
suspend fun flattenConcat() {
    println("==================== flattenConcat 方法====================")
    val flow1 = flowOf(1, 2).onEach { delay(100) }
    val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
    val calTime = measureTimeMillis {
        flowOf(flow1, flow2).flattenConcat().collect {
            println("collect: $it")
        }
    }
    println("calTime=$calTime")
}

/**
 * 作用和 flattenConcat 一样，但是可以设置并发收集流的数量。
 *
 * 有个入参：concurrency: Int ,即并发数量，当其 == 1时，效果和 flattenConcat 一样，大于 1 时，则是并发收集。
 */
suspend fun flattenMerge() {
    println("==================== flattenMerge 方法====================")
    val flow1 = flowOf(1, 2, 3).flowOn(Dispatchers.IO)
    val flow2 = flowOf("a", "b", "c").flowOn(Dispatchers.IO)
    val flow3 = flowOf("+", "？", "@").flowOn(Dispatchers.IO)
    val calTime = measureTimeMillis {
        flowOf(flow1, flow2, flow3).flattenMerge(3).collect {
            println("collect: $it")
        }
    }
    println("calTime=$calTime")

    // 顺序并不固定
    // collect: +
    // collect: ？
    // collect: @
    // collect: 1
    // collect: 2
    // collect: 3
    // collect: a
    // collect: b
    // collect: c
}

/**
 * 这是一个组合操作符，相当于 map + flattenConcat , 通过 map 转成一个流，在通过 flattenConcat 展开合并成一个流
 */
suspend fun flatMapConcat() {
    println("==================== flatMapConcat 方法====================")
    val flow = flowOf(1, 2, 3)
    val calTime = measureTimeMillis {
        flow.flatMapConcat {// Flow<Flow<T>>
            flowOf("$it->map") // 这里的lamb表达式，返回值必须是 Flow，这样才可以继续执行 flattenConcat 方法
        }.collect {
            println("collect: $it")
        }

        // 等价于
        println("equals")
        flow.map { flowOf("$it->map") }.flattenConcat().collect {
            println("collect: $it")
        }
    }
    println("calTime=$calTime")
}

/**
 * 和其他 带 Latest的操作符 一样，如果下个值来了，上变换还没结束，就取消掉。
 *
 * 相当于 transformLatest + emitAll
 */
suspend fun flatMapLatest() {
    println("==================== flatMapLatest 方法====================")
    val flow = flowOf(1, 2, 3).onEach { delay(100) }
    val calTime = measureTimeMillis {
        flow.flatMapLatest {
            flow {
                println("flatMapLatest start:$it")
                delay(200)
                emit("${it}_flatMapLatest")
            }
        }.collect {
            println("collect: $it")
        }
    }
    println("calTime=$calTime")
}

/**
 * 也是组合操作符，简化使用。 map + flattenMerge 。 因此也是有 concurrency: Int 这样一个参数，来限制并发数。
 */
suspend fun flatMapMerge() {
    println("==================== flatMapMerge 方法====================")
    val flow = flowOf(1, 2, 3, 4, 5, 6)
    val calTime = measureTimeMillis {
        flow.flatMapMerge(3) {
            flow {
                delay(10) // 模拟耗时操作
                emit(it)
            }.flowOn(Dispatchers.IO) // 指定每个 Flow 运行的线程，这样就并发执行了
        }.collect {
            println("collect: $it")
        }
    }
    println("calTime=$calTime") // 如果是顺序执行，则每个耗时10毫秒，则总时间肯定超过60毫秒，打印的结果是24毫秒，说明确实是并发执行的
}