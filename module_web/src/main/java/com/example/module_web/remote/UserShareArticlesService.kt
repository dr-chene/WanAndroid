package com.example.module_web.remote

import com.example.lib_net.bean.NetBean
import com.example.module_web.bean.UserShareArticle
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 *Created by chene on 20-12-20
 */
interface UserShareArticlesService {

    @GET("/user/{id}/share_articles/{page}/json")
    suspend fun otherShare(@Path("page") page: Int, @Path("id") id: Int): NetBean<UserShareArticle>

    @GET("/user/lg/private_articles/{page}/json")
    suspend fun myShare(@Path("page") page: Int): NetBean<UserShareArticle>

    @POST("/lg/user_article/delete/{id}/json")
    suspend fun deleteMyShare(@Path("id") id: Int): NetBean<Any>
}