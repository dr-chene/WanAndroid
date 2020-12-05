package com.example.module_home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.module_home.adapter.ArticleRecyclerViewAdapter
import com.example.module_home.databinding.FragmentHomeBinding
import com.example.module_home.viewmodel.ArticleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

@Route(path = "/home/fragment")
class HomeFragment : Fragment() {

    private lateinit var homeBinding: FragmentHomeBinding
    private val articleViewModel by viewModel<ArticleViewModel>()
    private val adapter by inject<ArticleRecyclerViewAdapter>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return homeBinding.root

        initView()
        initAction()
        subscribe()

        return homeBinding.root
    }

    private fun initView() {
        homeBinding.homeSwipeRefresh.isRefreshing = true
        homeBinding.includeContent.homeRv.adapter = adapter
        CoroutineScope(Dispatchers.Main).launch {
            articleViewModel.refresh()
        }
    }

    private fun initAction() {
        homeBinding.homeSwipeRefresh.setOnRefreshListener {
            Log.d("TAG_01", "initAction: refresh")
            CoroutineScope(Dispatchers.IO).launch {
                articleViewModel.refresh()
            }
        }
    }

    private fun subscribe() {
        articleViewModel.articles.observe(viewLifecycleOwner) {
            Log.d("TAG_01", "subscribe: ${it.size}")
            it.forEach {
                Log.d("TAG_02", "subscribe: ${it.title}")
            }
            adapter.submitList(it)
            Log.d("TAG_01", "subscribe: $adapter")
            homeBinding.homeSwipeRefresh.isRefreshing = false
        }
    }
}