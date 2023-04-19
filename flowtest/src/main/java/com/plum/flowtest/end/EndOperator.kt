package com.plum.flowtest.end

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * @author:meixianbing
 * @date:2023/4/18 下午5:25
 * @description:末端操作符
 */
fun main() = runBlocking {
    // 1. collect 操作符
    collect()

    // 2. collectLatest 操作符
    collectLatest()

    // 3. collectIndexed 操作符
    collectIndexed()

    // 4. toCollection 操作符:将结果添加到集合
    toCollection()

    // 5. toList 操作符: 将结果转换为List
    toList()

    // 6. toSet 操作符: 将结果转换为List
    toSet()

    // 7. launchIn 操作符: 直接触发流的执行，不设置action,入参为coroutineScope 一般不会直接调用，
    launchIn()

    // 8. last 操作符: 获取流 发出 的最后一个值 ,如果为空会抛异常
    last()

    // 9. lastOrNull 操作符: 返回流 发出 的最后一个值 ,可以为空
    lastOrNull()

    // 10. first 操作符: 返回流 发出 的第一个值 ,如果为空会抛异常
    first()

    // 11. firstOrNull 操作符: 返回流 发出 的第一个值 ,可以为空
    firstOrNull()

    // 12. single 操作符: 接收流发送的第一个值 ，区别于first(),如果为空或者发了不止一个值，则都会报错
    single()

    // 13. singleOrNull 操作符: 接收流发送的第一个值 ，可以为空 ,发出多值的话除第一个，后面均被置为null
    singleOrNull()

    // 14. count 操作符: 返回流发送值的个数。 类似 list.size() ，注：sharedFlow无效(无意义）
    count()

    // 15. fold 操作符: 从初始值开始 执行遍历,并将结果作为下个执行的 参数。
    fold()

    // 16. reduce 操作符: 和fold 差不多。 无初始值
    reduce()
}

/**
 * 触发flow的运行 。 最常用的监听方式
 */
suspend fun collect() {
    println("==============collect操作符==================")
    flow {
        for (i in 1..3) emit("emit($i)")
    }.collect {
        println("collect->$it")
    }
}


/**
 * 与 collect的区别是 ，有新值发出时，如果此时上个收集尚未完成，则会取消掉上个值的收集操作
 */
suspend fun collectLatest() {
    // 与 collect的区别是 ，有新值发出时，如果此时上个收集尚未完成，则会取消掉上个值的收集操作
    println("==============collectLatest操作符==================")
    flow {
        emit(1)
        delay(50)
        emit(2)
    }.collectLatest {
        // 只想要最新的数据，中间值可以丢弃时可以使用此方式
        println("collectLatest:$it,start")
        delay(100)
        println("collectLatest:$it,end")
    }
}

/**
 * 带下标的 收集操作
 */
suspend fun collectIndexed() {
    println("==============collectIndexed操作符==================")
    flow {
        for (i in 1..3) emit("emit($i)")
    }.collectIndexed { index, value ->
        println("collectIndexed:$index->$value")
    }
}

/**
 * toCollection 操作符:将结果添加到集合
 */
suspend fun toCollection() {
    println("==============toCollection操作符==================")
    val list = arrayListOf(0)
    flow {
        emit(1)
        emit(2)
    }.toCollection(list)
    list.forEach {
        println("toCollection:list->$it")
    }
}

/**
 * toList 操作符: 将结果转换为List
 */
suspend fun toList() {
    println("==============toList操作符==================")
    flow {
        emit(1)
        emit(2)
    }.toList().forEach {
        println("toList:list->$it")
    }
}

/**
 * toSet 操作符: 将结果转换为List
 */
suspend fun toSet() {
    println("==============toSet操作符==================")
    flow {
        emit(1)
        emit(2)
    }.toSet().forEach {
        println("toSet:set->$it")
    }
}

/**
 * launchIn 操作符: 直接触发流的执行，不设置action,入参为coroutineScope 一般不会直接调用，
 */
suspend fun launchIn() {
    // 会搭配别的操作符一起使用，如onEach,onCompletion 。返回值是Job
    println("==============launchIn操作符==================")
    flow {
        println("launchIn,thread=${Thread.currentThread()}")
        emit(1)
        emit(2)
    }.launchIn(CoroutineScope(SupervisorJob() + Dispatchers.IO)).join()
}

/**
 * last 操作符: 获取流 发出 的最后一个值 ,如果为空会抛异常
 */
suspend fun last() {
    println("==============last操作符==================")
    flow {
        for (i in 1..10) emit(i)
        emit(null)
    }.last().let { println("last: $it") }
}

/**
 * lastOrNull 操作符: 返回流 发出 的最后一个值 ,可以为空
 */
suspend fun lastOrNull() {
    println("==============lastOrNull操作符==================")
    flow {
        for (i in 1..10) emit(i)
        emit(null)
    }.lastOrNull().let { println("lastOrNull: $it") }
}

/**
 * first 操作符: 返回流 发出 的第一个值 ,如果为空会抛异常
 */
suspend fun first() {
    println("==============first操作符==================")
    flow {
        for (i in 1..10) emit(i)
    }.first().let { println("first: $it") }
}

/**
 * firstOrNull 操作符: 返回流 发出 的第一个值 ,可以为空
 */
suspend fun firstOrNull() {
    println("==============firstOrNull操作符==================")
    flow {
        for (i in 1..10) emit(i)
    }.firstOrNull().let { println("firstOrNull: $it") }
}

/**
 * single 操作符: 接收流发送的第一个值 ，区别于first(),如果为空或者发了不止一个值，则都会报错
 */
suspend fun single() {
    println("==============single操作符==================")
    flow {
        // for (i in 1..10) emit(i)
        emit(0)
    }.single().let { println("single: $it") }
}

/**
 * singleOrNull 操作符: 接收流发送的第一个值 ，可以为空 ,发出多值的话除第一个，后面均被置为null
 */
suspend fun singleOrNull() {
    println("==============singleOrNull操作符==================")
    flow {
        for (i in 1..10) emit(i)    // 发送了多个值，则singleOrNull()返回null
    }.singleOrNull().let { println("singleOrNull: $it") }
}

/**
 * count 操作符: 返回流发送值的个数。 类似 list.size() ，注：sharedFlow无效(无意义）
 */
suspend fun count() {
    println("==============count操作符==================")
    flow {
        for (i in 1..10) emit(i)
    }.count().let { println("count: $it") }
}

/**
 * fold 操作符: 从初始值开始 执行遍历,并将结果作为下个执行的 参数。
 */
suspend fun fold() {
    println("==============fold操作符==================")
    flow {
        for (i in 1..10) emit(i)
    }.fold(1) { acc, value ->
        acc + value
    }.let { println("fold: $it") }
}

/**
 * reduce 操作符: 和fold 差不多。 无初始值
 */
suspend fun reduce() {
    println("==============reduce操作符==================")
    flow {
        for (i in 1..10) emit(i)
    }.reduce { acc, value ->
        acc + value
    }.let { println("reduce: $it") }
}