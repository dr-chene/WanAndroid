package com.example.module_search.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
Created by chene on @date 20-12-8 下午9:30
 **/
@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey
    @ColumnInfo(name = "search_content")
    val searchContent: String,
    @ColumnInfo(name = "search_tag")
    val searchTag: Int,
    @ColumnInfo(name = "search_time")
    val searchTime: Long = System.currentTimeMillis()
){
    companion object {
        const val SEARCH_TAG_KEY = 0
        const val SEARCH_TAG_AUTHOR = 1
    }
}