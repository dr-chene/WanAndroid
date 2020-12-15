package com.example.module_nav

import com.example.module_nav.bean.*
import com.example.share_article.bean.Article

/**
Created by chene on @date 20-12-12 下午9:41
 **/
fun List<Nav>.adaptNavNav() = this.map {
    AdaptNav(it.articles.adaptArticleTag(), it.name)
}

fun List<Article>.adaptArticleTag() = this.map {
    AdaptTag(it.title, it.link, 0)
}

fun List<Tree>.adaptTreeNav() = this.map {
    AdaptNav(it.children.adaptTreeTag(), it.name)
}

fun List<Tree>.adaptTreeTag() = this.map {
    AdaptTag(it.name, "", it.id)
}

fun List<Project>.adaptNav(): List<AdaptNav> {
    val name = when (this[0].parentChapterId) {
        293 -> "项目"
        407 -> "公众号"
        else -> ""
    }
    return listOf(AdaptNav(this.map {
        AdaptTag(it.name, "", it.id)
    }, name))
}

inline fun <reified T> List<T>.adapt() = if (this.isNotEmpty()) {
    when (this[0]) {
        is Nav -> (this as List<Nav>).adaptNavNav()
        is Tree -> (this as List<Tree>).adaptTreeNav()
        is Project -> (this as List<Project>).adaptNav()
        else -> emptyList()
    }
} else {
    emptyList()
}