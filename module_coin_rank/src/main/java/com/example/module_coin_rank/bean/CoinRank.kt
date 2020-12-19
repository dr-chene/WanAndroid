package com.example.module_coin_rank.bean

import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
Created by chene on @date 20-12-11 下午2:07
 **/
data class CoinRank(
    val coinCount: Int,
    val level: Int,
    val rank: Int,
    val userId: Int,
    val username: String
) {
    class CoinRankDiffCallBack : DiffUtil.ItemCallback<CoinRank>() {
        override fun areItemsTheSame(oldItem: CoinRank, newItem: CoinRank): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: CoinRank, newItem: CoinRank): Boolean {
            return oldItem.username == newItem.username
        }

    }
}

@Entity(tableName = "page_coin_rank")
@TypeConverters(CoinRankListConverter::class)
data class PageCoinRank(
    @PrimaryKey
    @ColumnInfo(name = "cur_page")
    val curPage: Int,
    val datas: List<CoinRank>,
    val offset: Int,
    val over: Boolean,
    @ColumnInfo(name = "page_count")
    val pageCount: Int,
    val size: Int,
    val total: Int,
    @ColumnInfo(name = "last_time")
    val lastTime: Long = System.currentTimeMillis()
)

class CoinRankListConverter {

    @TypeConverter
    fun stringToList(s: String): List<CoinRank> =
        Gson().fromJson(s, object : TypeToken<List<CoinRank>>() {}.type)

    @TypeConverter
    fun listToString(list: List<CoinRank>): String =
        Gson().toJson(list)
}