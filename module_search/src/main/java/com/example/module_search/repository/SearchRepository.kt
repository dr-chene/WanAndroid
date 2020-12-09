package com.example.module_search.repository

import com.example.lib_net.NetResult
import com.example.module_search.remote.SearchService
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-8 下午11:07
 **/
class SearchRepository {

    private val searchApi by inject(SearchService::class.java)

    private var curPage = 0
    private var over = false
    private var curSearch = ""

    fun search(content: String, isSearch: Boolean) = flow {
            try {
                val searchKey = if (isSearch) {
                    over = false
                    curSearch = content
                    content
                } else {
                    curSearch
                }
                if (!over) {
                    searchApi.getSearch(curPage, searchKey).data.let {
                        over = it.over
                        curPage = it.curPage
                        emit(NetResult.Success(it.datas))
                    }
                } else {
                    emit(NetResult.Failure(Exception("没有更多数据...")))
                }
            } catch (e: Exception) {
                emit(NetResult.Failure(e.cause))
            }
        }

}