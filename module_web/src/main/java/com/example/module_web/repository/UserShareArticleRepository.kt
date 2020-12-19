package com.example.module_web.repository

import com.example.lib_net.bean.NetBean
import com.example.module_web.bean.coinInfo
import com.example.module_web.bean.shareArticles
import com.example.module_web.remote.UserShareArticlesService
import com.example.share_article.bean.PageArticle
import com.example.share_article.repository.ArticleRepository
import com.example.share_coin.Coin

/**
 *Created by chene on 20-12-20
 */
class UserShareArticleRepository(
    private val api: UserShareArticlesService
) : ArticleRepository(){

    var userCoin: NetBean<Coin>? = null

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<PageArticle> {
        return api.getArticles(page, cid).let {
            userCoin = it.coinInfo()
            it.shareArticles()
        }
    }

    fun refresh(id: Int) = super.refresh(0, "", id)

    fun load(id: Int) = super.load("", id)
}