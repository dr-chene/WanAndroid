package com.example.share_article.repository

import android.util.Log
import com.example.lib_net.bean.NetResult
import com.example.share_article.remote.ArticleService
import kotlinx.coroutines.flow.flow

/**
Created by chene on @date 20-12-14 上午9:49
 **/
open class ArticleRepository {

    private var over = false
    private var curPage = 0

    private fun getArticles(api: ArticleService, page: Int) = flow {
        try {
            if (over) {
                emit(NetResult.Failure("没有更多数据..."))
            } else {
                val net = api.getArticles(page)
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

    protected fun refresh(api: ArticleService) = getArticles(api, 0)

    protected fun load(api: ArticleService) = getArticles(api, curPage)

}