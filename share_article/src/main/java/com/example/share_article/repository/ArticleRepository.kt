package com.example.share_article.repository

import com.example.lib_net.bean.NetBean
import com.example.lib_net.repository.PageRepository
import com.example.lib_net.request
import com.example.share_article.bean.PageArticle

/**
Created by chene on @date 20-12-14 上午9:49
 **/
abstract class ArticleRepository: PageRepository() {

    private suspend fun getArticles(page: Int, query: String, cid: Int) = request(page, query, cid).request()

    protected abstract suspend fun request(
        page: Int,
        query: String,
        cid: Int
    ): NetBean<PageArticle>

    protected fun refresh(startPage: Int, query: String, cid: Int) =
        getArticles(let {
            curPage = startPage
            over = false
            curPage }, query, cid)

    protected fun load(query: String, cid: Int) = getArticles(curPage, query, cid)
}