package com.example.module_coin_detail.respository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.module_coin_detail.bean.PageCoinDetail

/**
Created by chene on @date 20-12-11 下午11:04
 **/
@Dao
interface PageCoinDetailDao {

    @Query("SELECT * FROM page_coin_detail WHERE cur_page = :page")
    fun getPageCoinDetail(page: Int): PageCoinDetail?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageCoinDetail(pageCoinDetail: PageCoinDetail)
}