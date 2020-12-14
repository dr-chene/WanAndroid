package com.example.module_nav.repository

import com.example.module_nav.bean.Tree
import com.example.module_nav.remote.TreeService
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-12 下午8:46
 **/
class TreeRepository internal constructor(
    private val treeDao: TreeDao
) {

    private val treeApi by inject(TreeService::class.java)

    suspend fun refresh() {
        treeApi.getTree().data?.forEach {
            insertTree(it)
        }
    }

    fun load() = treeDao.getTrees()

    private suspend fun insertTree(tree: Tree) = treeDao.insertTree(tree)
}