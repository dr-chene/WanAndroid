package com.example.module_main.view.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil

import com.example.lib_base.view.BaseActivity
import com.example.module_main.R
import com.example.module_main.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.makeStatusBarTransparent()
        super.makeStatusBarIconDark()

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}