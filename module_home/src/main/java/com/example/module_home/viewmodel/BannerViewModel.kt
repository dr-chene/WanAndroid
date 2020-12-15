package com.example.module_home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lib_net.bean.NetResult
import com.example.module_home.bean.Banner
import com.example.module_home.repository.BannerRepository
import kotlinx.coroutines.flow.collectLatest

/**
Created by chene on @date 20-12-5 下午7:49
 **/
class BannerViewModel(
    private val bannerRepository: BannerRepository
) : ViewModel() {

    val banners: LiveData<List<Banner>>
        get() = _banners
    private val _banners = MutableLiveData<List<Banner>>()

    suspend fun loadBanner() = bannerRepository.getBanner().collectLatest {
        if (it is NetResult.Success) {
            _banners.postValue(it.value)
        }
    }
}