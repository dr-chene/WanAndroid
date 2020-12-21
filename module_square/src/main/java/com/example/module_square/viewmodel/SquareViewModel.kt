package com.example.module_square.viewmodel

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.module_square.SquareFragment
import com.example.module_square.remote.QaService
import com.example.module_square.remote.SquareService
import com.example.share_article.bean.Article
import com.example.share_article.viewmodel.ArticleViewModel

/**
Created by chene on @date 20-12-14 上午9:28
 **/
class SquareViewModel(
    private val fragmentType: Int,
    private val squareApi: SquareService,
    private val qaApi: QaService
) : ArticleViewModel() {

    private fun api() = when (fragmentType) {
        SquareFragment.FRAGMENT_TYPE_SQUARE -> squareApi
        else -> qaApi
    }

    fun refresh() = super.refresh(0, "", 0)

    fun load(curList: MutableList<Article>) = super.load("", 0, curList)

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<NetPage<Article>> =
        api().getArticles(page)
}