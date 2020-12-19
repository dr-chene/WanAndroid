package com.example.share_collect.repository

import com.example.share_collect.remote.UnCollectArticleService

/**
 *Created by chene on 20-12-19
 */
class ArticleUnCollectRepository(
    private val api: UnCollectArticleService
) {

    suspend fun unCollect(id: Int, originId: Int) =
        if (originId != -1) api.unCollect(originId) else api.unCollectOnMe(id, originId)
}