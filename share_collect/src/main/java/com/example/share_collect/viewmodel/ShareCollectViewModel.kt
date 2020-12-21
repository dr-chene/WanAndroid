package com.example.share_collect.viewmodel

import com.example.lib_net.request
import com.example.share_collect.remote.ShareCollectService

/**
Created by chene on @date 20-12-12 上午10:58
 **/
class ShareCollectViewModel(
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