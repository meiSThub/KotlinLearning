package com.mei.kotlin.learning.coroutine.base

import kotlinx.coroutines.*

/**
 * @date 2021/6/3
 * @author mxb
 * @desc 协程上下文
 * @desired
 */
fun main() = runBlocking<Unit> {


    val coroutineContext1 = Job() + CoroutineName("这是第一个上下文")
    println("coroutineContext1=$coroutineContext1")
    val coroutineContext2 = coroutineContext1 + Dispatchers.Default + CoroutineName("这是第二个上下文")
    println("coroutineContext=$coroutineContext2")
    val coroutineContext3 = coroutineContext2 + Dispatchers.Main + CoroutineName("这是第三个上下文")
    println("coroutineContext3=$coroutineContext3")

    println("--------------->")
// 这个+运算符是不对称的，所以在我们实际的运用过程中，通过+增加Element的时候一定要注意它们结合的顺序
    val coroutineContext4 = CoroutineName("这是第四个上下文") + Job()
    println("coroutineContext4=$coroutineContext4")
    val coroutineContext5 = Dispatchers.Default + CoroutineName("这是第五个上下文") + coroutineContext4
    println("coroutineContext5=$coroutineContext5")
    val coroutineContext6 = Dispatchers.Main + CoroutineName("这是第六个上下文") + coroutineContext5
    println("coroutineContext6=$coroutineContext6")

}