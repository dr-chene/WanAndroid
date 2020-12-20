package com.example.module_web.remote

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.share_article.bean.Article
import com.example.share_article.bean.PageArticle
import com.example.share_article.remote.CidArticleService
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
Created by chene on @date 20-12-15 下午6:53
 **/
interface ProjectCidService : CidArticleService {

    @GET("/project/list/{page}/json")
    override suspend fun getArticles(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): NetBean<NetPage<Article>>
}