package com.example.module_nav.repository

import android.util.Log
import com.example.lib_net.bean.NetResult
import com.example.module_nav.bean.Tree
import com.example.module_nav.remote.TreeService
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-12 下午8:46
 **/
class TreeRepository internal constructor(
    private val treeDao: TreeDao
) {

    private val treeApi by inject(TreeService::class.java)
    private var curPage = 0
    private var over = false

    suspend fun refresh() {
        treeApi.getTree().data?.forEach {
            insertTree(it)
        }
    }

    fun load() = treeDao.getTrees()

    private suspend fun insertTree(tree: Tree) = treeDao.insertTree(tree)

    fun refreshTreeArticle(cid: Int) = getTreeArticle(0, cid)

    fun loadTreeArticle(cid: Int) = getTreeArticle(curPage, cid)

    private fun getTreeArticle(page: Int, cid: Int) = flow {
        try {
            if (!over) {
                treeApi.getTreeArticle(page, cid).let {
                    if (it.data == null) {
                        emit(NetResult.Failure(it.errorMsg))
                    } else {
                        it.data?.let { article ->
                            curPage = article.curPage
                            over = article.over
                            emit(NetResult.Success(article.datas))
                        }
                    }
                }
            } else {
                emit(NetResult.Failure("没有更多数据..."))
            }
        } catch (e: Exception) {
            Log.d("TAG_debug", "getTreeArticle: ${e.message}")
            emit(NetResult.Failure(e.message))
        }
    }
}