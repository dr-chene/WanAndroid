package com.example.share_home_search.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.share_home_search.ArticleListTypeConverter

@Entity(tableName = "page_articles")
@TypeConverters(ArticleListTypeConverter::class)
data class PageArticle(
    @PrimaryKey
    @ColumnInfo(name = "cur_page")
    val curPage: Int,
    val datas: List<Article>,
    val offset: Int,
    val over: Boolean,
    @ColumnInfo(name = "page_count")
    val pageCount: Int,
    val size: Int,
    val total: Int,
    @ColumnInfo(name = "last_time")
    val lastTime: Long = System.currentTimeMillis()
)
