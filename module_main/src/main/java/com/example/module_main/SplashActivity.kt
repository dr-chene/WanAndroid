package com.example.module_main

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.view.BaseActivity
import com.example.module_main.databinding.ActivitySplashBinding
import org.koin.android.ext.android.get

@Route(path = "/splash/activity")
class SplashActivity : BaseActivity() {

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