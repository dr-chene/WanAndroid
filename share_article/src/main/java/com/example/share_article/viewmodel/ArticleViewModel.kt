package com.example.share_article.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.lib_net.repository.PageViewModel
import com.example.lib_net.result
import com.example.share_article.bean.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
Created by chene on @date 20-12-14 上午9:49
 **/
abstract class ArticleViewModel : PageViewModel() {

    val articles: LiveData<List<Article>>
    get() = _articles
    private val _articles = MutableLiveData<List<Article>>()

    protected abstract suspend fun request(
        page: Int,
        query: String,
        cid: Int
    ): NetBean<NetPage<Article>>

    private suspend fun getArticles(page: Int, query: String, cid: Int) =
        request(page, query, cid).pageRequest()

    protected fun refresh(startPage: Int, query: String, cid: Int){
        CoroutineScope(Dispatchers.IO).launch {
            getArticles(let {
                curPage = startPage
                over = false
                curPage
            }, query, cid).result(
                completion = { _refreshing.postValue(false) }
            ) {
                _articles.postValue(it?.datas)
            }
        }
    }

    protected fun load(query: String, cid: Int, curList: MutableList<Article>){
        _loading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            getArticles(curPage, query, cid).result(
                completion = {_loading.postValue(false)}
            ){
                it?.datas?.let { list ->
                    curList.addAll(list)
                    _articles.postValue(curList)
                }
            }
        }
    }
}