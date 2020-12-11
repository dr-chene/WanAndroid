package com.example.module_search.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lib_base.showToast
import com.example.lib_net.doFailure
import com.example.lib_net.doSuccess
import com.example.module_search.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

/**
Created by chene on @date 20-12-8 下午11:13
 **/
class SearchedViewModel internal constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    @ExperimentalCoroutinesApi
    private suspend fun realSearch(
        content: String,
        start: () -> Unit,
        end: () -> Unit,
        isSearch: Boolean
    ) = synchronized(this) {
        channelFlow {
            searchRepository.search(content, isSearch)
                .onStart { start.invoke() }
                .onCompletion { end.invoke() }
                .collectLatest {
                    it.doFailure { t ->
                        t?.showToast()
                    }
                    it.doSuccess { list ->
                        send(list)
                    }
                }
        }
    }

    suspend fun load(start: () -> Unit, end: () -> Unit) = realSearch("", start, end, false)

    suspend fun search(content: String, end: () -> Unit) = realSearch(content, {}, end, true)
}