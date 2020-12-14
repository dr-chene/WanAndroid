package com.example.module_web

import com.example.module_web.remote.CidServiceImpl
import com.example.module_web.repository.CidArticleRepository
import org.koin.dsl.module
import retrofit2.Retrofit

/**
Created by chene on @date 20-12-14 下午5:13
 **/
val cidModule = module {

    single { (get() as Retrofit).create(CidServiceImpl::class.java) }
    factory { CidArticleRepository() }
}