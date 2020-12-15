package com.example.module_home.repository

import com.example.lib_net.bean.NetResult
import com.example.module_home.remote.BannerService
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent

/**
Created by chene on @date 20-12-5 下午7:39
 **/
class BannerRepository {

    private val bannerApi by KoinJavaComponent.inject(BannerService::class.java)

    fun getBanner() = flow {
        emit(
            try {
                NetResult.Success(bannerApi.getBanner().data)
            } catch (e: Exception) {
                NetResult.Failure(e.message)
            }
        )
    }

}