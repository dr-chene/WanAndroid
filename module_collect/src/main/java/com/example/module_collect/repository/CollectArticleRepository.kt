package com.example.module_collect.repository

import com.example.module_collect.remote.CollectArticleService

class CollectArticleRepository(
    private val api: CollectArticleService
) {
    suspend fun getRemoteArticles(page: Int) = api.getArticles(page)
}