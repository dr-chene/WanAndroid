package com.example.wanandroid

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.module_coin_detail.bean.PageCoinDetail
import com.example.module_coin_detail.respository.PageCoinDetailDao
import com.example.module_coin_rank.bean.PageCoinRank
import com.example.module_coin_rank.repository.PageCoinRankDao
import com.example.module_home.bean.TopArticle
import com.example.module_home.repository.PageArticleDao
import com.example.module_home.repository.TopArticleDao
import com.example.module_nav.bean.Nav
import com.example.module_nav.bean.Project
import com.example.module_nav.bean.Tree
import com.example.module_nav.repository.NavDao
import com.example.module_nav.repository.ProjectDao
import com.example.module_nav.repository.PublicDao
import com.example.module_nav.repository.TreeDao
import com.example.module_search.bean.SearchHistory
import com.example.module_search.repository.SearchHistoryDao
import com.example.share_article.bean.PageArticle

/**
Created by chene on @date 20-12-3 下午6:28
 **/
@Database(
    entities = [PageArticle::class, TopArticle::class, SearchHistory::class,
        PageCoinRank::class, PageCoinDetail::class, Nav::class,
        Tree::class, Project::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getPageArticleDao(): PageArticleDao
    abstract fun getTopArticleDao(): TopArticleDao
    abstract fun getSearchHistoryDao(): SearchHistoryDao
    abstract fun getPageCoinRankDao(): PageCoinRankDao
    abstract fun getPageCoinDetailDao(): PageCoinDetailDao
    abstract fun getNavDao(): NavDao
    abstract fun getTreeDao(): TreeDao
    abstract fun getProjectDao(): ProjectDao
    abstract fun getPublicDao(): PublicDao

    companion object {

        private const val DATA_BASE_NAME = "wanandroid.db"

        fun buildDatabase(context: Context): AppDataBase =
            Room.databaseBuilder(context, AppDataBase::class.java, DATA_BASE_NAME).build()

    }
}