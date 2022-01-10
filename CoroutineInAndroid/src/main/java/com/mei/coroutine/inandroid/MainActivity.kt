package com.mei.coroutine.inandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun globalCoroutine(view: View) {
        startActivity(Intent(this, GlobalCoroutineActivity::class.java))
    }

    fun mainScope(view: View) {
        startActivity(Intent(this, MainScopeActivity::class.java))
    }
}