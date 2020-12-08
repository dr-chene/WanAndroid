package com.example.module_home.remote

import com.example.module_home.bean.NetPageArticle
import com.example.module_home.bean.TopArticle
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
Created by chene on @date 20-12-3 下午7:16
 **/
interface ArticleService {
    @GET("/article/list/{page}/json")
    suspend fun getArticlesByPage(@Path("page") page: Int): NetPageArticle?

    @GET("/article/top/json")
    suspend fun getTopArticle(): TopArticle?
}