package com.example.module_web

import com.example.module_web.remote.ArticleCidService
import com.example.module_web.remote.ProjectCidService
import com.example.module_web.remote.PublicCidService
import com.example.module_web.repository.CidArticleRepository
import com.example.share_article.remote.CidArticleService
import org.koin.dsl.module
import retrofit2.Retrofit

/**
Created by chene on @date 20-12-14 下午5:13
 **/
val cidModule = module {

    single { get<Retrofit>().create(ArticleCidService::class.java) }
    single { get<Retrofit>().create(ProjectCidService::class.java) }
    single { get<Retrofit>().create(PublicCidService::class.java) }
    factory { (api: CidArticleService) -> CidArticleRepository(api) }
}