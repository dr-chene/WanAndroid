package com.example.module_collect

import com.example.module_collect.adapter.CollectArticleAdapter
import com.example.module_collect.adapter.CollectWebAdapter
import com.example.module_collect.remote.CollectArticleService
import com.example.module_collect.remote.CollectWebService
import com.example.module_collect.viewmodel.CollectArticlePageViewModel
import com.example.module_collect.viewmodel.CollectWebViewModel
import com.example.share_collect.bean.CollectWeb
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 *Created by chene on 20-12-19
 */
val collectModule = module {
    single { get<Retrofit>().create(CollectArticleService::class.java) }
    single { get<Retrofit>().create(CollectWebService::class.java) }
    viewModel { CollectArticlePageViewModel(get()) }
    viewModel { CollectWebViewModel(get()) }
    single { (unCollect: (Int, Int) -> Unit) -> CollectArticleAdapter(unCollect) }
    single { (modify: (CollectWeb) -> Unit) -> CollectWebAdapter(modify) }
}