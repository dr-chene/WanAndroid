package com.example.module_home.repository

import com.example.lib_net.response
import com.example.module_home.bean.Article
import com.example.module_home.bean.PageArticle
import com.example.module_home.bean.Tag
import com.example.module_home.bean.TopArticle
import com.example.module_home.remote.ArticleService
import com.example.module_home.shouldUpdate
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-3 下午7:24
 **/
class ArticleRepository(
    private val pageArticleDao: PageArticleDao,
    private val topArticleDao: TopArticleDao
) {

    var curPage = 0

    private val articleApi by inject(ArticleService::class.java)

    @ExperimentalCoroutinesApi
    suspend fun refreshArticles(netError: () -> Unit) = channelFlow {
        getTopArticles(netError).zip(getPageArticles(0, netError)) { top, page ->
            mutableListOf<Article>().apply {
                addAll(top.data)
                addAll(page.datas)
            }
        }.collectLatest {
            send(it)
        }
    }

    @ExperimentalCoroutinesApi
    fun loadArticles(netError: () -> Unit) = getPageArticles(++curPage, netError)

    @ExperimentalCoroutinesApi
    private fun getPageArticles(page: Int, netError: () -> Unit) = flow {
        val local = pageArticleDao.getArticlesByPage(page)
        if (local == null || local.lastTime.shouldUpdate()) {
            articleApi.getArticlesByPage(page)?.let {
                response(it, netError).data.addTag().let { pageArticle ->
                    emit(pageArticle)
                    insertPageArticle(pageArticle)
                }
            }
        } else {
            emit(local)
        }
    }.flowOn(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    private fun getTopArticles(netError: () -> Unit) = flow {
        val local = topArticleDao.getTopArticle()
        if (local == null || local.lastTime.shouldUpdate()) {
            articleApi.getTopArticle()?.let {
                response(it, netError).addTag().let { topArticle ->
                    emit(topArticle)
                    insertTopArticle(topArticle)
                }
            }
        } else {
            emit(local)
        }
    }.flowOn(Dispatchers.IO)

    private fun insertTopArticle(article: TopArticle) = CoroutineScope(Dispatchers.IO).launch {
        topArticleDao.insertTopArticle(article)
    }

    private fun insertPageArticle(article: PageArticle) = CoroutineScope(Dispatchers.IO).launch {
        pageArticleDao.insertArticles(article)
    }

    //添加“new”,“置顶”标签
    private fun TopArticle.addTag() = run {
        this.apply {
            data.forEach { article ->
                article.tags = mutableListOf<Tag>().apply {
                    addAll(article.tags)
                    add(Tag("置顶", ""))
                    if (article.fresh) add(Tag("new", ""))
                }.toList()
            }
        }
    }

    //添加“new”标签
    private fun PageArticle.addTag() = run {
        this.apply {
            datas.forEach { article ->
                article.tags = mutableListOf<Tag>().apply {
                    addAll(article.tags)
                    if (article.fresh) add(Tag("new", ""))
                }.toList()
            }
        }
    }
}