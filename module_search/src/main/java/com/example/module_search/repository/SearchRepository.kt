package com.example.module_search.repository

import com.example.lib_net.bean.NetResult
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
                searchApi.getSearch(curPage, searchKey).let {
                    if (it.data == null) {
                        emit(NetResult.Failure(it.errorMsg))
                    } else it.data?.let { page ->
                        over = page.over
                        curPage = page.curPage
                        emit(NetResult.Success(page.datas))
                    }
                }
            } else {
                emit(NetResult.Failure("没有更多数据..."))
            }
        } catch (e: Exception) {
            emit(NetResult.Failure(e.message))
        }
    }

}