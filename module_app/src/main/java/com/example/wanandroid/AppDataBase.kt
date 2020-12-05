package com.example.wanandroid

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.module_home.bean.PageArticle
import com.example.module_home.bean.TopArticle
import com.example.module_home.repository.PageArticleDao
import com.example.module_home.repository.TopArticleDao

/**
Created by chene on @date 20-12-3 下午6:28
 **/
@Database(entities = [PageArticle::class, TopArticle::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getPageArticleDao(): PageArticleDao
    abstract fun getTopArticleDao(): TopArticleDao

    companion object {

        private const val DATA_BASE_NAME = "wanandroid.db"

        fun buildDatabase(context: Context): AppDataBase =
            Room.databaseBuilder(context, AppDataBase::class.java, DATA_BASE_NAME).build()

    }
}