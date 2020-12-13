package com.example.module_nav

import com.example.module_nav.adapter.NavRecyclerViewAdapter
import com.example.module_nav.adapter.TagFlowAdapter
import com.example.module_nav.bean.AdaptNav
import com.example.module_nav.bean.AdaptTag
import com.example.module_nav.remote.NavService
import com.example.module_nav.remote.TreeService
import com.example.module_nav.repository.NavRepository
import com.example.module_nav.repository.TreeRepository
import com.example.module_nav.viewmodel.NavViewModel
import com.example.module_nav.viewmodel.TreeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

/**
Created by chene on @date 20-12-12 下午3:51
 **/
val navModule = module {
    single { NavRepository(get()) }
    single { (get() as Retrofit).create(NavService::class.java) }
    factory { (tags: List<AdaptTag>) -> TagFlowAdapter(tags) }

    single { AdaptNav.AdaptNavDiffCallBack() }
    factory { (click: (AdaptTag) -> Unit) -> NavRecyclerViewAdapter(click) }

    factory { (get() as Retrofit).create(TreeService::class.java) }
    factory { TreeRepository(get()) }

    viewModel { NavViewModel(get()) }
    viewModel { TreeViewModel(get()) }
}