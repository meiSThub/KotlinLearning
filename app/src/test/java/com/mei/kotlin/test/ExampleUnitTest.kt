package com.mei.kotlin.test

import kotlinx.coroutines.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        GlobalScope.launch {
            println("codes run in coroutine scope")
            delay(1500)
            println("finished")
        }
        Thread.sleep(1000)
    }

    @Test
    fun test_runBlocking() {
        println("current thread:${Thread.currentThread().name}")
        runBlocking {
            println("coroutine current thread:${Thread.currentThread().name}")
            println("codes run in coroutine scope")
            delay(1000)
            println("codes run in coroutine scope finished")
        }
    }

    @Test
    fun coroutineTest() {
        // 创建一个协程的作用域
        runBlocking {

            launch {
                println("launch1")
                delay(1000)
                println("launch1 finished")
            }
            // 创建子协程，必须允许在协程作用域中
            launch {
                println("launch2")
                delay(1000)
                println("launch2 finished")
            }
        }
    }

    @Test
    fun coroutineTest1() {
        val start = System.currentTimeMillis()
        runBlocking {
            var index = 0
            repeat(100000) {
                launch {
                    println("coroutine${index++}")
                }
            }
        }
        val end = System.currentTimeMillis()
        println("cost time:${end - start}")
    }

    suspend fun printDot() = coroutineScope {
        launch {
            println("coroutine scope")
            delay(1000)
        }
    }

    @Test
    fun coroutineTest2() {
        runBlocking {
            printDot()
        }
    }

    @Test
    fun coroutineTest3() {
        runBlocking {
            coroutineScope {
                for (i in 1..10) {
                    println("i=$i")
                    delay(1000)
                }
            }
            println("coroutineScope finished")
        }
        println("runBlocking finished")
    }

    @Test
    fun coroutineTest4() {
        runBlocking {
            val start = System.currentTimeMillis()
            val result1 = async {
                delay(1000)
                5 + 5
            }.await()
            val result2 = async {
                delay(1000)
                6 + 6
            }.await()
            println("result is ${result1 + result2}")
            val end = System.currentTimeMillis()
            println("cost time ${end - start}ms")
        }
    }

    @Test
    fun coroutineTest5() {
        runBlocking {
            val start = System.currentTimeMillis()
            val deferred1 = async {
                delay(1000)
                5 + 5
            }
            val deferred2 = async {
                delay(1000)
                6 + 6
            }
            println("result is ${deferred1.await() + deferred2.await()}")
            val end = System.currentTimeMillis()
            println("cost time ${end - start} ms")
        }
    }
}