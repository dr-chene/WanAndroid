package com.example.module_nav.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
Created by chene on @date 20-12-12 下午8:21
 **/
@Entity(tableName = "trees")
@TypeConverters(TreeTypeConverter::class)
data class Tree(
    val children: List<Tree>,
    val courseId: Int,
    @PrimaryKey
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)

class TreeTypeConverter {

    @TypeConverter
    fun stringToList(s: String): List<Tree> =
        Gson().fromJson(s, object : TypeToken<List<Tree>>() {}.type)

    @TypeConverter
    fun listToString(list: List<Tree>): String = Gson().toJson(list)
}