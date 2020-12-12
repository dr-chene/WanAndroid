package com.example.module_nav.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lib_base.view.BaseFragment
import com.example.module_nav.adaptNavNav
import com.example.module_nav.adapter.NavRecyclerViewAdapter
import com.example.module_nav.bean.AdaptTag
import com.example.module_nav.databinding.FragmentNavBinding
import com.example.module_nav.viewmodel.NavViewModel
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
class TabNavFragment: BaseFragment() {

    private lateinit var fragmentNavBinding: FragmentNavBinding
    private val navViewModel by viewModel<NavViewModel>()
    private val click: (AdaptTag) -> Unit = {
        TODO("not define")
    }
    private val navAdapter by inject<NavRecyclerViewAdapter>{ parametersOf(click) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentNavBinding = FragmentNavBinding.inflate(inflater, container, false)
        context ?: return fragmentNavBinding.root

        initView()
        initAction()
        subscribe()

        return fragmentNavBinding.root
    }

    private fun initView() {
        fragmentNavBinding.navNav.navRv.adapter = navAdapter
        refresh()
    }

    private fun initAction() {
        fragmentNavBinding.navNav.navFabRefresh.setOnClickListener {
            refresh()
        }
        fragmentNavBinding.navNav.navRv.adapter
    }

    private fun subscribe() {
        CoroutineScope(Dispatchers.IO).launch {
            navViewModel.getNavs()
                .collectLatest {
                    withContext(Dispatchers.Main) {
                        navAdapter.submitList(it.adaptNavNav())
                    }
                }
        }
    }

    private fun refresh() = CoroutineScope(Dispatchers.IO).launch {
        navViewModel.refresh()
    }
}