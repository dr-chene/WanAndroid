package com.example.module_search.remote

import com.example.lib_net.bean.NetBean
import com.example.share_article.bean.PageArticle
import com.example.share_article.remote.QueryArticleService
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
Created by chene on @date 20-12-8 下午11:00
 **/
interface KeySearchService : QueryArticleService {

    @POST("/article/query/{page}/json")
    override suspend fun getArticles(
        @Path("page") page: Int,
        @Query("k") query: String
    ): NetBean<PageArticle>
}