package com.example.module_nav.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lib_base.view.BaseFragment
import com.example.module_nav.adapt
import com.example.module_nav.adapter.NavRecyclerViewAdapter
import com.example.module_nav.bean.AdaptTag
import com.example.module_nav.databinding.NavContentBinding
import com.example.module_nav.repository.NavRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
Created by chene on @date 20-12-12 下午7:47
 **/
class NavTabFragment(
    private val click: (AdaptTag) -> Unit,
    private val repository: NavRepository
) : BaseFragment() {

    private lateinit var fragmentNavBinding: NavContentBinding
    private val navAdapter by inject<NavRecyclerViewAdapter> { parametersOf(click) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentNavBinding = NavContentBinding.inflate(inflater, container, false)
        context ?: return fragmentNavBinding.root

        initView()
        initAction()
        subscribe()

        return fragmentNavBinding.root
    }

    private fun initView() {
        fragmentNavBinding.navRv.adapter = navAdapter
        load()
    }

    private fun initAction() {
        fragmentNavBinding.navFabRefresh.setOnClickListener {
            refresh()
        }
    }

    private fun subscribe() {

    }

    private fun refresh() = CoroutineScope(Dispatchers.IO).launch {
        repository.refresh()
    }

    private fun load() = CoroutineScope(Dispatchers.IO).launch {
        repository.load()?.collectLatest {
            withContext(Dispatchers.Main) {
                navAdapter.submitList(it.adapt())
            }
        }
    }

}