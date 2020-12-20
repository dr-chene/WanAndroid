package com.example.share_article.remote

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.share_article.bean.Article
import com.example.share_article.bean.PageArticle

/**
Created by chene on @date 20-12-14 下午4:57
 **/
interface CidArticleService {

    suspend fun getArticles(page: Int, cid: Int): NetBean<NetPage<Article>>
}