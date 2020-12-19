package com.example.share_collect

import com.example.share_collect.bean.CollectWeb
import com.example.share_collect.remote.ModifyCollectWebService
import com.example.share_collect.remote.ShareCollectService
import com.example.share_collect.remote.UnCollectArticleService
import com.example.share_collect.repository.ArticleUnCollectRepository
import com.example.share_collect.repository.ShareCollectRepository
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 *Created by chene on 20-12-19
 */
val shareCollectModule = module {
    single { (get() as Retrofit).create(ShareCollectService::class.java) }
    single { ShareCollectRepository() }
    single { get<Retrofit>().create(ModifyCollectWebService::class.java) }
    single { CollectWeb.CollectWebDiffCallBack() }
    single { get<Retrofit>().create(UnCollectArticleService::class.java) }
    single { ArticleUnCollectRepository(get()) }
}