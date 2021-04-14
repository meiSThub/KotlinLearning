package com.mei.kotlin.learning.coroutine.c5flow

/**
 * Sequence (序列)
 * 如果在获取每一个元素时都需要执行一定的计算，这种计算是一种阻塞行为, 将计算后的多个结果返回给调用端
 * 关于序列的特点:
 * 1.序列中的数据并非像集合那样一次性返回给调用端， 而是计算完一个数据后就返回一个数据
 * 2.序列中的计算过程会使用调用它的线程来进行，因此它会阻塞调用它的线程的执行
 */
private fun myMethod(): Sequence<Int> = sequence {
    for (i in 100..105) {
        Thread.sleep(100) // 模拟耗时计算
        yield(i) // Sequence 的方法，把一个值传递给构建出来的迭代器（sequence方法创建的迭代器），
        // 并挂起当前序列，直到下一个值传递
    }
}

fun main() {
    // myMethod 中的序列，会阻塞主线程
    myMethod().forEach { println(it) } //
}