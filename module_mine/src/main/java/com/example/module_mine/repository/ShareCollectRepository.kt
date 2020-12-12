package com.example.module_mine.repository

import android.util.Log
import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetResult
import com.example.module_mine.remote.ShareCollectService
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-12 上午10:58
 **/
class ShareCollectRepository {

    private val shareCollectApi by inject(ShareCollectService::class.java)

    suspend fun shareArticle(title: String, link: String) =
        request(shareCollectApi.shareArticle(title, link))

    suspend fun collectArticle(title: String, author: String, link: String) =
        request(shareCollectApi.collectArticle(title, author, link))

    suspend fun collectWeb(name: String, link: String) = request(shareCollectApi.collectWeb(name, link))

    private fun <T> request(result: NetBean<T>) = flow {
        try {
            result.apply {
                if (errorCode == REQUEST_SUCCESS) {
                    emit(NetResult.Success(data))
                } else {
                    emit(NetResult.Failure(errorMsg))
                }
            }
        } catch (e: Exception) {
            Log.d("TAG_debug", "shareArticle: ${e.message}")
            emit(NetResult.Failure(e.message))
        }
    }

    companion object {
        const val REQUEST_SUCCESS = 0
    }
}