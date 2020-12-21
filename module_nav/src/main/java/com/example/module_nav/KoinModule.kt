package com.example.module_nav

import com.example.module_nav.adapter.NavRecyclerViewAdapter
import com.example.module_nav.adapter.TagFlowAdapter
import com.example.module_nav.bean.AdaptNav
import com.example.module_nav.bean.AdaptTag
import com.example.module_nav.remote.NavService
import com.example.module_nav.remote.ProjectService
import com.example.module_nav.remote.PublicService
import com.example.module_nav.remote.TreeService
import com.example.module_nav.viewmodel.NavViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

/**
Created by chene on @date 20-12-12 下午3:51
 **/
val navModule = module {
    single { get<Retrofit>().create(NavService::class.java) }
    single { get<Retrofit>().create(TreeService::class.java) }
    single { get<Retrofit>().create(ProjectService::class.java) }
    single { get<Retrofit>().create(PublicService::class.java) }

    factory { (tags: List<AdaptTag>) -> TagFlowAdapter(tags) }

    single { AdaptNav.AdaptNavDiffCallBack() }
    factory { (click: (AdaptTag) -> Unit) -> NavRecyclerViewAdapter(click) }

    viewModel { (tab: Int) -> NavViewModel(tab, get(), get(), get(), get(), get(), get(), get(), get()) }
}