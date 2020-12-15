package com.example.module_search.repository

import com.example.lib_net.bean.NetBean
import com.example.module_search.bean.SearchHistory
import com.example.module_search.remote.AuthorSearchService
import com.example.module_search.remote.KeySearchService
import com.example.share_article.bean.PageArticle
import com.example.share_article.repository.ArticleRepository
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-8 下午11:07
 **/
class SearchRepository(
    private val tag: Int
) : ArticleRepository() {

    private val keySearchApi by inject(KeySearchService::class.java)
    private val authorSearchApi by inject(AuthorSearchService::class.java)

    private var curSearch = ""

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<PageArticle> {
        return api().getArticles(page, query)
    }

    private fun api() = when (tag) {
        SearchHistory.SEARCH_TAG_AUTHOR -> authorSearchApi
        else -> keySearchApi
    }

    fun load() = super.load(curSearch, 0)

    fun refresh(query: String) = super.refresh(0, query.apply {
        over = false
        curSearch = query
    }, 0)
}