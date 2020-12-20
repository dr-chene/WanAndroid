package com.example.module_todo.viewmodel

import androidx.lifecycle.ViewModel
import com.example.module_todo.repository.TodoRepository

/**
 *Created by chene on 20-12-20
 */
class SortViewModel internal constructor(
    private val repository: TodoRepository
): ViewModel() {
}