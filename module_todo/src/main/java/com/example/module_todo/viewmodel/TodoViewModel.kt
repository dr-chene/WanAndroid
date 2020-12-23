package com.example.module_todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lib_net.repository.PageViewModel
import com.example.lib_net.request
import com.example.lib_net.result
import com.example.module_todo.bean.Todo
import com.example.module_todo.remote.TodoService
import com.example.module_todo.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by chene on 20-12-20
 */
class TodoViewModel(
    private val repository: TodoRepository
) : PageViewModel() {

    suspend fun add(
        title: String,
        content: String,
        date: String? = null,
        type: Int? = null,
        priority: Int? = null
    ) = repository.remoteAdd(title, content, date, type, priority)

    suspend fun modify(
        id: Int,
        title: String,
        content: String,
        date: String,
        status: Int? = null,
        type: Int? = null,
        priority: Int? = null
    ) = repository.remoteModify(id, title, content, date, status, type, priority)

    suspend fun delete(id: Int) = repository.remoteDelete(id)

    suspend fun complete(id: Int, status: Int) = repository.remoteComplete(id, status)

    private var _status: Int? = null
    private var _type: Int? = null
    private var _priority: Int? = null
    private var _orderby: Int = 4

    var modifyTodo: Todo? = null

    val todos: LiveData<List<Todo>>
        get() = _todos
    private val _todos = MutableLiveData<List<Todo>>()
    fun sort(status: Int? = null, type: Int? = null, priority: Int? = null, orderby: Int = 4) {
        _status = status
        _type = type
        _priority = priority
        _orderby = orderby
        refresh()
        _refreshing.postValue(true)
    }

    fun refresh(){
        CoroutineScope(Dispatchers.IO).launch {
            repository.remoteList(let {
                over = false
                curPage = 1
                1
            },_status, _type, _priority, _orderby).pageRequest().result(
                completion = {_refreshing.postValue(false)}
            ) {
                _todos.postValue(it?.datas)
            }
        }
    }

    fun load(curList: MutableList<Todo>) {
        _loading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            repository.remoteList(curPage, _status, _type, _priority, _orderby).pageRequest().result(
                completion = { _loading.postValue(false) }
            ) {
                it?.datas?.let { list -> curList.addAll(list) }
                _todos.postValue(curList)
            }
        }
    }
}