package com.example.module_search.remote

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.share_article.bean.Article
import com.example.share_article.bean.PageArticle
import com.example.share_article.remote.QueryArticleService
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
Created by chene on @date 20-12-14 下午4:21
 **/
interface AuthorSearchService : QueryArticleService {

    @GET("/article/list/{page}/json")
    override suspend fun getArticles(
        @Path("page") page: Int,
        @Query("author") query: String
    ): NetBean<NetPage<Article>>
}