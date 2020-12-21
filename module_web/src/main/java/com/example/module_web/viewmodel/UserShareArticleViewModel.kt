package com.example.module_web.viewmodel

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.module_web.bean.coinInfo
import com.example.module_web.bean.shareArticles
import com.example.module_web.remote.UserShareArticlesService
import com.example.share_article.bean.Article
import com.example.share_article.viewmodel.ArticleViewModel
import com.example.share_coin.Coin

/**
 *Created by chene on 20-12-20
 */
class UserShareArticleViewModel(
    private val api: UserShareArticlesService,
    private val isMyShare: Boolean
) : ArticleViewModel() {

    var userCoin: NetBean<Coin>? = null

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<NetPage<Article>> {
        return if (isMyShare) {
            api.myShare(page)
        } else {
            api.otherShare(page, cid)
        }.let {
            userCoin = it.coinInfo()
            it.shareArticles()
        }
    }

     fun refresh(id: Int) = super.refresh(if (isMyShare) 1 else 0, "", id)

     fun load(id: Int, curList: MutableList<Article>) = super.load("", id, curList)

    suspend fun delete(id: Int) = api.deleteMyShare(id)
}