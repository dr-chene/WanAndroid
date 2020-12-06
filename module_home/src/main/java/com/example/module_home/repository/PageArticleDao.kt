package com.example.module_home.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.module_home.bean.PageArticle
import kotlinx.coroutines.flow.Flow


/**
Created by chene on @date 20-12-3 下午6:38
 **/
@Dao
interface PageArticleDao {

    @Query("SELECT * FROM page_articles WHERE cur_page = :page")
    fun getArticlesByPage(page: Int): Flow<PageArticle?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: PageArticle)
}