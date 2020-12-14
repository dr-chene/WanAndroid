package com.example.module_square.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lib_base.view.BaseFragment
import com.example.lib_net.loadAction
import com.example.module_square.databinding.FragmentSquareBinding
import com.example.module_square.repository.SquareRepository
import com.example.share_article.adapter.ArticleRecyclerViewAdapter
import com.example.share_article.request
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
Created by chene on @date 20-12-14 上午9:16
 **/
class TabSquareFragment(
    private val type: Int
) : BaseFragment() {

    private lateinit var tabSquareBinding: FragmentSquareBinding
    private val repository by inject<SquareRepository> { parametersOf(type) }
    private val adapter by inject<ArticleRecyclerViewAdapter> { parametersOf(false) }
    private val refreshCompletion = {
        tabSquareBinding.squareSrl.isRefreshing = false
    }
    private val loadStart = {
        tabSquareBinding.squareLoad.root.visibility = View.VISIBLE
    }
    private val loadCompletion = {
        tabSquareBinding.squareLoad.root.visibility = View.INVISIBLE
    }

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
            tabSquareBinding.squareRv.smoothScrollToPosition( 0)
        }
    }

    private fun subscribe() {

    }

    private fun refresh() = repository.refresh().request(null, refreshCompletion) {
        adapter.submitList(it)
    }

    private fun load() = repository.load().request(loadStart, loadCompletion) {
        val cur = adapter.currentList.toMutableList()
        cur.addAll(it)
        adapter.submitList(cur)
    }
}