package com.example.module_search.repository

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.module_search.bean.SearchHistory
import com.example.module_search.remote.AuthorSearchService
import com.example.module_search.remote.KeySearchService
import com.example.share_article.bean.Article
import com.example.share_article.bean.PageArticle
import com.example.share_article.remote.QueryArticleService
import com.example.share_article.repository.ArticleRepository
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-8 下午11:07
 **/
class SearchRepository(
    private val keySearchApi: KeySearchService,
    private val authorSearchApi: AuthorSearchService
) : ArticleRepository() {

    private var curSearch = ""
    private var curApi: QueryArticleService = keySearchApi

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<NetPage<Article>> {
        return curApi.getArticles(page, query)
    }

    fun api(tag: Int) {
        curApi = when (tag) {
            SearchHistory.SEARCH_TAG_AUTHOR -> authorSearchApi
            else -> keySearchApi
        }
    }

    suspend fun load() = super.load(curSearch, 0)

    suspend fun refresh(query: String) = super.refresh(0, query.apply {
        over = false
        curSearch = query
    }, 0)
}