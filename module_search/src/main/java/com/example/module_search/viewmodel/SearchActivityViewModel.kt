package com.example.module_search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.module_search.bean.SearchHistory
import com.example.module_search.repository.SearchHistoryRepository
import com.example.share_article.bean.HotKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
Created by chene on @date 20-12-8 下午9:05
 **/
class SearchActivityViewModel internal constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) : ViewModel() {

    val hotKeys: LiveData<List<HotKey>>
        get() = _hotKeys
    private val _hotKeys = MutableLiveData<List<HotKey>>()

    fun setHotKeys(hotKeys: List<HotKey>) {
        _hotKeys.postValue(hotKeys)
    }

    val searching: LiveData<Boolean>
        get() = _searching
    private val _searching = MutableLiveData<Boolean>()
    fun startSearch() {
        _searching.postValue(true)
    }

    fun endSearch() {
        _searching.postValue(false)
    }

    val searchContent: LiveData<String>
        get() = _searchContent
    private val _searchContent = MutableLiveData<String>()
    fun search(content: String, tag: Int) {
        _searchContent.postValue(content)
        startSearch()
        CoroutineScope(Dispatchers.IO).launch {
            insertHistory(content, tag)
        }
    }

    val searchHistory = searchHistoryRepository.searchHistory

    private suspend fun insertHistory(searchContent: String, tag: Int) =
        withContext(Dispatchers.IO) {
            searchHistoryRepository.insertSearchHistory(SearchHistory(searchContent, tag))
        }

    suspend fun deleteSearchHistory(content: String) =
        searchHistoryRepository.deleteSearchHistory(content)

    val searchTag: LiveData<Int>
        get() = _searchTag
    private val _searchTag = MutableLiveData(SearchHistory.SEARCH_TAG_KEY)
    fun changeSearchTag(tag: Int) {
        _searchTag.postValue(tag)
    }
}