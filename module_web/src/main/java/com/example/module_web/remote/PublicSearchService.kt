package com.example.module_web.remote

import com.example.lib_net.bean.NetBean
import com.example.share_article.bean.PageArticle
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *Created by chene on 20-12-19
 */
interface PublicSearchService {

    @GET("/wxarticle/list/{cid}/{page}/json")
    suspend fun getArticles(
        @Path("page") page: Int,
        @Path("cid") cid: Int,
        @Query("k") query: String
    ): NetBean<PageArticle>
}