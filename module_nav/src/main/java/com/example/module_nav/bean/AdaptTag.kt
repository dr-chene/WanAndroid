package com.example.module_nav.bean

import com.example.share_article.bean.Tag

/**
Created by chene on @date 20-12-12 下午9:39
 **/
data class AdaptTag(
    val name: String,
    val link: String,
    val id: Int,
    val color: Int = Tag.getRandomColor()
)