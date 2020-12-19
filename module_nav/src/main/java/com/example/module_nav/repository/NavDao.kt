package com.example.module_nav.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.module_nav.bean.Nav
import kotlinx.coroutines.flow.Flow

/**
Created by chene on @date 20-12-12 下午4:14
 **/
@Dao
interface NavDao {

    @Query("SELECT * FROM navs ORDER BY cid")
    fun get(): Flow<List<Nav>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(t: Nav)
}