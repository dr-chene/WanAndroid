package com.example.module_square.remote

import com.example.lib_net.bean.NetBean
import com.example.share_article.bean.PageArticle
import com.example.share_article.remote.PageArticleService
import retrofit2.http.GET
import retrofit2.http.Path

/**
Created by chene on @date 20-12-14 上午10:15
 **/
interface QaService : PageArticleService {

    @GET("/wenda/list/{page}/json")
    override suspend fun getArticles(@Path("page") page: Int): NetBean<PageArticle>
}