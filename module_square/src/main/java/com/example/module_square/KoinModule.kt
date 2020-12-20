package com.example.module_square

import com.example.module_square.remote.QaService
import com.example.module_square.remote.SquareService
import com.example.module_square.repository.SquareRepository
import org.koin.dsl.module
import retrofit2.Retrofit

/**
Created by chene on @date 20-12-13 下午10:36
 **/
val squareModule = module {
    single { (get() as Retrofit).create(SquareService::class.java) }
    single { (get() as Retrofit).create(QaService::class.java) }
    factory { (type: Int) -> SquareRepository(type, get(), get()) }
}