package com.example.module_web.repository

import com.example.module_web.remote.ArticleCidService
import com.example.share_article.remote.CidArticleService

class CidArticleRepository(
    private val api: CidArticleService
) {
    fun isArticle() = api is ArticleCidService

    suspend fun getRemoteArticle(page: Int, cid: Int) = api.getArticles(page, cid)
}