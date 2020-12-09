package com.example.module_main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.view.BaseActivity
import com.example.module_main.databinding.ActivityMainBinding

@Route(path = "/main/activity")
class MainActivity : BaseActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        makeStatusBarTransparent()
        makeStatusBarIconDark()
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initView()
        initAction()
    }

    private fun initView() {
        supportFragmentManager.commit {
            add(R.id.main_fragment_container,
                ARouter.getInstance().build("/home/fragment").navigation() as Fragment)
        }
    }

    private fun initAction() {

    }
}