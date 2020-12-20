package com.example.module_todo.bean

import androidx.recyclerview.widget.DiffUtil

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
){
    class TodoDiffCallBack : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.title == newItem.title
        }

    }
}