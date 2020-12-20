package com.example.module_todo.remote

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.Page
import com.example.module_todo.bean.Todo
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *Created by chene on 20-12-20
 */
interface TodoService {

    @POST("/lg/todo/add/json")
    suspend fun add(
        @Query("title") title: String,
        @Query("content") content: String,
        @Query("date") date: String? = null,
        @Query("type") type: Int? = null,
        @Query("priority") priority: Int? = null
    ): NetBean<Todo>

    @POST("/lg/todo/update/{id}/json")
    suspend fun modify(
        @Path("id") id: Int,
        @Query("title") title: String,
        @Query("content") content: String,
        @Query("date") date: String,
        @Query("status") status: Int? = null,
        @Query("type") type: Int? = null,
        @Query("priority") priority: Int? = null
    ): NetBean<Todo>

    @POST("/lg/todo/delete/{id}/json")
    suspend fun delete(@Path("id") id: Int): NetBean<Any>

    @POST("/lg/todo/done/{id}/json")
    suspend fun complete(@Path("id") id: Int, @Query("status") status: Int): NetBean<Todo>

    @GET("/lg/todo/v2/list/{page}/json")
    suspend fun list(
        @Path("page") page: Int,
        @Query("status") status: Int? = null,
        @Query("type") type: Int? = null,
        @Query("priority") priority: Int? = null,
        @Query("orderby") orderby: Int = 4
    ): NetBean<Page<Todo>>
}