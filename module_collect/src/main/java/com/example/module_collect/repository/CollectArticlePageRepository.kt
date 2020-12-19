package com.example.module_collect.repository

import com.example.lib_net.bean.NetBean
import com.example.lib_net.request
import com.example.module_collect.remote.CollectArticleService
import com.example.share_article.bean.PageArticle
import com.example.share_article.repository.ArticleRepository

/**
 *Created by chene on 20-12-19
 */
class CollectArticlePageRepository(
    private val api: CollectArticleService
): ArticleRepository() {

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<PageArticle> {
        return api.getArticles(page)
    }

    fun refresh() = super.refresh(0, "", 0)

    fun load() = super.load("", 0)
}