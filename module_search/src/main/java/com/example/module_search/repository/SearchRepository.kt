package com.example.module_search.repository

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.module_search.bean.SearchHistory
import com.example.module_search.remote.AuthorSearchService
import com.example.module_search.remote.KeySearchService
import com.example.share_article.bean.Article
import com.example.share_article.remote.QueryArticleService

class SearchRepository(
    private val keySearchApi: KeySearchService,
    private val authorSearchApi: AuthorSearchService
) {
     var curSearch = ""
     var curApi: QueryArticleService = keySearchApi
    fun api(tag: Int) {
        curApi = when (tag) {
            SearchHistory.SEARCH_TAG_AUTHOR -> authorSearchApi
            else -> keySearchApi
        }
    }
}