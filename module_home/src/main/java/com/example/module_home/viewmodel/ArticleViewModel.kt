package com.example.module_home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lib_net.bean.NetResult
import com.example.module_home.repository.ArticleRepository
import com.example.share_article.bean.Article
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

/**
Created by chene on @date 20-12-3 下午7:24
 **/
class ArticleViewModel(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    val articles: LiveData<List<Article>>
        get() = _articles
    private val _articles = MutableLiveData<List<Article>>()

    @ExperimentalCoroutinesApi
    suspend fun refreshArticle(start: () -> Unit, end: () -> Unit, error: (msg: String?) -> Unit) =
        articleRepository.refreshArticles()
            .onStart { start.invoke() }
            .onCompletion { end.invoke() }
            .collectLatest {
                if (it is NetResult.Failure) {
                    error.invoke(it.errorMsg)
                } else if (it is NetResult.Success) {
                    _articles.postValue(it.value)
                }
            }


    @ExperimentalCoroutinesApi
    suspend fun loadArticle(start: () -> Unit, end: () -> Unit, error: (msg: String?) -> Unit) =
        articleRepository.loadArticles()
            .onStart { start.invoke() }
            .onCompletion { end.invoke() }.collectLatest {
                if (it is NetResult.Failure) {
                    error.invoke(it.errorMsg)
                } else if (it is NetResult.Success) {
                    it.value?.datas?.let { v ->
                        _articles.value?.let { a ->
                            _articles.postValue(
                                listOf(a, v).flatten()
                            )
                        }
                    }
                }
            }
}