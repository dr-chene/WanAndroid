package com.example.module_nav.viewmodel

import androidx.lifecycle.ViewModel
import com.example.module_nav.remote.NavService
import com.example.module_nav.remote.ProjectService
import com.example.module_nav.remote.PublicService
import com.example.module_nav.remote.TreeService
import com.example.module_nav.repository.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NavViewModel(
    private val tab: Int,
    private val navDao: NavDao,
    private val treeDao: TreeDao,
    private val projectDao: ProjectDao,
    private val publicDao: PublicDao,
    private val navApi: NavService,
    private val treeApi: TreeService,
    private val projectApi: ProjectService,
    private val publicApi: PublicService
) : ViewModel() {

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

    fun load() = when (tab) {
        TAB_NAV -> navDao.get()
        TAB_TREE -> treeDao.get()
        TAB_PROJECT -> projectDao.get()
        TAB_PUBLIC -> publicDao.get()
        else -> null
    }

    companion object {
        const val TAB_NAV = 0
        const val TAB_TREE = 1
        const val TAB_PROJECT = 2
        const val TAB_PUBLIC = 3
    }
}