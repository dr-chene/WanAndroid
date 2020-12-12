package com.example.module_nav.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.share_article.ArticleListTypeConverter
import com.example.share_article.bean.Article

/**
Created by chene on @date 20-12-12 下午3:57
 **/
@Entity(tableName = "navs")
@TypeConverters(ArticleListTypeConverter::class)
data class Nav(
    val articles: List<Article>,
    @PrimaryKey
    val cid: Int,
    val name: String
)