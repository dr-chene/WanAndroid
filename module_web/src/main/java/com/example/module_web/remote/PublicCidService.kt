package com.example.module_web.remote

import com.example.lib_net.bean.NetBean
import com.example.share_article.bean.PageArticle
import com.example.share_article.remote.CidArticleService
import retrofit2.http.GET
import retrofit2.http.Path

/**
Created by chene on @date 20-12-15 下午7:06
 **/
interface PublicCidService : CidArticleService {

    @GET("/wxarticle/list/{cid}/{page}/json")
    override suspend fun getArticles(
        @Path("page") page: Int,
        @Path("cid") cid: Int
    ): NetBean<PageArticle>
}