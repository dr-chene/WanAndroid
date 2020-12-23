package com.example.share_collect.repository

import com.example.share_collect.remote.UnCollectArticleService

class UnCollectArticleRepository(
    private val api: UnCollectArticleService
) {

    suspend fun unCollect(id: Int, originId: Int) =
        if (originId != -1) api.unCollect(originId) else api.unCollectOnMe(id, originId)
}