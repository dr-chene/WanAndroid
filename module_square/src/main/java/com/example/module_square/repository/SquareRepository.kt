package com.example.module_square.repository

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.module_square.SquareFragment
import com.example.module_square.remote.QaService
import com.example.module_square.remote.SquareService
import com.example.share_article.bean.Article
import com.example.share_article.repository.ArticleRepository

/**
Created by chene on @date 20-12-14 上午9:28
 **/
class SquareRepository(
    private val fragmentType: Int,
    private val squareApi: SquareService,
    private val qaApi: QaService
) : ArticleRepository() {

    private fun api() = when (fragmentType) {
        SquareFragment.FRAGMENT_TYPE_SQUARE -> squareApi
        else -> qaApi
    }

    suspend fun refresh() = super.refresh(0, "", 0)

    suspend fun load() = super.load("", 0)

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<NetPage<Article>> =
        api().getArticles(page)
}