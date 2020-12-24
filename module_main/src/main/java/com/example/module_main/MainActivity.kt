package com.example.module_main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.view.BaseActivity
import com.example.lib_net.loginCheck
import com.example.module_main.databinding.MainActivityBinding

@Route(path = "/main/activity")
class MainActivity : BaseActivity() {

    private lateinit var mainBinding: MainActivityBinding
    private var before = R.id.item_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        makeStatusBarIconDark()
        mainBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        initView()
        initAction()
    }

    private fun initView() {
        nav("/home/fragment", R.id.item_home)
    }

    override fun onResume() {
        super.onResume()
        mainBinding.mainNavBottom.selectedItemId = before
    }

    private fun initAction() {
        mainBinding.mainNavBottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> nav("/home/fragment", R.id.item_home)
                R.id.item_nav -> nav("/nav/fragment", R.id.item_nav)
                R.id.item_todo -> loginCheck {
                    ARouter.getInstance().build("/todo/activity").navigation()
                }
                R.id.item_square -> nav("/square/fragment", R.id.item_square)
                R.id.item_mine -> nav("/mine/fragment", R.id.item_mine)
            }
            true
        }
    }

    private fun nav(path: String, selectId: Int){
        supportFragmentManager.commit {
            replace(R.id.main_fragment_container,
                ARouter.getInstance().build(path).navigation() as Fragment)
        }
        before = selectId
    }
}