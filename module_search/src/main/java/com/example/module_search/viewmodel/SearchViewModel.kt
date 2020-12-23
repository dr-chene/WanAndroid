package com.example.module_search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.module_search.bean.SearchHistory
import com.example.module_search.remote.AuthorSearchService
import com.example.module_search.remote.KeySearchService
import com.example.module_search.repository.SearchHistoryRepository
import com.example.module_search.repository.SearchRepository
import com.example.share_article.bean.Article
import com.example.share_article.bean.HotKey
import com.example.share_article.remote.QueryArticleService
import com.example.share_article.viewmodel.ArticleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
Created by chene on @date 20-12-8 下午11:07
 **/
class SearchViewModel(
    private val searchHistoryRepository: SearchHistoryRepository,
    private val searchRepository: SearchRepository
) : ArticleViewModel() {

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

    val searchHistory : LiveData<List<SearchHistory>>
    get() = _searchHistory
    private val _searchHistory = MutableLiveData<List<SearchHistory>>()

    fun getSearchHistory() = CoroutineScope(Dispatchers.IO).launch {
        searchHistoryRepository.searchHistory.collectLatest {
            _searchHistory.postValue(it)
        }
    }

    private suspend fun insertHistory(searchContent: String, tag: Int) =
        withContext(Dispatchers.IO) {
            searchHistoryRepository.insertSearchHistory(SearchHistory(searchContent, tag))
        }

    suspend fun deleteSearchHistory(content: String) =
        searchHistoryRepository.deleteSearchHistory(content)

    private var curTag = SearchHistory.SEARCH_TAG_KEY
    fun changeSearchTag(tag: Int) {
        searchRepository.api(tag)
        curTag = tag
    }

    override suspend fun request(page: Int, query: String, cid: Int): NetBean<NetPage<Article>> {
        return searchRepository.curApi.getArticles(page, query)
    }

    fun load(curList: MutableList<Article>) = super.load(searchRepository.curSearch, 0, curList)

    fun refresh(query: String) = super.refresh(0, query.apply {
        over = false
        searchRepository.curSearch = query
    }, 0)
}