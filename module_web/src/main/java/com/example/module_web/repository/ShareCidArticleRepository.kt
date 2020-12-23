package com.example.module_web.repository

import com.example.module_web.remote.PublicSearchService

class ShareCidArticleRepository(
    private val api: PublicSearchService
) {

    suspend fun getRemoteArticles(page: Int, cid: Int, query: String) = api.getArticles(page, cid, query)
}