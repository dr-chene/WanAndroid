package com.example.module_collect.repository

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.lib_net.result
import com.example.module_collect.remote.CollectArticleService
import com.example.share_article.bean.Article
import com.example.share_article.bean.PageArticle
import com.example.share_article.repository.ArticleRepository

/**
 *Created by chene on 20-12-19
 */
class CollectArticlePageRepository(
    private val api: CollectArticleService
): ArticleRepository() {

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<NetPage<Article>> {
        return api.getArticles(page)
    }

    suspend fun refresh() = super.refresh(0, "", 0)

    suspend fun load() = super.load("", 0)
}