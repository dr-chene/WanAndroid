package com.example.module_home.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.lib_net.BaseNetBean
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
Created by chene on @date 20-12-4 下午2:33
 **/
@Entity(tableName = "page_articles")
@TypeConverters(Article.ArticleListTypeConverter::class)
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
) : Serializable

data class NetPageArticle(
    @SerializedName(value = "data", alternate = ["shareArticles"])
    val data: PageArticle
) : com.example.lib_net.BaseNetBean()