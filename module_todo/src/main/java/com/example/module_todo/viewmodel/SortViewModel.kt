package com.example.module_todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lib_net.result
import com.example.module_todo.bean.Todo
import com.example.module_todo.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by chene on 20-12-20
 */
class SortViewModel internal constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private var _status: Int? = null
    private var _type: Int? = null
    private var _priority: Int? = null
    private var _orderby: Int = 4

    val todos: LiveData<List<Todo>>
        get() = _todos
    private val _todos = MutableLiveData<List<Todo>>()
    fun sort(status: Int? = null, type: Int? = null, priority: Int? = null, orderby: Int = 4) {
        _status = status
        _type = type
        _priority = priority
        _orderby = orderby
        refresh(null)
    }

    fun refresh(completion: (() -> Unit)?) = CoroutineScope(Dispatchers.IO).launch {
        repository.refresh(_status, _type, _priority, _orderby).result(null, completion) {
            _todos.postValue(it?.datas)
        }
    }

    fun load(current: List<Todo>, start: () -> Unit, completion: () -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {
            repository.load(_status, _type, _priority, _orderby).result(start, completion) {
                it?.datas?.let { list -> current.toMutableList().addAll(list) }
                _todos.postValue(current)
            }
        }

    suspend fun add(
        title: String,
        content: String,
        date: String? = null,
        type: Int? = null,
        priority: Int? = null
    ) = repository.add(title, content, date, type, priority)

    suspend fun modify(
        id: Int,
        title: String,
        content: String,
        date: String,
        status: Int? = null,
        type: Int? = null,
        priority: Int? = null
    ) = repository.modify(id, title, content, date, status, type, priority)

    suspend fun delete(id: Int) = repository.delete(id)

    suspend fun complete(id: Int, status: Int) = repository.complete(id, status)
}