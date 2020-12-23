package com.example.share_collect.viewmodel

import com.example.share_collect.remote.UnCollectArticleService
import com.example.share_collect.repository.UnCollectArticleRepository

/**
 *Created by chene on 20-12-19
 */
class UnCollectArticleViewModel(
    private val repository: UnCollectArticleRepository
) {

    suspend fun unCollect(id: Int, originId: Int) = repository.unCollect(id, originId)

}