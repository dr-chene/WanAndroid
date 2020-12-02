package com.example.module_app

import com.example.lib_base.BaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
Created by chene on @date 20-12-2 下午9:56
 **/
class App : BaseApp() {

    /**
     * init koin and its module
     */
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@App)
            modules(

            )
        }
    }
}