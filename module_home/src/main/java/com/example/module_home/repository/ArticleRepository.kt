package com.example.module_home.repository

import com.example.lib_net.NetResult
import com.example.module_home.bean.TopArticle
import com.example.module_home.remote.ArticleService
import com.example.module_home.shouldUpdate
import com.example.share_home_search.bean.Article
import com.example.share_home_search.bean.PageArticle
import com.example.share_home_search.bean.Tag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-3 下午7:24
 **/
class ArticleRepository(
    private val pageArticleDao: PageArticleDao,
    private val topArticleDao: TopArticleDao
) {

    var curPage = 0
    private var over = false

    private val articleApi by inject(ArticleService::class.java)

    @ExperimentalCoroutinesApi
    suspend fun refreshArticles() = channelFlow {
        getTopArticles().zip(getPageArticles(0)) { top, page ->
            when {
                top is NetResult.Failure -> {
                    top
                }
                page is NetResult.Failure -> {
                    page
                }
                else -> {
                    val t = if (top is NetResult.Success) top.value else null
                    val p = if (page is NetResult.Success) page.value else null
                    NetResult.Success(
                        mutableListOf<Article>().apply {
                            if (t != null) addAll(t.data)
                            if (p != null) addAll(p.datas)
                        }.toList()
                    )
                }
            }
        }.collectLatest {
            send(it)
        }
    }

    @ExperimentalCoroutinesApi
    fun loadArticles() = getPageArticles(++curPage)

    @ExperimentalCoroutinesApi
    private fun getPageArticles(page: Int) = flow {
        try {
            if (!over) {
                var data = pageArticleDao.getArticlesByPage(page)
                if (data == null || data.lastTime.shouldUpdate()) {
                    articleApi.getArticlesByPage(page).let {
                        if (it.data == null) {
                            emit(NetResult.Failure(it.errorMsg))
                        } else {
                            it.data?.addTag()?.let { page ->
                                insertArticle(page)
                                data = page
                            }
                        }
                    }
                }
                over = data?.over ?: false
                emit(NetResult.Success(data))
            } else {
                emit(NetResult.Failure("没有更多数据..."))
            }
        } catch (e: Exception) {
            emit(NetResult.Failure(e.message))
        }
    }.flowOn(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    private fun getTopArticles() = flow {
        try {
            var data = topArticleDao.getTopArticle()
            if (data == null || data.lastTime.shouldUpdate()) {
                articleApi.getTopArticle().let {
                    if (it.data != null) {
                        it.data?.let { list ->
                            TopArticle(list).addTag().let { top ->
                                insertArticle(top)
                                data = top
                            }
                        }
                    } else {
                        emit(NetResult.Failure(it.errorMsg))
                    }
                }
            }
            emit(NetResult.Success(data))
        } catch (e: Exception) {
            emit(NetResult.Failure(e.message))
        }
    }.flowOn(Dispatchers.IO)

    private fun insertArticle(article: TopArticle) = CoroutineScope(Dispatchers.IO).launch {
        topArticleDao.insertTopArticle(article)
    }

    private fun insertArticle(article: PageArticle) = CoroutineScope(Dispatchers.IO).launch {
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