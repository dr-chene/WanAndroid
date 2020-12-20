
package com.example.module_web

import com.example.module_web.remote.*
import com.example.module_web.repository.CidArticleRepository
import com.example.module_web.repository.SearchCidArticleRepository
import com.example.module_web.repository.UserShareArticleRepository
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
    single { get<Retrofit>().create(PublicSearchService::class.java) }
    factory { (api: CidArticleService) -> CidArticleRepository(api) }
    factory { SearchCidArticleRepository(get()) }
    single { get<Retrofit>().create(UserShareArticlesService::class.java) }
    factory { (isMyShare: Boolean) -> UserShareArticleRepository(get(), isMyShare) }
}