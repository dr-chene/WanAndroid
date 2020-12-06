package com.example.module_home.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lib_base.netWorkCheck
import com.example.module_home.bean.Banner
import com.example.module_home.repository.BannerRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import org.koin.java.KoinJavaComponent

/**
Created by chene on @date 20-12-5 下午7:49
 **/
class BannerViewModel(
    private val bannerRepository: BannerRepository
) : ViewModel() {

    val banners: LiveData<List<Banner>>
        get() = _banners
    private val _banners = MutableLiveData<List<Banner>>()

    @ExperimentalCoroutinesApi
    suspend fun loadBanner() {
        if (netWorkCheck()) {
            bannerRepository.getBanner().collectLatest {
                _banners.postValue(it.data.sortedBy { banner ->
                    banner.order
                })
            }
        } else {
            Toast.makeText(
                KoinJavaComponent.get(Context::class.java),
                "网络连接失败，请检查后重试...",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}