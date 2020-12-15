package com.example.module_web.repository

import com.example.lib_net.bean.NetBean
import com.example.module_web.remote.ArticleCidService
import com.example.share_article.bean.PageArticle
import com.example.share_article.remote.CidArticleService
import com.example.share_article.repository.ArticleRepository

/**
Created by chene on @date 20-12-14 下午5:11
 **/
class CidArticleRepository(
    private val cidApi: CidArticleService
) : ArticleRepository() {

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<PageArticle> {
        return cidApi.getArticles(page, cid)
    }

    fun refresh(cid: Int) = if (cidApi is ArticleCidService) {
        super.refresh(0, "", cid)
    } else super.refresh(1, "", cid)

    fun load(cid: Int) = super.load("", cid)
}