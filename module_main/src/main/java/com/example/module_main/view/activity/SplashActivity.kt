package com.example.module_main.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.module_main.R
import com.example.module_main.databinding.ActivitySplashBinding
import org.koin.android.ext.android.get


class SplashActivity : AppCompatActivity() {

    private lateinit var splashBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        initView()
    }

    private fun initView() {
        splashBinding.testBtn.setOnClickListener {
            Toast.makeText(get(), "test", Toast.LENGTH_SHORT).show()
        }
    }
}