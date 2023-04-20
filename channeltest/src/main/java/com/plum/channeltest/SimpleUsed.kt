package com.plum.channeltest

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce

/**
 * @author:meixianbing
 * @date:2023/4/19 下午5:13
 * @description: Channel的简单使用
 */

fun main() = runBlocking {
    testRendezvous(this)
    testUnlimited(this)
    testConflated(this)
    testBuffered(this)
    testProduce(this)
}

/**
 * Channel的默认容量就是 Channel.RENDEZVOUS
 *
 * RENDEZVOUS 就是 0，这个词本意就是描述“不见不散”的场景，所以你不来 receive，我这 send 就一直搁这儿挂起等着。
 * 换句话说，我们开头的例子里面，如果 consumer 不 receive，producer 里面的第一个 send 就给挂起了。
 */
suspend fun testRendezvous(coroutineScope: CoroutineScope) {
    println("=================== Channel.RENDEZVOUS 容量===========================")
    val channel = Channel<Int>(Channel.RENDEZVOUS) // Channel的默认容量就是 Channel.RENDEZVOUS
    joinAll(
        coroutineScope.launch(Dispatchers.IO) {
            var i = 0
            repeat(2) {
                println("send:$i")
                channel.send(i++)
                delay(100)
            }
        },
        coroutineScope.launch(Dispatchers.IO) {
            repeat(2) {
                println("receive:${channel.receive()}")
            }
        }
    )
}

/**
 * 容量: Channel.UNLIMITED
 *
 * UNLIMITED 比较好理解，无限制，从它给出的实现 LinkedListChannel 来看，这一点也与我们的 LinkedBlockingQueue 类似。
 */
suspend fun testUnlimited(coroutineScope: CoroutineScope) {
    println("=================== Channel.UNLIMITED 容量===========================")
    val channel = Channel<Int>(Channel.UNLIMITED)
    joinAll(
        coroutineScope.launch(Dispatchers.IO) {
            var i = 0
            repeat(10) {
                println("send:$i")
                channel.send(i++)
                delay(100)
            }
        },
        coroutineScope.launch {
            repeat(5) {
                println("consumer1,receive:${channel.receive()}")
            }
        },
        coroutineScope.launch {
            repeat(5) {
                println("consumer2,receive:${channel.receive()}")
            }
        }
    )
}

/**
 * 容量: Channel.CONFLATED
 *
 * CONFLATED，这个词是合并的意思，这个类型的 Channel 有一个元素大小的缓冲区，但每次有新元素过来，都会用新的替换旧的。
 */
suspend fun testConflated(coroutineScope: CoroutineScope) {
    println("=================== Channel.CONFLATED 容量===========================")
    val channel = Channel<Int>(Channel.CONFLATED)
    joinAll(
        coroutineScope.launch(Dispatchers.IO) {
            var i = 0
            repeat(10) {
                println("send:$i")
                channel.send(i++)
                delay(100)
            }
        },
        coroutineScope.launch {
            repeat(5) {
                println("consumer1,receive:${channel.receive()}")
            }
        },
        coroutineScope.launch {
            repeat(5) {
                println("consumer2,receive:${channel.receive()}")
            }
        }
    )
}

/**
 * 容量: Channel.BUFFERED
 *
 * BUFFERED 了，它接收一个值作为缓冲区容量的大小,默认64。
 */
suspend fun testBuffered(coroutineScope: CoroutineScope) {
    println("=================== Channel.BUFFERED 容量===========================")
    val channel = Channel<Int>(Channel.BUFFERED)
    joinAll(
        coroutineScope.launch(Dispatchers.IO) {
            var i = 0
            repeat(10) {
                println("send:$i")
                channel.send(i++)
                delay(100)
            }
        },
        coroutineScope.launch {
            repeat(5) {
                println("consumer1,receive:${channel.receive()}")
            }
        },
        coroutineScope.launch {
            repeat(5) {
                println("consumer2,receive:${channel.receive()}")
            }
        }
    )
}


/**
 * 容量: Channel.BUFFERED
 *
 * BUFFERED 了，它接收一个值作为缓冲区容量的大小,默认64。
 */
suspend fun testProduce(coroutineScope: CoroutineScope) {
    println("=================== produce 方法创建 Channel ===========================")
    joinAll(
        coroutineScope.launch(Dispatchers.IO) {
            val channel = produce(Dispatchers.Unconfined) {
                send(1)
                send(2)
            }
            for (i in channel) {
                println("got:$i")
            }
        }
    )
}




