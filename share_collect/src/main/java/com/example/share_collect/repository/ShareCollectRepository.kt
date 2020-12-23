package com.example.share_collect.repository

import com.example.lib_net.request
import com.example.share_collect.remote.ShareCollectService

class ShareCollectRepository(
    private val api: ShareCollectService
) {
    suspend fun shareArticle(title: String, link: String) =
        api.shareArticle(title, link).request()

    suspend fun collectArticle(title: String, author: String, link: String) =
        api.collectArticle(title, author, link).request()

    suspend fun collectWeb(name: String, link: String) =
        api.collectWeb(name, link).request()

    suspend fun collectInnerArticle(id: Int) =
        api.collectInnerArticle(id).request()
}