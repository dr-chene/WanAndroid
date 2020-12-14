package com.example.module_web.repository

import com.example.lib_net.bean.NetBean
import com.example.module_web.remote.CidServiceImpl
import com.example.share_article.bean.PageArticle
import com.example.share_article.repository.ArticleRepository
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-14 下午5:11
 **/
class CidArticleRepository : ArticleRepository() {

    private val cidApi by inject(CidServiceImpl::class.java)

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<PageArticle> {
        return cidApi.getArticles(page, cid)
    }

    fun refresh(cid: Int) = super.refresh("", cid)

    fun load(cid: Int) = super.load("", cid)
}