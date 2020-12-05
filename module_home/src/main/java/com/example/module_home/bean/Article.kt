package com.example.module_home.bean

import androidx.recyclerview.widget.DiffUtil
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
Created by chene on @date 20-12-3 下午4:55
 **/
data class Article(
    val apkLink: String,
    val audit: Int,
    val author: String,
    val canEdit: Boolean,
    val chapterId: Long,
    val chapterName: String,
    val collect: Boolean,
    val courseId: Long,
    val desc: String,
    val descMd: String,
    val envelopePic: String,
    val fresh: Boolean,
    val id: Long,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val realSuperChapterId: Long,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String,
    val superChapterId: Long,
    val superChapterName: String,
    val tags: List<Tag>,
    val title: String,
    val type: Int,
    val userId: Long,
    val visible: Int,
    val zan: Int
) {
    class ArticleListTypeConverter {

        @TypeConverter
        fun stringToList(s: String): List<Article> =
            Gson().fromJson(s, object : TypeToken<List<Article>>() {}.type)

        @TypeConverter
        fun listToString(list: List<Article>): String =
            Gson().toJson(list)
    }

    class ArticleDiffCallBack: DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.link == newItem.link
        }

    }
}