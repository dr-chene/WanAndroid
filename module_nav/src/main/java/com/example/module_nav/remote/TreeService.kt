package com.example.module_nav.remote

import com.example.lib_net.bean.NetBean
import com.example.module_nav.bean.Tree
import com.example.share_article.bean.PageArticle
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
Created by chene on @date 20-12-12 下午8:33
 **/
interface TreeService {

    @GET("/tree/json")
    suspend fun getTree(): NetBean<List<Tree>>

    @GET("/article/list/{page}/json")
    suspend fun getTreeArticle(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): NetBean<PageArticle>
}