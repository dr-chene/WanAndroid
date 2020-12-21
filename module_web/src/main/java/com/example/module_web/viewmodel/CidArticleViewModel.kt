package com.example.module_web.viewmodel

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.module_web.remote.ArticleCidService
import com.example.share_article.bean.Article
import com.example.share_article.remote.CidArticleService
import com.example.share_article.viewmodel.ArticleViewModel

/**
Created by chene on @date 20-12-14 下午5:11
 **/
class CidArticleViewModel(
    private val cidApi: CidArticleService
) : ArticleViewModel() {

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<NetPage<Article>> {
        return cidApi.getArticles(page, cid)
    }

    fun refresh(cid: Int) = if (cidApi is ArticleCidService) {
            super.refresh(0, "", cid)
        } else {
            super.refresh(1, "", cid)
        }

    fun load(cid: Int, curList: MutableList<Article>) = super.load("", cid, curList)
}