package com.example.module_square.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lib_base.view.BaseFragment
import com.example.lib_net.loadAction
import com.example.module_square.databinding.FragmentSquareBinding
import com.example.module_square.viewmodel.SquareViewModel
import com.example.share_article.adapter.ArticleRecyclerViewAdapter
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
Created by chene on @date 20-12-14 上午9:16
 **/
class TabSquareFragment(
    private val type: Int
) : BaseFragment() {

    private lateinit var tabSquareBinding: FragmentSquareBinding
    private val viewModel by viewModel<SquareViewModel> { parametersOf(type) }
    private val adapter by inject<ArticleRecyclerViewAdapter> { parametersOf(false, false, null) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tabSquareBinding = FragmentSquareBinding.inflate(inflater, container, false)
        context ?: return tabSquareBinding.root

        initVew()
        initAction()
        subscribe()

        return tabSquareBinding.root
    }

    private fun initVew() {
        tabSquareBinding.squareRv.adapter = adapter
        tabSquareBinding.squareSrl.isRefreshing = true
        refresh()
    }

    private fun initAction() {
        tabSquareBinding.squareRv.loadAction { load() }
        tabSquareBinding.squareSrl.setOnRefreshListener {
            refresh()
        }
        tabSquareBinding.squareFabUp.fabUp.setOnClickListener {
            tabSquareBinding.squareRv.smoothScrollToPosition(0)
        }
    }

    private fun subscribe() {
        viewModel.articles.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.refreshing.observe(viewLifecycleOwner) {
            tabSquareBinding.squareSrl.isRefreshing = it
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            tabSquareBinding.squareLoad.root.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun refresh() = viewModel.refresh()

    private fun load() = viewModel.load(adapter.currentList.toMutableList())
}