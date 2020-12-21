package com.example.module_search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lib_base.viewmodel.BaseViewModel
import com.example.lib_net.result
import com.example.module_search.bean.SearchHistory
import com.example.module_search.repository.SearchHistoryRepository
import com.example.module_search.repository.SearchRepository
import com.example.share_article.bean.Article
import com.example.share_article.bean.HotKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get

/**
Created by chene on @date 20-12-8 下午9:05
 **/
class SearchActivityViewModel internal constructor(
    private val searchHistoryRepository: SearchHistoryRepository,
    private val repository: SearchRepository,
) : BaseViewModel() {

    val hotKeys: LiveData<List<HotKey>>
        get() = _hotKeys
    private val _hotKeys = MutableLiveData<List<HotKey>>()

    fun setHotKeys(hotKeys: List<HotKey>) {
        _hotKeys.postValue(hotKeys)
    }

    val searchContent: LiveData<String>
        get() = _searchContent
    private val _searchContent = MutableLiveData<String>()
    fun search(content: String) {
        _searchContent.postValue(content)
        _loading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            insertHistory(content, curTag)
        }
    }

    val searchHistory = searchHistoryRepository.searchHistory

    private suspend fun insertHistory(searchContent: String, tag: Int) =
        withContext(Dispatchers.IO) {
            searchHistoryRepository.insertSearchHistory(SearchHistory(searchContent, tag))
        }

    suspend fun deleteSearchHistory(content: String) =
        searchHistoryRepository.deleteSearchHistory(content)

    private var curTag = SearchHistory.SEARCH_TAG_KEY
    fun changeSearchTag(tag: Int) {
        repository.api(tag)
        curTag = tag
    }

    val articles: LiveData<List<Article>>
        get() = _articles
    private val _articles = MutableLiveData<List<Article>>()

    fun refresh(query: String) = CoroutineScope(Dispatchers.IO).launch {
        _loading.postValue(true)
        repository.refresh(query).result(
            start = null,
            completion = { _loading.postValue(false) }
        ) {
            it?.datas.let { list ->
                _articles.postValue(list)
            }
        }
    }

    fun load(curList: MutableList<Article>) = CoroutineScope(Dispatchers.IO).launch {
        _loading.postValue(true)
        repository.load().result(
            start = null,
            completion = { _loading.postValue(false) }
        ) {
            it?.datas?.let { list ->
                curList.addAll(list)
                _articles.postValue(curList)
            }
        }
    }
}