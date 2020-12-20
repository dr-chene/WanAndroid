package com.example.module_todo.bean

/**
 *Created by chene on 20-12-20
 */
data class Todo(
    val completeDate: Long?,
    val completeDateStr: String,
    val content: String,
    val date: Long,
    val dateStr: String,
    val id: Int,
    val priority: Int,
    val status: Int,
    val title: String,
    val type: Int,
    val userId: Int
)