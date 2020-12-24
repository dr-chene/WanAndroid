package com.example.module_main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
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
        mainBinding.mainFragmentViewPager.apply {
            isUserInputEnabled = false
            adapter = object : FragmentStateAdapter(this@MainActivity) {
                override fun getItemCount(): Int = 4

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> fragment("/home/fragment")
                        1 -> fragment("/nav/fragment")
                        2 -> fragment("/square/fragment")
                        else -> fragment("/mine/fragment")
                    }
                }
            }
            setCurrentItem(0, false)
        }
    }

    override fun onResume() {
        super.onResume()
        mainBinding.mainNavBottom.selectedItemId = before
    }

    private fun initAction() {
        mainBinding.mainNavBottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> nav(0)
                R.id.item_nav -> nav(1)
                R.id.item_todo -> loginCheck {
                    ARouter.getInstance().build("/todo/activity").navigation()

                }
                R.id.item_square -> nav(2)
                R.id.item_mine -> nav(3)
            }
            true
        }
    }

    private fun fragment(path: String) = ARouter.getInstance().build(path).navigation() as Fragment

    private fun nav(position: Int) {
        before = toBottomNavId(position)
        mainBinding.mainFragmentViewPager.setCurrentItem(position, false)
    }

    private fun toBottomNavId(position: Int) = when (position) {
        0 -> R.id.item_home
        1 -> R.id.item_nav
        2 -> R.id.item_square
        3 -> R.id.item_mine
        else -> R.id.item_home
    }
}