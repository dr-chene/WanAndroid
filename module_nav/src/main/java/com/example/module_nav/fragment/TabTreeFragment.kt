package com.example.module_nav.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.view.BaseFragment
import com.example.module_nav.adaptTreeNav
import com.example.module_nav.adapter.NavRecyclerViewAdapter
import com.example.module_nav.bean.AdaptTag
import com.example.module_nav.databinding.FragmentTreeBinding
import com.example.module_nav.viewmodel.TreeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
Created by chene on @date 20-12-12 下午7:47
 **/
class TabTreeFragment : BaseFragment() {

    private lateinit var fragmentTreeBinding: FragmentTreeBinding
    private val treeViewModel by viewModel<TreeViewModel>()
    private val click: (AdaptTag) -> Unit = {
        ARouter.getInstance()
            .build("/cid/activity")
            .withString("cid", it.id.toString())
            .navigation()
    }
    private val navAdapter by inject<NavRecyclerViewAdapter> { parametersOf(click) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentTreeBinding = FragmentTreeBinding.inflate(inflater, container, false)
        context ?: return fragmentTreeBinding.root

        initView()
        initAction()
        subscribe()

        return fragmentTreeBinding.root
    }

    private fun initView() {
        fragmentTreeBinding.navTree.navRv.adapter = navAdapter
    }

    private fun initAction() {
        fragmentTreeBinding.navTree.navFabRefresh.setOnClickListener {
            refresh()
        }
    }

    private fun subscribe() {
        CoroutineScope(Dispatchers.IO).launch {
            treeViewModel.getTrees().collectLatest {
                withContext(Dispatchers.Main) {
                    navAdapter.submitList(it.adaptTreeNav())
                }
            }
        }
    }

    private fun refresh() = CoroutineScope(Dispatchers.IO).launch {
        treeViewModel.refresh()
    }
}