package com.mei.kotlin.learning

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyClass {

    var name = "张三"

    fun test() {
        GlobalScope.launch {
            (1..3)
            "".split(" ").asSequence()

            listOf(1, 2, 4).associateWith { }
        }

    }
}