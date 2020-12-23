
package com.example.module_web

import com.example.module_web.remote.*
import com.example.module_web.repository.CidArticleRepository
import com.example.module_web.repository.ShareCidArticleRepository
import com.example.module_web.repository.UserShareArticleRepository
import com.example.module_web.viewmodel.CidArticleViewModel
import com.example.module_web.viewmodel.SearchCidArticleViewModel
import com.example.module_web.viewmodel.UserShareArticleViewModel
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
    single { ShareCidArticleRepository(get()) }
    factory { (api: CidArticleService) -> CidArticleRepository(api) }
    factory { (repository: CidArticleRepository) -> CidArticleViewModel(repository) }
    factory { SearchCidArticleViewModel(get()) }
    single { get<Retrofit>().create(UserShareArticlesService::class.java) }
    factory { (isMyShare: Boolean) -> UserShareArticleViewModel(isMyShare, get()) }
    single { UserShareArticleRepository(get()) }
}