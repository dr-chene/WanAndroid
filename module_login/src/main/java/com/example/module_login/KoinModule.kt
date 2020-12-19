package com.example.module_login

import com.example.module_login.remote.LoginService
import com.example.module_login.repository.LoginRepository
import org.koin.dsl.module
import retrofit2.Retrofit

/**
Created by chene on @date 20-12-10 下午10:17
 **/
val loginModule = module {
    single { (get() as Retrofit).create(LoginService::class.java) }
    single { LoginRepository() }
}