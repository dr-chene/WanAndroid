package com.example.module_coin_detail.bean

import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import com.example.lib_base.getResColor
import com.example.module_coin_detail.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.java.KoinJavaComponent.get
import kotlin.random.Random

/**
Created by chene on @date 20-12-11 下午10:51
 **/
data class CoinDetail(
    val coinCount: Int,
    val date: Long,
    val desc: String,
    val id: Int,
    val reason: String,
    val type: Int,
    val userId: Int,
    val userName: String
) {

    val backgroundColor: Int
        get() = getRandomBackgroundColor()

    class CoinDetailDiffCallBack : DiffUtil.ItemCallback<CoinDetail>() {
        override fun areItemsTheSame(oldItem: CoinDetail, newItem: CoinDetail): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: CoinDetail, newItem: CoinDetail): Boolean {
            return oldItem.id == newItem.id
        }

    }

    companion object {
        private val backgroundColors = arrayOf(
            R.color.red_50.getResColor(),
            R.color.orange_50.getResColor(),
            R.color.yellow_50.getResColor(),
            R.color.green_50.getResColor(),
            R.color.cyan_50.getResColor(),
            R.color.blue_50.getResColor(),
            R.color.purple_50.getResColor()
        )

        private fun getRandomBackgroundColor() = backgroundColors[
                get(Random::class.java).nextInt(backgroundColors.size)
        ]
    }
}

@Entity(tableName = "page_coin_detail")
@TypeConverters(CoinDetailListConverter::class)
data class PageCoinDetail(
    @PrimaryKey
    @ColumnInfo(name = "cur_page")
    val curPage: Int,
    val datas: List<CoinDetail>,
    val offset: Int,
    val over: Boolean,
    @ColumnInfo(name = "page_count")
    val pageCount: Int,
    val size: Int,
    val total: Int,
    @ColumnInfo(name = "last_time")
    val lastTime: Long = System.currentTimeMillis()
)

class CoinDetailListConverter {

    @TypeConverter
    fun stringToList(s: String): List<CoinDetail> =
        Gson().fromJson(s, object : TypeToken<List<CoinDetail>>() {}.type)

    @TypeConverter
    fun listToString(list: List<CoinDetail>): String =
        Gson().toJson(list)
}