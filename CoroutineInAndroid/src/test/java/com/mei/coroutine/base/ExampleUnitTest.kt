package com.mei.coroutine.base

import kotlinx.coroutines.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun test(){
        println("canonicalName=${String.javaClass.canonicalName}")
    }

    @Test
    fun testException() = runBlocking<Unit> {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("${coroutineContext[CoroutineName]}处理异常 ：$throwable")
        }
        GlobalScope.launch(CoroutineName("父协程") + exceptionHandler) {
            launch(CoroutineName("异常子协程")) {
                println("${Thread.currentThread().name}->我要开始抛异常了")
                throw NullPointerException("异常测试")
            }
            val result = withContext(Dispatchers.IO) {
                delay(200)
                "请求结果：今天天气☀️"
            }
            println("请求结果：$result")
            println("${Thread.currentThread().name}->end")
        }

        Thread.sleep(2000)
    }

    /**
     * 协同作用域: 如果子协程抛出未捕获的异常时，会将异常传递给父协程处理，如果父协程被取消，则所有子协程同时也会被取消。
     *
     * 异常会导致父协程被取消执行，同时导致后续的所有子协程都没有执行完成(可能偶尔有个别会执行完)
     *
     * 默认情况下，在一个协程A中，通过launch或者async开启一个新的协程B，则协程A和B就是协同作用域
     */
    @Test
    fun testException1() = runBlocking<Unit> {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("${coroutineContext[CoroutineName]}处理异常 ：$throwable")
        }

        GlobalScope.launch(CoroutineName("父协程") + exceptionHandler) {
            val job = launch(CoroutineName("子协程")) {
                println("${coroutineContext[CoroutineName]}->我要开始抛异常了")
                for (index in 0..10) {
                    launch(CoroutineName("孙子协程$index")) {
                        println("${coroutineContext[CoroutineName]}")
                    }
                }
                throw NullPointerException("空指针异常")
            }
            println("-----------------")
            for (index in 0..10) {
                launch(CoroutineName("子协程$index")) {
                    delay(10) // 模拟耗时
                    println("${coroutineContext[CoroutineName]}")
                }
            }

            try {
                job.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            println("父协程结束")
        }

        Thread.sleep(2000)
    }

    /**
     * 主从作用域：该作用域下的协程取消操作的单向传播性，子协程的异常不会导致其它子协程取消
     *
     * 主从(监督)作用域需要使用supervisorScope或者SupervisorJob
     *
     * 使用supervisorScope 实现主从作用域
     */
    @Test
    fun testException2() = runBlocking<Unit> {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("${coroutineContext[CoroutineName]}处理异常 ：$throwable")
        }
        val job = GlobalScope.launch(exceptionHandler) {
            supervisorScope {
                launch(CoroutineName("异常子协程")) {
                    println("${coroutineContext[CoroutineName]}->我要开始抛异常了")
                    throw NullPointerException("${coroutineContext[CoroutineName]}的空指针异常")
                }

                for (index in 0..10) {
                    launch(CoroutineName("子协程$index")) {
                        if (index % 3 == 0) {
                            throw NullPointerException("${coroutineContext[CoroutineName]}的空指针异常")
                        }
                        println("${coroutineContext[CoroutineName]}正常执行")
                    }
                }
            }
        }
        job.join()
    }

    /**
     * 主从作用域：该作用域下的协程取消操作的单向传播性，子协程的异常不会导致其它子协程取消
     *
     * 主从(监督)作用域需要使用supervisorScope或者SupervisorJob
     *
     * 使用SupervisorJob 实现主从作用域
     */
    @Test
    fun testException3() = runBlocking<Unit> {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("${coroutineContext[CoroutineName]}处理异常 ：$throwable")
        }

        // 通过SupervisorJob 构建一个主从协程作用域
        val supervisorScope = CoroutineScope(SupervisorJob() + exceptionHandler)

        // 测试无法实现
//        supervisorScope.launch(CoroutineName("父协程")) {
//
//            launch(CoroutineName("异常子协程")) {
//                println("${coroutineContext[CoroutineName]}->我要开始抛异常了")
//                throw NullPointerException("${coroutineContext[CoroutineName]}的空指针异常")
//            }
//
//            for (index in 0..10) {
//                launch(CoroutineName("子协程$index")) {
//                    if (index % 3 == 0) {
//                        throw NullPointerException("${coroutineContext[CoroutineName]}的空指针异常")
//                    }
//                    println("${coroutineContext[CoroutineName]}正常执行")
//                }
//            }
//        }

        with(supervisorScope) {
            launch(CoroutineName("异常子协程")) {
                println("${coroutineContext[CoroutineName]}->我要开始抛异常了")
                throw NullPointerException("${coroutineContext[CoroutineName]}的空指针异常")
            }

            for (index in 0..10) {
                launch(CoroutineName("子协程$index")) {
                    if (index % 3 == 0) {
                        throw NullPointerException("${coroutineContext[CoroutineName]}的空指针异常")
                    }
                    println("${coroutineContext[CoroutineName]}正常执行")
                }
            }
        }

        Thread.sleep(3000)
    }

}