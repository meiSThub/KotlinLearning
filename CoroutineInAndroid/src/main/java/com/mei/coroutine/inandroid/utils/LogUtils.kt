package com.mei.coroutine.inandroid.utils

import android.util.Log

/**
 * @date 2021/6/9
 * @author mxb
 * @desc
 * @desired
 */
fun log(logMessage: String) = Log.i("LogUtils", "[${Thread.currentThread().name}] $logMessage")