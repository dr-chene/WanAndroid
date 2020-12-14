package com.example.share_article.repository

import android.util.Log
import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetResult
import com.example.share_article.bean.PageArticle
import kotlinx.coroutines.flow.flow

/**
Created by chene on @date 20-12-14 上午9:49
 **/
abstract class ArticleRepository {

    protected var over = false
    private var curPage = 0

    private fun getArticles(page: Int, query: String, cid: Int) = flow {
        try {
            if (over) {
                emit(NetResult.Failure("没有更多数据..."))
            } else {
                val net = request(page, query, cid)
                if (net.data == null) {
                    emit(NetResult.Failure("网络数据请求失败..."))
                } else {
                    net.data?.let {
                        over = it.over
                        curPage = it.curPage
                        emit(NetResult.Success(it.datas))
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("TAG_debug", "getArticles: ${e.message}")
            emit(NetResult.Failure(e.message))
        }
    }

    protected abstract suspend fun request(
        page: Int,
        query: String,
        cid: Int
    ): NetBean<PageArticle>

    protected fun refresh(query: String, cid: Int) = getArticles(0, query, cid)

    protected fun load(query: String, cid: Int) = getArticles(curPage, query, cid)
}