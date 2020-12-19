package com.example.module_web.remote

import com.example.lib_net.bean.NetBean
import com.example.module_web.bean.UserShareArticle
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *Created by chene on 20-12-20
 */
interface UserShareArticlesService {

    @GET("/user/{id}/share_articles/{page}/json")
    suspend fun getArticles(@Path("page") page: Int, @Path("id") id: Int): NetBean<UserShareArticle>
}