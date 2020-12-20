package com.example.lib_net.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

/**
 *Created by chene on 20-12-20
 */
@Entity(tableName = "page_articles")
data class Page<T>(
    @PrimaryKey
    @ColumnInfo(name = "cur_page")
    val curPage: Int,
    val datas: List<T>?,
    val offset: Int,
    val over: Boolean,
    @ColumnInfo(name = "page_count")
    val pageCount: Int,
    val size: Int,
    val total: Int,
    @ColumnInfo(name = "last_time")
    val lastTime: Long = System.currentTimeMillis()
) {

}