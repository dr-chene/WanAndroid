package com.example.module_todo.repository

import com.example.lib_net.request
import com.example.module_todo.remote.TodoService

class TodoRepository(
    private val api: TodoService
) {

    suspend fun remoteList(page: Int,status: Int?, type: Int?, priority: Int?, orderby: Int) =
        api.list(page, status, type, priority, orderby)

    suspend fun remoteAdd(
        title: String,
        content: String,
        date: String? = null,
        type: Int? = null,
        priority: Int? = null
    ) = api.add(title, content, date, type, priority).request()

    suspend fun remoteModify(
        id: Int,
        title: String,
        content: String,
        date: String,
        status: Int? = null,
        type: Int? = null,
        priority: Int? = null
    ) = api.modify(id, title, content, date, status, type, priority).request()

    suspend fun remoteDelete(id: Int) = api.delete(id).request()

    suspend fun remoteComplete(id: Int, status: Int) = api.complete(id, status).request()
}