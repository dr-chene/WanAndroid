package com.example.module_web.repository

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.module_web.remote.ArticleCidService
import com.example.module_web.remote.PublicSearchService
import com.example.share_article.bean.Article
import com.example.share_article.bean.PageArticle
import com.example.share_article.repository.ArticleRepository

/**
 *Created by chene on 20-12-19
 */
class SearchCidArticleRepository(
    private val api: PublicSearchService
) : ArticleRepository() {
    override suspend fun request(page: Int, query: String, cid: Int): NetBean<NetPage<Article>> {
        return api.getArticles(page, cid, query)
    }

    suspend fun refresh(query: String, cid: Int) = super.refresh(0, query, cid)

    suspend fun load(cid: Int, query: String) = super.load(query, cid)
}