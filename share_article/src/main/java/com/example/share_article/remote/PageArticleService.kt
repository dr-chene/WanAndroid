package com.example.share_article.remote

import com.example.lib_net.bean.NetBean
import com.example.share_article.bean.PageArticle

/**
Created by chene on @date 20-12-14 上午10:00
 **/
interface PageArticleService {

    suspend fun getArticles(page: Int): NetBean<PageArticle>
}