package com.example.module_coin_rank.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.module_coin_rank.bean.PageCoinRank

/**
Created by chene on @date 20-12-11 下午7:11
 **/
@Dao
interface PageCoinRankDao {

    @Query("SELECT * FROM page_coin_rank WHERE cur_page = :page")
    fun getPageCoinRank(page: Int): PageCoinRank?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageCoinRank(pageCoinRank: PageCoinRank)
}