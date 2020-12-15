package com.example.module_nav.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
Created by chene on @date 20-12-14 下午9:39
 **/
@Entity(tableName = "projects")
data class Project(
    val courseId: Int,
    val id: Int,
    @PrimaryKey
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)