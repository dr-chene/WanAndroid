package com.example.module_home.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.lib_base.BaseNetBean

/**
Created by chene on @date 20-12-4 下午2:33
 **/
@Entity(tableName = "top_articles")
@TypeConverters(Article.ArticleListTypeConverter::class)
data class TopArticle(
    val data: List<Article>,
    @PrimaryKey
    val index: Long = data[0].id,
    @ColumnInfo(name = "last_time")
    val lastTime: Long = System.currentTimeMillis()
) : BaseNetBean()