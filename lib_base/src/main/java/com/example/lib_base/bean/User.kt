package com.example.lib_base.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
Created by chene on @date 20-12-10 下午5:13
 **/
@Entity(tableName = "user")
data class User(
    val admin: Boolean,
    @ColumnInfo(name = "chapter_tops")
    val chapterTops: List<Any>,
    @ColumnInfo(name = "coin_count")
    val coinCount: Int,
    @ColumnInfo(name = "collect_ids")
    val collectIds: List<Int>,
    val email: String,
    val icon: String,
    @PrimaryKey
    val id: Int,
    val nickname: String,
    val password: String,
    @ColumnInfo(name = "public_name")
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String,
)