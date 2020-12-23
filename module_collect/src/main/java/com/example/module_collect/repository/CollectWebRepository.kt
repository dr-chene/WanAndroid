package com.example.module_collect.repository

import com.example.lib_net.request
import com.example.module_collect.remote.CollectWebService

class CollectWebRepository(
    private val api: CollectWebService
) {
    suspend fun getRemoteWebs() = api.getWebs().request()
}