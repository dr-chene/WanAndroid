package com.example.module_setting

import com.example.module_setting.remote.LoginOutService
import com.example.module_setting.repository.LoginOutRepository
import org.koin.dsl.module
import retrofit2.Retrofit

/**
Created by chene on @date 20-12-11 上午11:24
 **/
val settingModule = module {
    single { (get() as Retrofit).create(LoginOutService::class.java) }
    single { LoginOutRepository() }
}