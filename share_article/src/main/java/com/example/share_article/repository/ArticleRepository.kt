package com.example.share_article.repository

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.lib_net.repository.PageRepository
import com.example.lib_net.result
import com.example.share_article.bean.Article

/**
Created by chene on @date 20-12-14 上午9:49
 **/
abstract class ArticleRepository : PageRepository() {

    protected abstract suspend fun request(
        page: Int,
        query: String,
        cid: Int
    ): NetBean<NetPage<Article>>

    private suspend fun getArticles(page: Int, query: String, cid: Int) =
        request(page, query, cid).pageRequest()

    protected suspend fun refresh(startPage: Int, query: String, cid: Int) =
        getArticles(let {
            curPage = startPage
            over = false
            curPage
        }, query, cid)

    protected suspend fun load(query: String, cid: Int) = getArticles(curPage, query, cid)
}