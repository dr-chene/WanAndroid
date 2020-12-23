package com.example.module_web.viewmodel

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.module_web.remote.PublicSearchService
import com.example.module_web.repository.ShareCidArticleRepository
import com.example.share_article.bean.Article
import com.example.share_article.viewmodel.ArticleViewModel

/**
 *Created by chene on 20-12-19
 */
class SearchCidArticleViewModel(
private val repository: ShareCidArticleRepository
) : ArticleViewModel() {

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<NetPage<Article>> {
        return repository.getRemoteArticles(page, cid, query)
    }

    fun refresh(query: String, cid: Int) = super.refresh(0, query, cid)

    fun load(cid: Int, query: String, curList: MutableList<Article>) = super.load(query, cid, curList)
}