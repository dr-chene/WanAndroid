package com.example.module_search.repository

import com.example.lib_net.bean.NetBean
import com.example.module_search.bean.SearchHistory
import com.example.module_search.remote.AuthorSearchService
import com.example.module_search.remote.KeySearchService
import com.example.share_article.bean.PageArticle
import com.example.share_article.repository.ArticleRepository
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-8 下午11:07
 **/
class SearchRepository(
    private val tag: Int
) : ArticleRepository() {

    private val keySearchApi by inject(KeySearchService::class.java)
    private val authorSearchApi by inject(AuthorSearchService::class.java)

    private var curSearch = ""

//    fun search(content: String, isSearch: Boolean, searchTag: Int) = flow {
//        try {
//            val searchKey = if (isSearch) {
//                over = false
//                curSearch = content
//                content
//            } else {
//                curSearch
//            }
//            if (!over) {
//                val searchResult = searchByTag(searchTag, searchKey, curPage)
//                if (searchResult == null) {
//                    emit(NetResult.Failure("搜索tag参数错误..."))
//                } else {
//                    searchResult.let {
//                        if (it.data == null) {
//                            emit(NetResult.Failure(it.errorMsg))
//                        } else it.data?.let { page ->
//                            over = page.over
//                            curPage = page.curPage
//                            emit(NetResult.Success(page.datas))
//                        }
//                    }
//                }
//            } else {
//                emit(NetResult.Failure("没有更多数据..."))
//            }
//        } catch (e: Exception) {
//            emit(NetResult.Failure(e.message))
//        }
//    }


    override suspend fun request(page: Int, query: String, cid: Int): NetBean<PageArticle> {
        return api().getArticles(page, query)
    }

    private fun api() = when (tag) {
        SearchHistory.SEARCH_TAG_AUTHOR -> authorSearchApi
        else -> keySearchApi
    }

    private fun load(query: String) = super.load(query, 0)

    fun refresh(query: String) = super.refresh(query, 0)

    fun search(query: String, isSearch: Boolean) = load(
        if (isSearch) {
            over = false
            curSearch = query
            query
        } else {
            curSearch
        }
    )
}