package com.example.module_todo

import com.example.module_todo.fragment.SortDialogFragment
import com.example.module_todo.remote.TodoService
import com.example.module_todo.repository.TodoRepository
import com.example.module_todo.viewmodel.SortViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 *Created by chene on 20-12-20
 */
val todoModule = module {
    single { SortDialogFragment() }
    single { get<Retrofit>().create(TodoService::class.java) }
    factory { TodoRepository(get()) }
    viewModel { SortViewModel(get()) }
}