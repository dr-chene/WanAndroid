package com.example.module_web.remote

import com.example.lib_net.bean.NetBean
import com.example.share_article.bean.PageArticle
import com.example.share_article.remote.CidArticleService
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
Created by chene on @date 20-12-14 下午5:01
 **/
interface ArticleCidService : CidArticleService {

    @GET("/article/list/{page}/json")
    override suspend fun getArticles(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): NetBean<PageArticle>
}