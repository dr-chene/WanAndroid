package com.example.module_nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.view.BaseFragment
import com.example.module_nav.databinding.NavFragmentBinding
import com.example.module_nav.fragment.TabNavFragment
import com.example.module_nav.fragment.TabTreeFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
Created by chene on @date 20-12-12 下午3:25
 **/
@Route(path = "/nav/fragment")
class NavFragment : BaseFragment() {

    private lateinit var navFragmentBinding: NavFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navFragmentBinding = NavFragmentBinding.inflate(inflater, container, false)
        context ?: return navFragmentBinding.root

        initView()
        initAction()
        subscribe()

        return navFragmentBinding.root
    }

    private fun initView() {
        navFragmentBinding.navViewPage.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> TabNavFragment()
                    else -> TabTreeFragment()
                }
            }
        }
        navFragmentBinding.apply {
            TabLayoutMediator(navTopTab, navViewPage) { tab, position ->
                when (position) {
                    0 -> tab.text = resources.getText(R.string.nav_tab_nav)
                    else -> tab.text = resources.getText(R.string.nav_tab_tree)
                }
            }.attach()
        }
    }

    private fun initAction() {

    }

    private fun subscribe() {

    }
}