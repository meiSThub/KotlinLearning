package com.mei.kotlin.test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * @date 2021/5/14
 * @author mxb
 * @desc
 * @desired
 */
class MainViewModel : ViewModel() {

    private fun test() {
        viewModelScope.launch {

        }
    }
}