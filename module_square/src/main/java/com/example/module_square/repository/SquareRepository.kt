package com.example.module_square.repository

import com.example.lib_net.bean.NetBean
import com.example.module_square.SquareFragment
import com.example.module_square.remote.QaService
import com.example.module_square.remote.SquareService
import com.example.share_article.bean.PageArticle
import com.example.share_article.repository.ArticleRepository
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-14 上午9:28
 **/
class SquareRepository(
    private val fragmentType: Int
) : ArticleRepository() {

    private val squareApi by inject(SquareService::class.java)
    private val qaApi by inject(QaService::class.java)

    private fun api() = when (fragmentType) {
        SquareFragment.FRAGMENT_TYPE_SQUARE -> squareApi
        else -> qaApi
    }

    fun refresh() = super.refresh("", 0)

    fun load() = super.load("", 0)

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<PageArticle> =
        api().getArticles(page)
}