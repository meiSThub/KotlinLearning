package com.plum.flowtest.operator.transform

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*

/**
 * @author:meixianbing
 * @date:2023/4/18 下午7:37
 * @description:变换操作符
 */
fun main() = runBlocking(CoroutineName("main")) {
    map()

    mapLatest()

    mapNotNull()

    transform()

    transformLatest()

    transformWhile()

    withIndex()

    scan()

    runningFold()

    runningReduce()

    produceIn()

    shareIn()

    stateIn()

    asStateFlow()

    asSharedFlow()

    receiveAsFlow()

    consumeAsFlow()
}

/**
 * 将发出的值 进行变换 ，lambda的返回值为最终发送的值
 */
suspend fun map() {
    println("==================== map 方法====================")
    flow {
        emit(1)
        emit(2)
    }.map {
        "map->$it"
    }.collect {
        println("collect: $it")
    }
}

/**
 * 类比 collectLatest ,当有新值发送时如果上个变换还没结束，会先取消掉
 */
suspend fun mapLatest() {
    println("==================== mapLatest 方法====================")
    flow {
        emit(1)
        delay(50)
        emit(2)
    }.mapLatest {
        println("mapLatest start:$it")
        delay(100)
        println("mapLatest end:$it")
        "map->$it"
    }.collect {
        println("collect: $it")
    }
}


/**
 * 仅发送 map 后不为空的值
 */
suspend fun mapNotNull() {
    println("==================== mapNotNull 方法====================")
    flow {
        emit(0)
        emit(1)
    }.mapNotNull {// 相当于过滤空值
        if (it == 0) {
            null
        } else {
            "mapNotNull->$it"
        }
    }.collect {
        println("collect: $it")
    }
}

/**
 * 对发出的值 进行变换 。区别于map， transform的接收者是FlowCollector ，因此它非常灵活，可以变换、跳过它或多次发送。
 */
suspend fun transform() {
    println("==================== transform 方法====================")
    flow {
        emit(0)
        emit(1)
    }.transform {
        if (it == 0) {
            emit("zero:$it*2")
        }
        emit("transform->$it")
    }.collect {
        println("collect: $it")
    }
}

/**
 * 类比 mapLatest ,当有新值发送时如果上个变换还没结束，会先取消掉
 */
suspend fun transformLatest() {
    println("==================== transformLatest 方法====================")
    flow {
        emit(0)
        delay(50)
        emit(1)
    }.transformLatest {
        println("transformLatest start:$it")
        delay(100)
        emit("transform->$it")
        println("transformLatest end:$it")
    }.collect {
        println("collect: $it")
    }
}

/**
 * 这个变化的lambda 的返回值是 Boolean ,如果为 False则不再进行后续变换, 为 True则继续执行
 * 即：只有在满足条件的是才进行转换
 */
suspend fun transformWhile() {
    println("==================== transformWhile 方法====================")
    flow {
        for (i in 0..5) emit(i)
    }.transformWhile {
        println("transformWhile start:$it")
        emit("transformWhile: $it")
        // 0 是满足条件的，所以会发射一个0，所以会执行下一个转换
        // 转换1，还是会发射，但这个时候返回false，所以在转换完1之后，就不在转换后面的数据了
        it % 2 == 0
    }.collect {
        println("collect: $it")
    }
}


/**
 * 将结果包装成Indexed:Value 类型
 */
suspend fun withIndex() {
    println("==================== withIndex 方法====================")
    flow {
        for (i in 1..2) emit(i)
    }.withIndex().collect {
        println("withIndex,collect: index=${it.index},value=${it.value}")
    }
}

/**
 * 和 fold 相似，区别是fold 返回的是最终结果，scan返回的是个flow ，会把初始值和每一步的操作结果发送出去。
 */
suspend fun scan() {
    println("==================== scan 方法====================")
    flow {
        for (i in 1..2) emit(i)
    }.scan(10) { accumulator, value ->
        println("scan compute:accumulator=$accumulator,value=$value")
        accumulator + value
    }.collect {
        println("scan,collect: $it")
    }
}

/**
 * 区别于 fold ，就是返回一个新流，将每步的结果发射出去。
 */
suspend fun runningFold() {
    println("==================== runningFold 方法====================")
    flow {
        for (i in 1..2) emit(i)
    }.runningFold(0) { accumulator, value ->
        accumulator + value
    }.collect {
        println("runningFold,collect:$it")
    }
}

/**
 * 区别于 reduce ，就是返回一个新流，将每步的结果发射出去。
 */
suspend fun runningReduce() {
    println("==================== runningReduce 方法====================")
    flow {
        for (i in 1..2) emit(i)
    }.runningReduce { accumulator, value ->
        accumulator + value
    }.collect {
        println("runningReduce,collect:$it")
    }
}


/**
 * produceIn 在指定协程中运行
 * 将 Flow 转换为 Channel
 * 转换为 ReceiveChannel , 不常用。
 * 注： Channel 内部有 ReceiveChannel 和 SendChannel之分,看名字就是一个发送，一个接收。
 */
suspend fun produceIn() {
    println("==================== produceIn 方法====================")
    flow {
        println("thread=${Thread.currentThread()},coroutine name:${currentCoroutineContext()[CoroutineName]}")
        for (i in 1..2) emit(i)
    }.produceIn(CoroutineScope(SupervisorJob() + Dispatchers.IO) + CoroutineName("IO"))
        .consumeEach {
            println("produceIn,consumeEach:$it")
        }
}

/**
 * 将普通flow 转化为 SharedFlow ,即把冷数据流变为热数据流， 其有三个参数
 * scope:  CoroutineScope 开始共享的协程范围
 * started:  SharingStarted 控制何时开始和停止共享的策略
 * replay: Int = 0 发给 新的订阅者 的旧值数量
 *
 * 其中 started 有一些可选项:
 * Eagerly : 共享立即开始，永不停止
 * Lazily : 当第一个订阅者出现时,永不停止
 * WhileSubscribed : 在第一个订阅者出现时开始共享，在最后一个订阅者消失时立即停止（默认情况下），永久保留重播缓存（默认情况下）
 * WhileSubscribed 具有以下可选参数：
 * stopTimeoutMillis — 配置最后一个订阅者消失到协程停止共享之间的延迟（以毫秒为单位）。 默认为零（立即停止）。
 * replayExpirationMillis - 共享的协程从停止到重新激活，这期间缓存的时效
 */
suspend fun shareIn() {
    println("==================== shareIn 方法====================")
    flow {
        emit(1)
        emit(0)
    }.shareIn(
        CoroutineScope(SupervisorJob() + Dispatchers.Default),
        SharingStarted.Eagerly, 2
    ).collect { // 可以有多个观察者
        println("produceIn,collect:$it")
    }
}

/**
 * 将普通 flow 转化为 StateFlow 。 其有三个参数：
 * scope - 开始共享的协程范围
 * started - 控制何时开始和停止共享的策略
 * initialValue - 状态流的初始值
 */
suspend fun stateIn() {
    println("==================== stateIn 方法====================")
    flow {
        emit(1)
        emit(0)
    }.stateIn(
        CoroutineScope(SupervisorJob() + Dispatchers.Default),
        SharingStarted.Eagerly, 1
    ).collect { // 可以有多个观察者
        println("produceIn,collect:$it")
    }

    // stateIn和sharedIn 通常用在其他来源的flow的改造监听，不会像上面那样使用。
}


/**
 * 将 MutableStateFlow 转换为 StateFlow ，就是变成不可变的。常用在对外暴露属性时使用
 */
suspend fun asStateFlow() {
    println("==================== asStateFlow 方法====================")
    MutableStateFlow(0).asStateFlow().collect {
        println("MutableStateFlow->StateFlow：asStateFlow->$it")
    }
}

/**
 * 将 MutableSharedFlow 转换为 SharedFlow ，就是变成不可变的。常用在对外暴露属性时使用
 */
suspend fun asSharedFlow() {
    println("==================== asSharedFlow 方法====================")
    MutableSharedFlow<Int>().asSharedFlow().collect {
        println("MutableStateFlow->SharedFlow：asSharedFlow->$it")
    }
}

/**
 * 将 Channel 转换为Flow ,可以有多个观察者，但不是多播，可能会轮流收到值。
 */
suspend fun receiveAsFlow() {
    println("==================== receiveAsFlow 方法====================")
    Channel<String>().receiveAsFlow().collect {
        println("receiveAsFlow->$it")
    }
}


/**
 * 将Channel 转换为Flow ,但不能多个观察者（会crash）!
 */
suspend fun consumeAsFlow() {
    println("==================== consumeAsFlow 方法====================")
    val eventChannel = Channel<String>()
    eventChannel.send("channel send result")
    val event = eventChannel.consumeAsFlow()
    event.collect {
        println("consumeAsFlow,collect->$it")
    }
    event.collect {
        println("consumeAsFlow,collect->$it")
    }
}
