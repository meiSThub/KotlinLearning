package com.plum.flowtest.operator.create

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

/**
 * @author:meixianbing
 * @date:2023/4/18 下午4:42
 * @description:创建Flow
 */
class FlowCreate {

    /**
     * 使用顶层行数：flow 创建Flow对象
     */
    fun createFlow1(): Flow<String> {
        return flow {
            for (i in 1..5) {
                println("emit $i")
                emit("send:$i")
            }
        }
    }

    /**
     * asFlow() 方法: 把集合转换成 Flow 对象，
     */
    fun createFlow2(): Flow<Int> = listOf(1, 2, 3, 4, 5).asFlow()

    /**
     * flowOf 方法：把指定对象转换成 Flow 对象
     */
    fun createFlow3(): Flow<Int> = flowOf(1, 2, 3, 4, 5)

    /**
     * 创建一个空的Flow对象
     */
    fun createFlow4(): Flow<Int> = emptyFlow()

    /**
     * 将回调方法改造成flow ,类似suspendCoroutine
     *
     * 比如：网络请求接口回调，就可以用callbackFlow 方法把接口回调改成返回Flow对象
     */
    fun createFlow5(): Flow<String> = callbackFlow {
        send("emit hello")
        awaitClose {
            //如果回调需要解注册，可以在这里操作
            println("finished:close")
        }
    }

    /**
     * 在一般的flow在构造代码块中不允许切换线程，ChannelFlow则允许内部切换线程
     */
    fun createFlow6(): Flow<String> = channelFlow {
        send("hello；thread=${Thread.currentThread()}")
        withContext(Dispatchers.IO) {
            send("执行耗时任务：start；thread=${Thread.currentThread()}")
            delay(100)
            send("执行耗时任务：finished；thread=${Thread.currentThread()}")
        }
    }

}


fun main() = runBlocking {
    val flowCreate = FlowCreate()
    GlobalScope.launch {
        println("================flow方法创建Flow对象======================")
        flowCreate.createFlow1().collect {
            println(it)
        }
        println("================asFlow方法创建Flow对象======================")
        flowCreate.createFlow2().collect {
            println(it)
        }
        println("================flowOf方法创建Flow对象======================")
        flowCreate.createFlow3().collect {
            println(it)
        }
        println("================emptyFlow方法创建Flow对象======================")
        flowCreate.createFlow4().collect {
            println("emptyFlow $it")
        }
        println("================callbackFlow方法创建Flow对象======================")
        flowCreate.createFlow5().collect {
            println(it)
        }
        println("================channelFlow方法创建Flow对象======================")
        flowCreate.createFlow6().collect {
            println(it)
        }
    }.join()
}