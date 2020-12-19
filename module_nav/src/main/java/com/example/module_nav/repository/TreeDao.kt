package com.example.module_nav.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.module_nav.bean.Tree
import kotlinx.coroutines.flow.Flow

/**
Created by chene on @date 20-12-12 下午8:38
 **/
@Dao
interface TreeDao {

    @Query("SELECT * FROM trees ORDER BY id")
    fun get(): Flow<List<Tree>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(t: Tree)
}