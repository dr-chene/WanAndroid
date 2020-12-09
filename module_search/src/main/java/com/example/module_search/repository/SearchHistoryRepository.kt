package com.example.module_search.repository

import com.example.module_search.bean.SearchHistory

/**
Created by chene on @date 20-12-8 下午9:42
 **/
class SearchHistoryRepository(
    private val searchHistoryDao: SearchHistoryDao
) {

    val searchHistory = searchHistoryDao.getSearchHistory()

    suspend fun insertSearchHistory(searchHistory: SearchHistory) {
        searchHistoryDao.insertSearchHistory(searchHistory)
    }

    suspend fun deleteSearchHistory(content: String) {
        searchHistoryDao.delete(content)
    }
}