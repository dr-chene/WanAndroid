package com.example.module_search

import com.example.module_search.adapter.HotKeyAdapter
import com.example.module_search.adapter.MyFlowTagAdapter
import com.example.module_search.bean.SearchHistoryTag
import com.example.module_search.fragment.NotSearchedFragment
import com.example.module_search.fragment.SearchedFragment
import com.example.module_search.remote.SearchService
import com.example.module_search.repository.SearchHistoryRepository
import com.example.module_search.repository.SearchRepository
import com.example.module_search.viewmodel.SearchActivityViewModel
import com.example.module_search.viewmodel.SearchedViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

/**
Created by chene on @date 20-12-8 下午9:07
 **/
val searchModule = module {

    factory { (click: (SearchHistoryTag) -> Unit) -> HotKeyAdapter(click) }
    factory { (data: List<SearchHistoryTag>) -> MyFlowTagAdapter(data) }
    single { SearchHistoryRepository(get()) }

    factory { NotSearchedFragment() }
    factory { (tag: Int) -> SearchedFragment(tag) }
    single { (get() as Retrofit).create(SearchService::class.java) }
    factory { SearchRepository() }

    viewModel { SearchedViewModel(get()) }
    viewModel { SearchActivityViewModel(get()) }
}