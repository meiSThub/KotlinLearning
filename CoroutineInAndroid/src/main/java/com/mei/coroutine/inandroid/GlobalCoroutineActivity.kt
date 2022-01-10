package com.mei.coroutine.inandroid

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mei.coroutine.inandroid.databinding.ActivityGlobalCoroutineBinding
import com.mei.coroutine.inandroid.utils.log
import kotlinx.coroutines.*

class GlobalCoroutineActivity : AppCompatActivity() {

    private var job: Job? = null

    private lateinit var binding: ActivityGlobalCoroutineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlobalCoroutineBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    /**
     * GlobalScope：全局顶级协程，与App同寿
     *
     * @param view
     */
    fun getWeather(view: View) {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            log("${coroutineContext[CoroutineName].toString()} 处理异常：$throwable")
            binding.tvWeather.text = "加载失败"
        }
        // 开启一个协程，去执行耗时任务
        job = GlobalScope.launch(
            Dispatchers.Main // 使协程在主线程执行
//                    + SupervisorJob() // 当子协程发生异常的时候，保证父协程不会退出
                    + exceptionHandler // 异常处理对象
                    + CoroutineName("父协程")
        ) {
            log("协程start")

            supervisorScope {


                launch(CoroutineName("子协程")) {
                    // 模拟子协程发生异常
                    throw  NullPointerException("空指针")
                }

                log("开始加载数据")
                // 执行网络请求
                val result = async(Dispatchers.IO) {
                    delay(200)
                    "请求结果：今天天气☀️"
                }

                log("数据加载成功：${result.await()}")
                // 网络执行成功后，刷新页面数据
                binding.tvWeather.text = result.await()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
//        GlobalScope.cancel() // 通过GlobalScope取消协程，会报异常
//  Caused by: java.lang.IllegalStateException: Scope cannot be cancelled because it does not have a job: kotlinx.coroutines.GlobalScope@f8f6413
    }
}