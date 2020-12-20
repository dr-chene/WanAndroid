package com.example.module_collect.remote

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.share_article.bean.Article
import com.example.share_article.bean.PageArticle
import com.example.share_article.remote.PageArticleService
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *Created by chene on 20-12-19
 */
interface CollectArticleService: PageArticleService {

    @GET("lg/collect/list/{page}/json")
    override suspend fun getArticles(@Path("page")page: Int): NetBean<NetPage<Article>>
}