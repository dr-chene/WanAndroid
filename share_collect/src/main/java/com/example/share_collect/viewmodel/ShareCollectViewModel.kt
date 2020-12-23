package com.example.share_collect.viewmodel

import com.example.lib_net.request
import com.example.share_collect.remote.ShareCollectService
import com.example.share_collect.repository.ShareCollectRepository

/**
Created by chene on @date 20-12-12 上午10:58
 **/
class ShareCollectViewModel(
    private val repository: ShareCollectRepository
) {
    suspend fun shareArticle(title: String, link: String) =
        repository.shareArticle(title, link)

    suspend fun collectArticle(title: String, author: String, link: String) =
        repository.collectArticle(title, author, link)

    suspend fun collectWeb(name: String, link: String) =
        repository.collectWeb(name, link)

    suspend fun collectInnerArticle(id: Int) =
        repository.collectInnerArticle(id)
}