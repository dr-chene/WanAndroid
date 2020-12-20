package com.example.module_web.bean

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.share_article.bean.Article
import com.example.share_article.bean.PageArticle
import com.example.share_coin.Coin

/**
 *Created by chene on 20-12-20
 */
data class UserShareArticle(
    val coinInfo: Coin,
    val shareArticles: NetPage<Article>
)

fun NetBean<UserShareArticle>.coinInfo() = NetBean(this.data?.coinInfo, this.errorCode, this.errorMsg)

fun NetBean<UserShareArticle>.shareArticles() =
    NetBean(this.data?.shareArticles, this.errorCode, this.errorMsg)