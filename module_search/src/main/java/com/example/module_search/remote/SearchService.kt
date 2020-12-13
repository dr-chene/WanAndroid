package com.example.module_search.remote

import com.example.lib_net.bean.NetBean
import com.example.share_article.bean.PageArticle
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
Created by chene on @date 20-12-8 下午11:00
 **/
interface SearchService {

    @POST("/article/query/{page}/json")
    suspend fun getSearchByKey(
        @Path("page") page: Int,
        @Query("k") content: String
    ): NetBean<PageArticle>

    @GET("/article/list/{page}/json")
    suspend fun getSearchByAuthor(
        @Path("page") page: Int,
        @Query("author") author: String
    ): NetBean<PageArticle>
}