package com.example.module_home

import com.example.module_home.adapter.ArticleRecyclerViewAdapter
import com.example.module_home.adapter.ArticleTagsRecyclerViewAdapter
import com.example.module_home.bean.Article
import com.example.module_home.bean.Tag
import com.example.module_home.repository.ArticleRepository
import com.example.module_home.viewmodel.ArticleViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
Created by chene on @date 20-12-3 下午6:49
 **/
val homeModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { Article.ArticleDiffCallBack() }
    single { Tag.TagDiffCallBack() }
    single { ArticleTagsRecyclerViewAdapter() }
    single { ArticleRecyclerViewAdapter() }
    single { ArticleRepository(get(), get()) }

    viewModel { ArticleViewModel(get()) }
}