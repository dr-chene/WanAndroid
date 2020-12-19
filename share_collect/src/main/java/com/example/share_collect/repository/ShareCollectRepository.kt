package com.example.share_collect.repository

import com.example.lib_net.request
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-12 上午10:58
 **/
class ShareCollectRepository {

    private val shareCollectApi by inject(com.example.share_collect.remote.ShareCollectService::class.java)

    suspend fun shareArticle(title: String, link: String) =
        shareCollectApi.shareArticle(title, link).request()

    suspend fun collectArticle(title: String, author: String, link: String) =
        shareCollectApi.collectArticle(title, author, link).request()

    suspend fun collectWeb(name: String, link: String) =
        shareCollectApi.collectWeb(name, link).request()
}