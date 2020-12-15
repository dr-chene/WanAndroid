package com.example.module_nav.remote

import com.example.lib_net.bean.NetBean
import com.example.module_nav.bean.Project
import retrofit2.http.GET

/**
Created by chene on @date 20-12-14 下午9:52
 **/
interface ProjectService {

    @GET("/project/tree/json")
    suspend fun getData(): NetBean<List<Project>>
}