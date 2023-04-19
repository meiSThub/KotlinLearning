package com.plum.sequencetest.create

import kotlinx.coroutines.runBlocking

/**
 * @author:meixianbing
 * @date:2023/4/19 下午4:48
 * @description:Sequence 创建方式
 */

fun main() = runBlocking {
    sequenceOf()
    asSequence()
    generateSequence()
    sequence()
}

fun sequenceOf() {
    println("============== sequenceOf 构建Sequence===============")
    sequenceOf(1, 2, 4).forEach {
        println(it)
    }
}

fun asSequence() {
    println("============== asSequence 构建Sequence===============")
    listOf(1, 3, 2).asSequence().forEach {
        println(it)
    }
}

/**
 * 要使用 generateSequence() 创建有限序列，请提供一个函数，该函数在需要的最后一个元素之后返回 null。
 */
fun generateSequence() {
    println("============== generateSequence 构建Sequence===============")
    generateSequence(1) {
        if (it > 3) null else it * 2
    }.forEach {
        println(it)
    }
}

/**
 * sequence() 函数
 */
fun sequence() {
    println("============== sequence 构建Sequence===============")
    sequence {
        println("Start sequence build...")
        // yiled和yiledAll它们将元素返回给序列使用者，并挂起sequence() 的执行，直到使用者请求下一个元素。
        // yield() 使用单个元素作为参数；yieldAll() 中可以采用 Iterable 对象、Iterable 或其他 Sequence。
        yield(1) // 发送一个值
        yieldAll(listOf(3, 4, 5))
    }.forEach {
        println(it)
    }

    // RestrictsSuspension
    //
    // 带有此注释的类和接口将受到限制，不能随意调用挂起函数。 SequenceBuilder 其并非为异步流设计，Sequence用于同步流没什么问题,那么针对异步流我们该如何处理呢？
}
