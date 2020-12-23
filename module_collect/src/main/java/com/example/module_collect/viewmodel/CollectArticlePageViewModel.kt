package com.example.module_collect.viewmodel

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.module_collect.remote.CollectArticleService
import com.example.module_collect.repository.CollectArticleRepository
import com.example.share_article.bean.Article
import com.example.share_article.viewmodel.ArticleViewModel

/**
 *Created by chene on 20-12-19
 */
class CollectArticlePageViewModel(
    private val repository: CollectArticleRepository
): ArticleViewModel() {

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<NetPage<Article>> {
        return repository.getRemoteArticles(page)
    }

    fun refresh() = super.refresh(0, "", 0)

    fun load(curList: MutableList<Article>) = super.load("", 0, curList)
}