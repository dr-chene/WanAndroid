package com.example.module_web.viewmodel

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.module_web.bean.coinInfo
import com.example.module_web.bean.shareArticles
import com.example.module_web.remote.UserShareArticlesService
import com.example.module_web.repository.UserShareArticleRepository
import com.example.share_article.bean.Article
import com.example.share_article.viewmodel.ArticleViewModel
import com.example.share_coin.Coin

/**
 *Created by chene on 20-12-20
 */
class UserShareArticleViewModel(
    private val isMyShare: Boolean,
    private val repository: UserShareArticleRepository
) : ArticleViewModel() {

    var userCoin: NetBean<Coin>? = null

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<NetPage<Article>> {
        return repository.getShareArticle(isMyShare, page, cid).let {
            userCoin = it.coinInfo()
            it.shareArticles()
        }
    }

     fun refresh(id: Int) = super.refresh(if (isMyShare) 1 else 0, "", id)

     fun load(id: Int, curList: MutableList<Article>) = super.load("", id, curList)

    suspend fun delete(id: Int) = repository.deleteMyShare(id)
}