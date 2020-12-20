package com.example.module_nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.view.BaseFragment
import com.example.module_nav.bean.AdaptTag
import com.example.module_nav.databinding.NavFragmentBinding
import com.example.module_nav.fragment.NavTabFragment
import com.example.module_nav.repository.NavRepository
import com.google.android.material.tabs.TabLayoutMediator

/**
Created by chene on @date 20-12-12 下午3:25
 **/
@Route(path = "/nav/fragment")
class NavFragment : BaseFragment() {

    private lateinit var navFragmentBinding: NavFragmentBinding
    private val navClick: (AdaptTag) -> Unit = {
        ARouter.getInstance()
            .build("/web/activity")
            .withString("link", it.link)
            .navigation()
    }
    private val treeClick: (AdaptTag) -> Unit = {
        ARouter.getInstance()
            .build("/cid/activity")
            .withString("cid", it.id.toString())
            .withString("cate", "article")
            .navigation()
    }
    private val projectClick: (AdaptTag) -> Unit = {
        ARouter.getInstance()
            .build("/cid/activity")
            .withString("cid", it.id.toString())
            .withString("cate", "project")
            .navigation()
    }

    private val publicClick: (AdaptTag) -> Unit = {
        ARouter.getInstance()
            .build("/cid/activity")
            .withString("cid", it.id.toString())
            .withString("cate", "public")
            .navigation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navFragmentBinding = NavFragmentBinding.inflate(inflater, container, false)
        context ?: return navFragmentBinding.root

        initView()

        return navFragmentBinding.root
    }

    private fun initView() {
        navFragmentBinding.navViewPage.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 4

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> NavTabFragment(
                        navClick,
                        NavRepository(NavRepository.TAB_NAV)
                    )
                    1 -> NavTabFragment(
                        treeClick,
                        NavRepository(NavRepository.TAB_TREE)
                    )
                    2 -> NavTabFragment(
                        projectClick,
                        NavRepository(NavRepository.TAB_PROJECT)
                    )
                    else -> NavTabFragment(
                        publicClick,
                        NavRepository(NavRepository.TAB_PUBLIC)
                    )
                }
            }
        }
        navFragmentBinding.apply {
            TabLayoutMediator(navTopTab, navViewPage) { tab, position ->
                when (position) {
                    0 -> tab.text = resources.getText(R.string.nav_tab_nav)
                    1 -> tab.text = resources.getText(R.string.nav_tab_tree)
                    2 -> tab.text = resources.getText(R.string.nav_tab_project)
                    else -> tab.text = resources.getText(R.string.nav_tab_public)
                }
            }.attach()
        }
    }
}