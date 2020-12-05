package com.example.module_home

import android.accounts.NetworkErrorException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.module_home.adapter.ArticleRecyclerViewAdapter
import com.example.module_home.databinding.FragmentHomeBinding
import com.example.module_home.viewmodel.ArticleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
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
        refresh()
    }

    private fun initAction() {
        homeBinding.homeSwipeRefresh.setOnRefreshListener {
            refresh()
        }
    }

    private fun subscribe() {
        articleViewModel.articles.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            homeBinding.homeSwipeRefresh.isRefreshing = false
        }
    }

    private fun refresh() = CoroutineScope(Dispatchers.Main).launch {
        try {
            articleViewModel.refresh()
        } catch (e: NetworkErrorException) {
            Toast.makeText(get(), "网络连接失败，请检查网络", Toast.LENGTH_SHORT).show()
            loadDataError()
        } catch (e: Throwable) {
            Toast.makeText(get(), "获取数据失败，请及时向开发者反映", Toast.LENGTH_SHORT).show()
            Log.d("TAG_EXCEPTION", "${e.message}")
            loadDataError()
        }
    }

    private fun loadDataError() {
        homeBinding.homeSwipeRefresh.isRefreshing = false
    }
}