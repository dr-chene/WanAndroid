package com.example.module_home.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.module_home.bean.TopArticle
import kotlinx.coroutines.flow.Flow

/**
Created by chene on @date 20-12-4 下午3:29
 **/
@Dao
interface TopArticleDao {

    @Query("SELECT * FROM top_articles ORDER BY last_time DESC LIMIT 1")
    fun getTopArticle(): Flow<TopArticle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopArticle(topArticle: TopArticle)
}