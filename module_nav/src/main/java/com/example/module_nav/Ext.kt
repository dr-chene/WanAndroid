package com.example.module_nav

import com.example.module_nav.bean.AdaptNav
import com.example.module_nav.bean.AdaptTag
import com.example.module_nav.bean.Nav
import com.example.module_nav.bean.Tree
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

