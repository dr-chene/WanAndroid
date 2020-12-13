package com.example.module_search.repository

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetResult
import com.example.module_search.bean.SearchHistory
import com.example.module_search.remote.SearchService
import com.example.share_article.bean.PageArticle
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

    fun search(content: String, isSearch: Boolean, searchTag: Int) = flow {
        try {
            val searchKey = if (isSearch) {
                over = false
                curSearch = content
                content
            } else {
                curSearch
            }
            if (!over) {
                val searchResult = searchByTag(searchTag, searchKey, curPage)
                if (searchResult == null) {
                    emit(NetResult.Failure("搜索tag参数错误..."))
                } else {
                    searchResult.let {
                        if (it.data == null) {
                            emit(NetResult.Failure(it.errorMsg))
                        } else it.data?.let { page ->
                            over = page.over
                            curPage = page.curPage
                            emit(NetResult.Success(page.datas))
                        }
                    }
                }
            } else {
                emit(NetResult.Failure("没有更多数据..."))
            }
        } catch (e: Exception) {
            emit(NetResult.Failure(e.message))
        }
    }

    private suspend fun searchByTag(tag: Int, content: String, page: Int): NetBean<PageArticle>? {
        return when (tag) {
            SearchHistory.SEARCH_TAG_AUTHOR -> searchApi.getSearchByAuthor(page, content)
            SearchHistory.SEARCH_TAG_KEY -> searchApi.getSearchByKey(page, content)
            else -> null
        }
    }
}