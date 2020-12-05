package com.example.module_home.repository

import android.accounts.NetworkErrorException
import android.content.Context
import android.widget.Toast
import com.example.module_home.bean.*
import com.example.module_home.remote.ArticleService
import com.example.module_home.shouldUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.java.KoinJavaComponent.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.coroutines.resumeWithException

/**
Created by chene on @date 20-12-3 下午7:24
 **/
class ArticleRepository(
    private val pageArticleDao: PageArticleDao,
    private val topArticleDao: TopArticleDao
) {

    var curPage = 0

    @ExperimentalCoroutinesApi
    suspend fun refreshArticles() = flow {
        try {
            getTopArticles().zip(getPageArticles(0)) { top, page ->
                mutableListOf<Article>().apply {
                    addAll(top)
                    addAll(page)
                }
            }.collect {
                emit(it.toList())
            }
        } catch (e: Throwable) {
            throw e
        }
    }

    @ExperimentalCoroutinesApi
    fun loadArticles() = try {
        getPageArticles(++curPage)
    } catch (e: Throwable) {
        throw e
    }

    @ExperimentalCoroutinesApi
    private fun getPageArticles(page: Int) = flow {
        pageArticleDao.getArticlesByPage(page).collect {
            if (it == null || it.datas.isNullOrEmpty() || it.lastTime.shouldUpdate()) {
                try {
                    netPageArticle(page).collect { net ->
                        emit(net.datas)
                    }
                } catch (e: Throwable) {
                    throw e
                }
            } else {
                emit(it.datas)
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun getTopArticles() = flow {
        topArticleDao.getTopArticle().collect {
            if (it == null || it.lastTime.shouldUpdate()) {
                try {
                    netTopArticle().collect { net ->
                        topArticleDao.insertTopArticle(net)
                        emit(net.data)
                    }
                } catch (e: Throwable) {
                    throw e
                }
            } else {
                emit(it.data)
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun netTopArticle() = flow<TopArticle> {
        emit(
            suspendCancellableCoroutine { continuation ->
                get(Retrofit::class.java).create(ArticleService::class.java).getTopArticle()
                    .enqueue(object : Callback<TopArticle> {
                        override fun onResponse(
                            call: Call<TopArticle>,
                            response: Response<TopArticle>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                response.body()?.let {
                                    //添加“置顶”、“new”标签
                                    continuation.resume(it.apply {
                                        data.forEach { article ->
                                            article.tags = mutableListOf<Tag>().apply {
                                                addAll(article.tags)
                                                add(Tag("置顶", ""))
                                                if (article.fresh) add(Tag("new", ""))
                                            }.toList()
                                        }
                                    }) { e ->
                                        continuation.resumeWithException(e)
                                    }
                                }
                            } else {
                                continuation.resumeWithException(NetworkErrorException())
                            }
                        }

                        override fun onFailure(call: Call<TopArticle>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }
                    })
            }
        )
    }

    @ExperimentalCoroutinesApi
    private fun netPageArticle(page: Int) = flow<PageArticle> {
        emit(
            suspendCancellableCoroutine { continuation ->
                get(Retrofit::class.java).create(ArticleService::class.java).getArticlesByPage(page)
                    .enqueue(object : Callback<NetPageArticle> {
                        override fun onResponse(
                            call: Call<NetPageArticle>,
                            response: Response<NetPageArticle>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                response.body()?.data?.let {
                                    curPage = it.curPage
                                    //添加“new”标签
                                    continuation.resume(it.apply {
                                        datas.forEach { article ->
                                            article.tags = mutableListOf<Tag>().apply {
                                                addAll(article.tags)
                                                if (article.fresh) add(Tag("new", ""))
                                            }.toList()
                                        }
                                    }) { e ->
                                        continuation.resumeWithException(e)
                                    }
                                }
                            } else {
                                continuation.resumeWithException(NetworkErrorException())
                            }
                        }

                        override fun onFailure(call: Call<NetPageArticle>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }
                    })
            }
        )

    }
}