package com.example.module_web.repository

import com.example.module_web.remote.UserShareArticlesService

class UserShareArticleRepository(
    private val api: UserShareArticlesService
) {

    suspend fun getShareArticle(isMyShare: Boolean, page: Int, cid: Int) = if (isMyShare) {
        api.myShare(page)
    } else {
        api.otherShare(page, cid)
    }

    suspend fun deleteMyShare(id: Int) = api.deleteMyShare(id)
}