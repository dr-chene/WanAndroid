package com.example.module_home.repository

import android.content.Context
import android.widget.Toast
import com.example.module_home.bean.Article
import com.example.module_home.bean.NetPageArticle
import com.example.module_home.bean.PageArticle
import com.example.module_home.bean.TopArticle
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
        getTopArticles().zip(getPageArticles(0)) { top, page ->
            mutableListOf<Article>().apply {
                addAll(top)
                addAll(page)
            }
        }.collect {
            emit(it.toList())
        }
    }

    fun loadArticles() = getPageArticles(++curPage)

    @ExperimentalCoroutinesApi
    private fun getPageArticles(page: Int) = flow {
        pageArticleDao.getArticlesByPage(page).collect {
            if (it == null || it.datas.isNullOrEmpty() || it.lastTime.shouldUpdate()) {
                netPageArticle(page).collect { net ->
                    emit(net.datas)
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
                netTopArticle().collect { net ->
                    topArticleDao.insertTopArticle(net)
                    emit(net.data)
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
                                    continuation.resume(it) { e ->
                                        e.printStackTrace()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    get(Context::class.java),
                                    "网络数据获取失败",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<TopArticle>, t: Throwable) {
                            Toast.makeText(get(Context::class.java), "网络连接失败", Toast.LENGTH_SHORT)
                                .show()
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
                                    continuation.resume(it) { e ->
                                        e.printStackTrace()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    get(Context::class.java),
                                    "网络数据获取失败",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<NetPageArticle>, t: Throwable) {
                            Toast.makeText(get(Context::class.java), "网络数据获取失败", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
        )

    }
}