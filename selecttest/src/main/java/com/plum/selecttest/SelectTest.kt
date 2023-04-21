package com.plum.selecttest

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlin.system.measureTimeMillis

/**
 * 如今的二维码识别应用场景越来越广了，早期应用比较广泛的识别SDK如zxing、zbar，它们各有各的特点，也存在识别不出来的情况，
 * 为了将两者优势结合起来，我们想到的方法是同一份二维码图片分别给两者进行识别。
 * 两个识别库哪个识别快就用哪个的结果
 */
fun main() = runBlocking {
    serial()
    coroutineParallel()
    multipleListen()
    select()
    selectOnChannel()
    selectRandom()
}

suspend fun getQrcodeInfoFromZxing(url: String): String {
    // 模拟耗时
    delay(2000)
    return "Zxing,I'm finished"
}

suspend fun getQrcodeInfoFromZbar(url: String): String {
    // 模拟耗时
    delay(1000)
    return "Zbar,I'm finished"
}

/**
 * 串行执行两个耗时任务
 */
suspend fun serial() {
    println("================ 串行执行耗时任务 =================")
    val calTime = measureTimeMillis {
        val qrcode1 = getQrcodeInfoFromZxing("")
        val qrcode2 = getQrcodeInfoFromZbar("")
        println("qrcode1=$qrcode1,qrcode2=$qrcode2")
    }
    println("串行执行耗时任务耗时：$calTime")

    // 日志打印：
    // qrcode1=Zxing,I'm finished,qrcode2=Zbar,I'm finished
    // 串行执行耗时任务耗时：3004
}

/**
 * 协程并行执行耗时任务
 *
 * 与 串行 相比，虽然识别过程是放在协程里并行执行的，但是在等待识别结果却是串行的
 */
suspend fun coroutineParallel() {
    println("================ 协程并行执行耗时任务 =================")
    val calTime = measureTimeMillis {
        val deferredZxing = GlobalScope.async {
            getQrcodeInfoFromZxing("")
        }
        val deferredZbar = GlobalScope.async {
            getQrcodeInfoFromZbar("")
        }

        val qrcode1 = deferredZxing.await()
        val qrcode2 = deferredZbar.await()

        println("qrcode1=$qrcode1,qrcode2=$qrcode2")
    }
    println("串行执行耗时任务耗时：$calTime")

    // 日志打印：
    // qrcode1=Zxing,I'm finished,qrcode2=Zbar,I'm finished
    // 串行执行耗时任务耗时：2007
}


/**
 * 同时监听多路结果，哪个快就先回调
 */
suspend fun multipleListen() {
    println("================ 同时监听多路结果 =================")
    val startTime = System.currentTimeMillis()
    val deferredZxing = GlobalScope.async {
        getQrcodeInfoFromZxing("")
    }
    val deferredZbar = GlobalScope.async {
        getQrcodeInfoFromZbar("")
    }

    var isEnd = false
    var result: String? = null
    GlobalScope.launch {
        if (!isEnd) {
            // 没有结束，继续识别
            val qrcode = deferredZxing.await()
            if (!isEnd) {
                result = qrcode
                println("zxing,useTime=${System.currentTimeMillis() - startTime},qrcode=$qrcode")
                // 标记识别结束
                isEnd = true
            }
        }
    }

    GlobalScope.launch {
        if (!isEnd) {
            val qrcode = deferredZbar.await()
            // 识别没有结束，说明自己是第一个返回结果的
            if (!isEnd) {
                result = qrcode
                println("zbar,useTime=${System.currentTimeMillis() - startTime},qrcode=$qrcode")
                // 标记识别结束
                isEnd = true
            }
        }
    }

    // 检测是否有结果返回
    while (!isEnd) {
        delay(1)
    }
    // 有返回结果了
    println("识别结果：result=$result")

    // 日志打印：
    // zbar,useTime=1001,qrcode=Zbar,I'm finished
    // 识别结果：result=Zbar,I'm finished
}

/**
 * 虽说上个Demo结果符合预期，但是多了很多额外的代码、多引入了其它协程，并且需要子模块对标记进行赋值（对"isEnd"进行赋值），
 * 没有达到解耦的目的。我们希望子模块的任务是单一且闭环的，如果能在一个函数里统一检测结果的返回就好了。
 * Select 就是为了解决多路数据的选择而生的。
 */
suspend fun select() {
    println("================ select监听async的结果 =================")
    val startTime = System.currentTimeMillis()
    val deferredZxing = GlobalScope.async {
        getQrcodeInfoFromZxing("")
    }
    val deferredZbar = GlobalScope.async {
        getQrcodeInfoFromZbar("")
    }
    // 通过select 监听zxing、zbar 结果返回
    val result = kotlinx.coroutines.selects.select {
        // 监听 zxing 的返回结果， 注意：一定要调用SelectClause1 的 invoke 方法，返回结果
        deferredZxing.onAwait.invoke { it }
        // deferredZxing.onAwait { it } // 等同于 调用 invoke 方法
        // 监听 zbar 的返回结果
        deferredZbar.onAwait.invoke { it }
        // deferredZbar.onAwait { it }
    }
    // 运行到此，说明已经有结果返回
    println("识别结果：useTime:${System.currentTimeMillis() - startTime},result=$result")

    // 日志打印：
    // 识别结果：useTime:1001,result=zbar result:Zbar,I'm finished
}


/**
 * Select 还可以监听Channel的发送方/接收方 数据
 */
suspend fun selectOnChannel() {
    println("================ select监听Channel的结果 =================")
    val startTime = System.currentTimeMillis()
    val channelZxing = GlobalScope.produce {
        val qrcode = getQrcodeInfoFromZxing("")
        send(qrcode)
    }
    val channelZbar = GlobalScope.produce {
        val qrcode = getQrcodeInfoFromZbar("")
        send(qrcode)
    }
    // 通过select 监听zxing、zbar 结果返回
    val result = kotlinx.coroutines.selects.select {
        // 监听 zxing 的返回结果， 注意：一定要调用SelectClause1 的 invoke 方法，返回结果
        channelZxing.onReceive.invoke { it }
        // deferredZxing.onAwait { it } // 等同于 调用 invoke 方法
        // 监听 zbar 的返回结果
        channelZbar.onReceive.invoke { it }
        // deferredZbar.onAwait { it }
    }
    // 运行到此，说明已经有结果返回
    println("识别结果：useTime:${System.currentTimeMillis() - startTime},result=$result")

    // 日志打印：
    // 识别结果：useTime:1005,result=Zbar,I'm finished
}

/**
 * 如果select有多个数据同时到达，select 默认会选择第一个数据，若想要随机选择数据，可做如下处理：
 */
suspend fun selectRandom() {
    println("================ 随机选择返回结果 =================")
    val startTime = System.currentTimeMillis()
    val channelZxing = GlobalScope.produce {
        val qrcode = getQrcodeInfoFromZxing("")
        send(qrcode)
    }
    val channelZbar = GlobalScope.produce {
        val qrcode = getQrcodeInfoFromZbar("")
        send(qrcode)
    }
    // 当 zxing、zbar 同时返回结果时，可以用 selectUnbiased 方法随机选择一个结果返回
    val result = kotlinx.coroutines.selects.selectUnbiased {
        // 监听 zxing 的返回结果， 注意：一定要调用SelectClause1 的 invoke 方法，返回结果
        channelZxing.onReceive.invoke { it }
        // deferredZxing.onAwait { it } // 等同于 调用 invoke 方法
        // 监听 zbar 的返回结果
        channelZbar.onReceive.invoke { it }
        // deferredZbar.onAwait { it }
    }
    // 运行到此，说明已经有结果返回
    println("识别结果：useTime:${System.currentTimeMillis() - startTime},result=$result")

    // 日志打印：
    // 识别结果：useTime:1005,result=Zbar,I'm finished
}


