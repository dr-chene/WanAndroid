package com.example.module_search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lib_base.view.BaseFragment
import com.example.lib_net.loadAction
import com.example.module_search.databinding.FragmentSearchedBinding
import com.example.module_search.viewmodel.SearchActivityViewModel
import com.example.share_article.adapter.ArticleRecyclerViewAdapter
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
    private val searchAdapter by inject<ArticleRecyclerViewAdapter> {
        parametersOf(
            false,
            false,
            null
        )
    }

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
            searchViewModel.load(searchAdapter.currentList.toMutableList())
        }
    }

    private fun subscribe() {
        searchViewModel.articles.observe(viewLifecycleOwner){
            searchAdapter.submitList(it)
            searchedBinding.searchedNoData.root.visibility =
                if (it.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
        }
        searchViewModel.searchContent.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                searchViewModel.refresh(it)
            }
        }
    }

}