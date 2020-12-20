package com.example.module_todo.repository

import com.example.lib_net.repository.PageRepository
import com.example.lib_net.request
import com.example.module_todo.remote.TodoService

/**
 *Created by chene on 20-12-20
 */
class TodoRepository(
    private val api: TodoService
) : PageRepository() {

    suspend fun refresh(
        status: Int? = null,
        type: Int? = null,
        priority: Int? = null,
        orderby: Int = 4
    ) = api.list(let {
        over = false
        curPage = 1
        1
    }, status, type, priority, orderby).pageRequest()

    suspend fun load(
        status: Int? = null,
        type: Int? = null,
        priority: Int? = null,
        orderby: Int = 4
    ) = api.list(curPage, status, type, priority, orderby).pageRequest()

    suspend fun add(
        title: String,
        content: String,
        date: String? = null,
        type: Int? = null,
        priority: Int? = null
    ) = api.add(title, content, date, type, priority).request()

    suspend fun modify(
        id: Int,
        title: String,
        content: String,
        date: String,
        status: Int? = null,
        type: Int? = null,
        priority: Int? = null
    ) = api.modify(id, title, content, date, status, type, priority).request()

    suspend fun delete(id: Int) = api.delete(id).request()

    suspend fun complete(id: Int, status: Int) = api.complete(id, status).request()
}