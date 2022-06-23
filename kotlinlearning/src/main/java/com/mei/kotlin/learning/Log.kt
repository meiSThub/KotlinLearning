package com.mei.kotlin.learning

/**
 * @date 2022/1/10
 * @author mxb
 * @desc
 * @desired
 */
class Log {
    companion object {
        fun d(tag: String? = null, msg: String) {
            println("$tag, $msg")
        }
    }
}