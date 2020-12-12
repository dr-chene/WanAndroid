package com.example.module_search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lib_base.view.BaseFragment
import com.example.lib_net.loadAction
import com.example.module_search.databinding.FragmentSearchedBinding
import com.example.module_search.viewmodel.SearchActivityViewModel
import com.example.module_search.viewmodel.SearchedViewModel
import com.example.share_home_search.adapter.ArticleRecyclerViewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
Created by chene on @date 20-12-8 下午7:24
 **/
class SearchedFragment : BaseFragment() {

    private lateinit var searchedBinding: FragmentSearchedBinding
    private val searchViewModel by sharedViewModel<SearchActivityViewModel>()
    private val searchedViewModel by viewModel<SearchedViewModel>()
    private val searchAdapter by inject<ArticleRecyclerViewAdapter> { parametersOf(false) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchedBinding = FragmentSearchedBinding.inflate(inflater, container, false)
        context ?: return searchedBinding.root

        initView()
        initAction()
        subscribe()

        return searchedBinding.root
    }

    private fun initView() {
        searchedBinding.rv1searchedResult.adapter = searchAdapter
    }

    private fun initAction() {
        searchedBinding.rv1searchedResult.loadAction {
            loadMore()
        }
    }

    private fun subscribe() {
        searchViewModel.searchContent.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) CoroutineScope(Dispatchers.Main).launch {
                searchedViewModel.search(it) {
                    searchViewModel.endSearch()
                }.collectLatest { cur ->
                    searchAdapter.submitList(cur)
                    searchedBinding.searchedNoData.root.visibility =
                        if (cur.isEmpty()) View.VISIBLE else View.INVISIBLE
                    cancel()
                }
            }
        }
    }

    private fun loadMore() = CoroutineScope(Dispatchers.Main).launch {
        searchedViewModel.load(
            { searchViewModel.startSearch() }
        ) {
            searchViewModel.endSearch()
        }.collectLatest {
            val before = searchAdapter.currentList.toMutableList()
            before.addAll(it)
            searchAdapter.submitList(before)
            cancel()
        }
    }
}