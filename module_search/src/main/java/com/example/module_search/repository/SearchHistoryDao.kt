package com.example.module_search.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.module_search.bean.SearchHistory
import kotlinx.coroutines.flow.Flow

/**
Created by chene on @date 20-12-8 下午9:37
 **/
@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history ORDER BY search_time DESC")
    fun getSearchHistory(): Flow<List<SearchHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(searchHistory: SearchHistory)

    @Query("DELETE FROM search_history WHERE search_content = :content")
    suspend fun delete(content: String)
}