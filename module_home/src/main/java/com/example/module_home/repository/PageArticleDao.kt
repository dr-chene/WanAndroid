package com.example.module_home.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lib_net.bean.Page
import com.example.share_article.bean.Article
import com.example.share_article.bean.PageArticle


/**
Created by chene on @date 20-12-3 下午6:38
 **/
@Dao
interface PageArticleDao {

    @Query("SELECT * FROM page_articles WHERE cur_page = :page")
    fun getArticlesByPage(page: Int): Page<Article>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: Page<Article>)
}