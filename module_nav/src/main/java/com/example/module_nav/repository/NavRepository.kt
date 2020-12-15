package com.example.module_nav.repository

import com.example.module_nav.remote.NavService
import com.example.module_nav.remote.ProjectService
import com.example.module_nav.remote.PublicService
import com.example.module_nav.remote.TreeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-14 下午11:18
 **/
class NavRepository(
    private val tab: Int
) {

    private val navApi by inject(NavService::class.java)
    private val treeApi by inject(TreeService::class.java)
    private val projectApi by inject(ProjectService::class.java)
    private val publicApi by inject(PublicService::class.java)

    private val navDao by inject(NavDao::class.java)
    private val treeDao by inject(TreeDao::class.java)
    private val projectDao by inject(ProjectDao::class.java)
    private val publicDao by inject(PublicDao::class.java)

    fun load() = when (tab) {
        TAB_NAV -> navDao.get()
        TAB_TREE -> treeDao.get()
        TAB_PROJECT -> projectDao.get()
        TAB_PUBLIC -> publicDao.get()
        else -> null
    }

    fun refresh() = CoroutineScope(Dispatchers.IO).launch {
        when (tab) {
            TAB_NAV -> {
                navApi.getData().data?.forEach {
                    navDao.insert(it)
                }
            }
            TAB_TREE -> {
                treeApi.getData().data?.forEach {
                    treeDao.insert(it)
                }
            }
            TAB_PROJECT -> {
                projectApi.getData().data?.forEach {
                    projectDao.insert(it)
                }
            }
            TAB_PUBLIC -> {
                publicApi.getData().data?.forEach {
                    publicDao.insert(it)
                }
            }
        }
    }

    companion object {
        const val TAB_NAV = 0
        const val TAB_TREE = 1
        const val TAB_PROJECT = 2
        const val TAB_PUBLIC = 3
    }
}