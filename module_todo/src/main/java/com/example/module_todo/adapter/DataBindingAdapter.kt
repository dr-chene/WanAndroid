package com.example.module_todo.adapter

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.module_todo.TodoActivity.Companion.PRIORITY_IMPORTANT
import com.example.module_todo.TodoActivity.Companion.PRIORITY_NORMAL
import com.example.module_todo.TodoActivity.Companion.STATUS_COMPLETE
import com.example.module_todo.TodoActivity.Companion.STATUS_UN_COMPLETE
import com.example.module_todo.TodoActivity.Companion.TYPE_LIFE
import com.example.module_todo.TodoActivity.Companion.TYPE_PLAY
import com.example.module_todo.TodoActivity.Companion.TYPE_WORK

/**
 *Created by chene on 20-12-20
 */
@BindingAdapter("bindCompleteDate")
fun bindCompleteDate(view: TextView, date: String?){
    if (date.isNullOrEmpty()) {
        view.visibility = View.GONE
    } else {
        view.text = date
    }
}

@BindingAdapter("bindPriority")
fun bindPriority(view: TextView, priority: Int) {
    val text = priority.toPriority()
    val color = when (priority) {
        PRIORITY_IMPORTANT -> Color.RED
        PRIORITY_NORMAL -> Color.GRAY
        else -> Color.GRAY
    }
    view.setTextColor(color)
    view.text = text
}

fun Int.toPriority() = when (this) {
    PRIORITY_IMPORTANT -> "重要"
    PRIORITY_NORMAL -> "普通"
    else -> "普通"
}

@BindingAdapter("bindType")
fun bindType(view: TextView, type: Int) {
    val text = type.toType()
//    val color = when (type) {
//        TYPE_LIFE -> Color.GREEN
//        TYPE_PLAY -> Color.YELLOW
//        TYPE_WORK -> Color.BLUE
//        else -> Color.BLUE
//    }
//    view.setTextColor(color)
    view.text = text
}

fun Int.toType() = when (this) {
    TYPE_LIFE -> "生活"
    TYPE_PLAY -> "娱乐"
    TYPE_WORK -> "工作"
    else -> "生活"
}

@BindingAdapter("bindStatus")
fun bindStatus(view: TextView, status: Int) {
    val text = status.toStatus()
    val color = when (status) {
        STATUS_COMPLETE -> Color.GRAY
        STATUS_UN_COMPLETE -> Color.BLACK
        else -> Color.BLACK
    }
    view.setTextColor(color)
    view.text = text
}

fun Int.toStatus() = when (this) {
    STATUS_COMPLETE -> "已完成"
    STATUS_UN_COMPLETE -> "未完成"
    else -> "未完成"
}