package com.example.module_nav.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.module_nav.bean.Project
import kotlinx.coroutines.flow.Flow

/**
Created by chene on @date 20-12-14 下午11:12
 **/
@Dao
interface PublicDao {

    @Query("SELECT * FROM projects WHERE parentChapterId = 407 ORDER BY id")
    fun get(): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(t: Project)
}