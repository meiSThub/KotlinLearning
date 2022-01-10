package com.mei.coroutine.inandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.mei.coroutine.inandroid.databinding.ActivityMainScopeBinding
import com.mei.coroutine.inandroid.utils.log
import kotlinx.coroutines.*

class MainScopeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainScopeBinding

    // 默认运行在主线程上的协程作用域
    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScopeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * 获取天气
     *
     * @param view
     */
    fun getWeather(view: View) {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            log("${coroutineContext[CoroutineName]} 处理异常：${throwable}")
        }

        mainScope.launch(exceptionHandler + CoroutineName("父协程")) {
            showLoading()
            supervisorScope {
                log("启动协程")
                launch( CoroutineName("异常协程")) {
                    log("异常子协程")
                    throw NullPointerException("空指针")
                }
                log("开始加载网络数据")
                val result = withContext(Dispatchers.IO) {
                    delay(500)
                    "请求结果：今天天气☀️"
                }
                log("数据加载成功：$result")
                binding.tvWeather.text = result
            }
        }
    }

    private fun showLoading() {
        binding.tvWeather.text = "加载中..."
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}