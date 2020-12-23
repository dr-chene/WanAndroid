package com.example.module_nav.viewmodel

import androidx.lifecycle.ViewModel
import com.example.module_nav.remote.NavService
import com.example.module_nav.remote.ProjectService
import com.example.module_nav.remote.PublicService
import com.example.module_nav.remote.TreeService
import com.example.module_nav.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

class NavViewModel(
    private val tab: Int
) : ViewModel() {

    private val repository by inject(NavRepository::class.java){ parametersOf(tab) }

    fun refresh() = repository.refresh()

    fun load() = repository.load()
}