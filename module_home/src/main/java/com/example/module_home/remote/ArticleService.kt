package com.example.module_home.remote

import com.example.lib_net.bean.NetBean
import com.example.share_article.bean.Article
import com.example.share_article.bean.PageArticle
import retrofit2.http.GET
import retrofit2.http.Path

/**
Created by chene on @date 20-12-3 下午7:16
 **/
interface ArticleService {
    @GET("/article/list/{page}/json")
    suspend fun getArticlesByPage(@Path("page") page: Int): NetBean<PageArticle>

    @GET("/article/top/json")
    suspend fun getTopArticle(): NetBean<List<Article>>
}