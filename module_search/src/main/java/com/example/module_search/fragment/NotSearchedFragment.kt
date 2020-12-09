package com.example.module_search.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseFragment
import com.example.module_search.adapter.HotKeyAdapter
import com.example.module_search.adapter.MyFlowTagAdapter
import com.example.module_search.bean.FlowTag
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
    private val click: (String) -> Unit = {
        searchActivityViewModel.search(it)
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
        notSearchedBinding.rvHotKey.adapter = hotKeyAdapter
    }

    private fun initAction() {
        notSearchedBinding.searchHistory.apply {
            setOnTagClickListener { _, position, _ ->
                searchActivityViewModel.search((adapter.getItem(position) as FlowTag).text)
                return@setOnTagClickListener true
            }
        }
        notSearchedBinding.ivDeleteSearchHistory.setOnClickListener {
            delete()
        }
    }

    private fun subscribe() {
        searchActivityViewModel.hotKeys.observe(viewLifecycleOwner) {
            hotKeyAdapter.submitList(it)
        }
        CoroutineScope(Dispatchers.Main).launch {
            searchActivityViewModel.searchHistory.collect {
                notSearchedBinding.searchHistory.adapter =
                    get<MyFlowTagAdapter> {
                        parametersOf(it.map { sear ->
                            FlowTag(sear.searchContent)
                        })
                    }
                notSearchedBinding.ivDeleteSearchHistory.visibility =
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
        notSearchedBinding.searchHistory.adapter.apply {
            for (i in 0 until count) {
                searchActivityViewModel.deleteSearchHistory((getItem(i) as FlowTag).text)
            }
        }
    }
}