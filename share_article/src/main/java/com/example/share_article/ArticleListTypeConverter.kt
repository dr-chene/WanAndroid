package com.example.share_article

import androidx.room.TypeConverter
import com.example.share_article.bean.Article
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
Created by chene on @date 20-12-8 下午8:39
 **/
class ArticleListTypeConverter {

    @TypeConverter
    fun stringToList(s: String): List<Article> =
        Gson().fromJson(s, object : TypeToken<List<Article>>() {}.type)

    @TypeConverter
    fun listToString(list: List<Article>): String =
        Gson().toJson(list)
}