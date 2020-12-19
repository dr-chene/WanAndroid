package com.example.share_article.bean

import androidx.recyclerview.widget.DiffUtil

/**
Created by chene on @date 20-12-3 下午4:55
 **/
data class Article(
    val apkLink: String,
    val audit: Int,
    val author: String?,
    val canEdit: Boolean,
    val chapterId: Long,
    val chapterName: String,
    val collect: Boolean,
    val courseId: Long,
    val desc: String,
    val descMd: String,
    val envelopePic: String,
    val fresh: Boolean,
    val id: Int,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val originId: Int = -1,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val realSuperChapterId: Long,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String?,
    val superChapterId: Long,
    val superChapterName: String,
    var tags: List<Tag>,
    val title: String,
    val type: Int,
    val userId: Long,
    val visible: Int,
    val zan: Int
) {

    class ArticleDiffCallBack : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.link == newItem.link
        }

    }
}