package com.example.share_home_search

import com.example.share_home_search.adapter.ArticleRecyclerViewAdapter
import org.koin.dsl.module

/**
Created by chene on @date 20-12-8 下午9:01
 **/
val shareHomeSearchModule = module {
    factory { (isHome: Boolean) -> ArticleRecyclerViewAdapter(isHome) }
}