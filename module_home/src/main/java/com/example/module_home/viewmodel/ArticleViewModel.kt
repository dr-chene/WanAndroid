package com.example.module_home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.example.module_home.bean.Article
import com.example.module_home.repository.ArticleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip

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
    suspend fun refresh() {
        articleRepository.refreshArticles().collect {
            _articles.postValue(it)
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun load() {
        _articles.asFlow().zip(articleRepository.loadArticles()) { before, load ->
            mutableListOf<Article>().apply {
                addAll(before)
                addAll(load)
            }.toList()
        }.collect {
            _articles.postValue(it)
        }
    }
}