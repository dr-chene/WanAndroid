package com.example.module_nav.repository

import com.example.module_nav.remote.NavService
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-12 下午4:27
 **/
class NavRepository internal constructor(
    private val navDao: NavDao
) {
    private val navApi by inject(NavService::class.java)

    fun local() = navDao.getNavs()

    suspend fun refresh(){
        navApi.getNavs().data?.forEach {
            navDao.insertNav(it)
        }
    }
}