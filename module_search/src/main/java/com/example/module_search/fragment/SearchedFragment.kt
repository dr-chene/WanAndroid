package com.example.module_search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseFragment
import com.example.lib_net.loadAction
import com.example.lib_net.result
import com.example.module_search.databinding.FragmentSearchedBinding
import com.example.module_search.repository.SearchRepository
import com.example.module_search.viewmodel.SearchActivityViewModel
import com.example.share_article.adapter.ArticleRecyclerViewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

/**
Created by chene on @date 20-12-8 下午7:24
 **/
class SearchedFragment(private val tag: Int) : BaseFragment() {

    private lateinit var searchedBinding: FragmentSearchedBinding
    private val searchViewModel by sharedViewModel<SearchActivityViewModel>()
    private val searchAdapter by inject<ArticleRecyclerViewAdapter> {
        parametersOf(
            false,
            false,
            null
        )
    }
    private val repository by inject<SearchRepository> { parametersOf(tag) }

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
            if (it.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    repository.refresh(it).result(
                        start = null,
                        completion = { searchViewModel.endSearch() }
                    ) { page ->
                        if (page != null) {
                            searchAdapter.submitList(page.datas)
                        }
                        searchedBinding.searchedNoData.root.visibility =
                            if (page?.datas.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE

                    }
                }
            }
        }
    }

    private fun loadMore() = CoroutineScope(Dispatchers.Main).launch {
        repository.load().result(
            start = { searchViewModel.startSearch() },
            completion = { searchViewModel.endSearch() }
        ) {
            val before = searchAdapter.currentList.toMutableList()
            it?.let { list -> before.addAll(list.datas) }
            searchAdapter.submitList(before)
        }
    }

}