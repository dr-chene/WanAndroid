package com.example.lib_net

import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
Created by chene on @date 20-12-6 下午4:10
 **/
private const val READ_TIMEOUT = 60L
private const val WRITE_TIMEOUT = 60L
private const val CONNECT_TIMEOUT = 3L
val netModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com")
//            .validateEagerly(true)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(CookieInterceptor())
                    .build()
            )
            .build()
    }
}