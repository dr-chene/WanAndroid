package com.example.module_todo.repository

import com.example.lib_net.request
import com.example.module_todo.remote.TodoService

/**
 *Created by chene on 20-12-20
 */
class TodoRepository(
    private val api: TodoService
) {
    private var page = 1
    private var over = false

    suspend fun refresh(status: Int? = null, type: Int? = null, priority: Int? = null, orderby: Int = 4) =
        api.list(1, status, type, priority, orderby).request()

//    suspend fun load() =
}