package com.example.module_search.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseFragment
import com.example.module_search.R
import com.example.module_search.adapter.HotKeyAdapter
import com.example.module_search.adapter.MyFlowTagAdapter
import com.example.module_search.bean.SearchHistory
import com.example.module_search.bean.SearchHistoryTag
import com.example.module_search.databinding.FragmentNotSearchedBinding
import com.example.module_search.viewmodel.SearchActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf

/**
Created by chene on @date 20-12-8 下午7:24
 **/
class NotSearchedFragment : BaseFragment() {

    private lateinit var notSearchedBinding: FragmentNotSearchedBinding
    private val searchActivityViewModel by sharedViewModel<SearchActivityViewModel>()
    private val click: (SearchHistoryTag) -> Unit = {
        searchActivityViewModel.search(it.content)
    }
    private val hotKeyAdapter by inject<HotKeyAdapter> { parametersOf(click) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        notSearchedBinding = FragmentNotSearchedBinding.inflate(inflater, container, false)
        context ?: return notSearchedBinding.root

        initView()
        initAction()
        subscribe()

        return notSearchedBinding.root
    }

    private fun initView() {
        notSearchedBinding.searchRvHotKey.adapter = hotKeyAdapter
        notSearchedBinding.searchChipKey.isChecked = true
    }

    private fun initAction() {
        notSearchedBinding.searchSearchHistory.apply {
            setOnTagClickListener { _, position, _ ->
                (adapter.getItem(position) as SearchHistoryTag).apply {
                    searchActivityViewModel.search(content)
                }
                return@setOnTagClickListener true
            }
        }
        notSearchedBinding.searchIvDeleteSearchHistory.setOnClickListener {
            delete()
        }
        notSearchedBinding.searchNotSearchChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.search_chip_key -> {
                    searchActivityViewModel.changeSearchTag(SearchHistory.SEARCH_TAG_KEY)
                }
                R.id.search_chip_author -> {
                    searchActivityViewModel.changeSearchTag(SearchHistory.SEARCH_TAG_AUTHOR)
                }
            }
        }
    }

    private fun subscribe() {
        searchActivityViewModel.hotKeys.observe(viewLifecycleOwner) {
            hotKeyAdapter.submitList(it)
        }
        CoroutineScope(Dispatchers.Main).launch {
            searchActivityViewModel.searchHistory.collect {
                notSearchedBinding.searchSearchHistory.adapter =
                    get<MyFlowTagAdapter> {
                        parametersOf(it.map { search ->
                            SearchHistoryTag(search.searchContent, search.searchTag)
                        })
                    }
                notSearchedBinding.searchIvDeleteSearchHistory.visibility =
                    if (it.isEmpty()) View.INVISIBLE else View.VISIBLE
            }
        }
    }

    private fun delete() {
        AlertDialog.Builder(requireContext()).apply {
            setMessage("是否清除全部历史记录?")
            setPositiveButton("全部删除") { _, _ ->
                deleteAll()
            }
            setNegativeButton("取消") { _, _ ->
                "操作取消".showToast()
            }
        }.show().apply {
            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        }
    }

    private fun deleteAll() = CoroutineScope(Dispatchers.IO).launch {
        notSearchedBinding.searchSearchHistory.adapter.apply {
            for (i in 0 until count) {
                searchActivityViewModel.deleteSearchHistory((getItem(i) as SearchHistoryTag).content)
            }
        }
    }
}