package com.mei.kotlin.learning.coroutine2

import com.mei.kotlin.learning.Log
import kotlinx.coroutines.*

/**
 * @date 2022/1/10
 * @author mxb
 * @desc
 * @desired
 */
fun main() = runBlocking<Unit> {
//    ExceptionHandler().testCoroutineScope4()
//
//    delay(3000)
    val job = Job()
    val job3 = launch(job) {
        val job1 = coroutineContext[Job]
        println("job=$job, job1=$job1")
    }
    println("job3=$job3")
}

class ExceptionHandler {

    fun testCoroutineScope4() {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            println("exceptionHandler, ${coroutineContext[CoroutineName]} $throwable")
        }
        val coroutineScope = CoroutineScope(SupervisorJob() + CoroutineName("coroutineScope"))
        GlobalScope.launch(CoroutineName("scope1") + exceptionHandler) {
            with(coroutineScope) {
                val scope2 = launch(CoroutineName("scope2") + exceptionHandler) {
                    Log.d("scope", "1--------- ${coroutineContext[CoroutineName]}")
                    throw  NullPointerException("空指针")
                }
                val scope3 = launch(CoroutineName("scope3") + exceptionHandler) {
                    scope2.join()
                    Log.d("scope", "2--------- ${coroutineContext[CoroutineName]}")
                    delay(2000)
                    Log.d("scope", "3--------- ${coroutineContext[CoroutineName]}")
                }
                scope2.join()
                Log.d("scope", "4--------- ${coroutineContext[CoroutineName]}")
                coroutineScope.cancel()
                scope3.join()
                Log.d("scope", "5--------- ${coroutineContext[CoroutineName]}")
            }
            Log.d("scope", "6--------- ${coroutineContext[CoroutineName]}")
        }
    }

}