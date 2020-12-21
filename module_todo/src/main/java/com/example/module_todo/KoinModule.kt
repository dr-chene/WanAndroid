package com.example.module_todo

import android.view.View
import com.example.module_todo.adapter.TodoRecyclerViewAdapter
import com.example.module_todo.bean.Todo
import com.example.module_todo.fragment.SortDialogFragment
import com.example.module_todo.remote.TodoService
import com.example.module_todo.viewmodel.TodoViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 *Created by chene on 20-12-20
 */
val todoModule = module {
    single { SortDialogFragment() }
    single { get<Retrofit>().create(TodoService::class.java) }
    single { TodoViewModel(get()) }
    single { Todo.TodoDiffCallBack() }
    single { (option: (Todo, View) -> Unit) -> TodoRecyclerViewAdapter(option) }
}