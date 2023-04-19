package com.plum.flowtest.filter

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * @author:meixianbing
 * @date:2023/4/18 下午8:42
 * @description:过滤操作符
 */

fun main() = runBlocking {
    filter()

    filterNot()

    filterNotNull()

    filterIsInstance()

    drop()

    dropWhile()

    take()

    takeWhile()

    debounce()

    sample()

    distinctUntilChangedBy()

    distinctUntilChanged()
}

/**
 * 筛选出符合条件的值
 */
suspend fun filter() {
    println("==================== filter 方法====================")
    flow {
        for (i in 1..5) emit(i)
    }.filter {
        it % 2 == 0
    }.collect {
        println("collect: $it")
    }
}


/**
 * 筛选不符合条件相反的值,相当于filter取反
 */
suspend fun filterNot() {
    println("==================== filterNot 方法====================")
    flow {
        for (i in 1..5) emit(i)
    }.filterNot {
        it % 2 == 0
    }.collect {
        println("collect: $it")
    }
}

/**
 * 筛选不为空的值
 */
suspend fun filterNotNull() {
    println("==================== filterNotNull 方法====================")
    flow {
        for (i in 1..5) if (i % 2 == 0) emit(i) else emit(null)
    }.filterNotNull().collect {
        println("collect: $it")
    }
}

/**
 * 筛选对应类型的值
 */
suspend fun filterIsInstance() {
    println("==================== filterIsInstance 方法====================")
    flow {
        for (i in 1..5) emit(i)
        emit("保留字符串")
    }.filterIsInstance<String>().collect {
        println("collect: $it")
    }
}

/**
 * 入参count为int类型 ,作用是 丢弃掉前 n 个的值
 */
suspend fun drop() {
    println("==================== drop 方法====================")
    flow {
        for (i in 1..5) emit(i)
    }.drop(2) // 抛弃前面两个数据
        .collect {
            println("collect: $it")
        }
}

/**
 * 这个操作符有点特别，和 filter 不同！ 它是找到第一个不满足条件的，返回其和其之后的值。
 * 如果首项就不满足条件，则是全部返回。
 */
suspend fun dropWhile() {
    println("==================== dropWhile 方法====================")
    flow {
        for (i in 1..5) emit(i)
    }.dropWhile {
        it != 3
    }.collect {
        println("collect: $it")
    }
}


/**
 * 返回前 n个 元素
 */
suspend fun take() {
    println("==================== take 方法====================")
    flow {
        for (i in 1..5) emit(i)
    }.take(3).collect {
        println("collect: $it")
    }
}

/**
 * 也是找第一个不满足条件的项，但是取其之前的值 ，和 dropWhile 相反。
 */
suspend fun takeWhile() {
    println("==================== takeWhile 方法====================")
    flow {
        for (i in 1..5) emit(i)
    }.takeWhile {
        it != 3
    }.collect {
        println("collect: $it")
    }
}


/**
 * 防抖节流 ，指定时间内的值只接收最新的一个，其他的过滤掉。搜索联想场景适用
 */
suspend fun debounce() {
    println("==================== debounce 方法====================")
    flow {
        for (i in 1..5) {
            emit(i)
        }
    }.debounce(200).collect {
        println("collect: $it")
    }
}

/**
 * 采样 。给定一个时间周期，仅获取周期内最新发出的值
 *
 * 图示: 每个 - 代表 20毫秒
 * |1------200|-------400|-------600|-------800|------1000|------1200|------1400|------1600|------1800|
 * |----------|----------|----------|----------|----------|----------|----------|----------|----------|
 * |1--120|---240|---360|---480|---600|---720|---840|---960|--1080|--1200| 每延时120毫秒发射一次
 * |------|------|------|------|------|------|------|------|------|------|
 * |1----2|-----3|-----4|-----5|-----6|-----7|-----8|-----9|----10| 发射的值
 * |1------200|-------400|-------600|-------800|------1000|------1200|------1400|------1600|------1800|
 *
 * 取值：2，4，5，7，8，9,10
 * 在第三个周期，即301-400之间，有两个值：3和4，而sample是取周期内的最新值，所以取4，后面的同理
 */
suspend fun sample() {
    println("==================== sample 方法====================")
    flow {
        for (i in 1..10) {
            emit(i)
            delay(120)
        }
    }.sample(200).collect {
        println("collect: $it")
    }
}


/**
 * 去重操作符，判断连续的两个值是否重复，可以选择是否丢弃重复值。
 * keySelector: (T) -> Any? 指定用来判断是否需要比较的 key
 */
suspend fun distinctUntilChangedBy() {
    println("==================== distinctUntilChangedBy 方法====================")
    flow {
        emit(Pair("张三", 10))
        emit(Pair("李四", 10))
        emit(Pair("王五", 20))
    }.distinctUntilChangedBy {
        it.second
    }.collect {
        println("collect: $it")
    }
}

/**
 * 过滤用，distinctUntilChangedBy 的简化调用 。连续两个值一样，则跳过发送
 */
suspend fun distinctUntilChanged() {
    println("==================== distinctUntilChanged 方法====================")
    flow {
        emit(Pair("张三", 10))
        emit(Pair("张三", 10))
        emit(Pair("张三", 20))
    }.distinctUntilChanged().collect {
        println("collect: $it")
    }
}