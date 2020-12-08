package com.example.module_home

import com.example.module_home.adapter.ArticleRecyclerViewAdapter
import com.example.module_home.adapter.MyBannerAdapter
import com.example.module_home.bean.Article
import com.example.module_home.bean.Banner
import com.example.module_home.bean.Tag
import com.example.module_home.remote.ArticleService
import com.example.module_home.remote.BannerService
import com.example.module_home.remote.HotKeyService
import com.example.module_home.repository.ArticleRepository
import com.example.module_home.repository.BannerRepository
import com.example.module_home.repository.HotKeyRepository
import com.example.module_home.viewmodel.ArticleViewModel
import com.example.module_home.viewmodel.BannerViewModel
import com.example.module_home.viewmodel.HotKeyViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

/**
Created by chene on @date 20-12-3 下午6:49
 **/

val homeModule = module {

    single { Article.ArticleDiffCallBack() }
    single { Tag.TagDiffCallBack() }
    single { ArticleRecyclerViewAdapter() }
    single { ArticleRepository(get(), get()) }
    single { HotKeyRepository() }

    single { BannerRepository() }
    single { (data: List<Banner>) -> MyBannerAdapter(data) }

    single { (get() as Retrofit).create(ArticleService::class.java) }
    single { (get() as Retrofit).create(BannerService::class.java) }
    single<HotKeyService> { (get() as Retrofit).create(HotKeyService::class.java) }

    single { Random(System.currentTimeMillis()) }

    viewModel { ArticleViewModel(get()) }
    viewModel { BannerViewModel(get()) }
    viewModel { HotKeyViewModel(get()) }
}