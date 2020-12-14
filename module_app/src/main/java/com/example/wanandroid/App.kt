package com.example.wanandroid

import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.BaseApp
import com.example.lib_base.isDebug
import com.example.lib_net.netModule
import com.example.module_coin_detail.coinDetailModule
import com.example.module_coin_rank.coinRankModule
import com.example.module_home.homeModule
import com.example.module_login.loginModule
import com.example.module_mine.mineModule
import com.example.module_nav.navModule
import com.example.module_search.searchModule
import com.example.module_setting.settingModule
import com.example.module_square.squareModule
import com.example.share_article.shareHomeSearchModule
import com.tencent.mmkv.MMKV
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

        MMKV.initialize(this)

        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@App)
            modules(
                appModule, homeModule, netModule, shareHomeSearchModule, searchModule,
                mineModule, loginModule, settingModule, coinRankModule, coinDetailModule,
                navModule, squareModule
            )
        }

        if (isDebug) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }
}