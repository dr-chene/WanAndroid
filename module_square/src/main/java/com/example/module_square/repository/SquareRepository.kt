package com.example.module_square.repository

import com.example.module_square.SquareFragment
import com.example.module_square.remote.QaService
import com.example.module_square.remote.SquareService

class SquareRepository(
    private val type: Int,
    private val squareApi: SquareService,
    private val qaApi: QaService
) {
    private fun api() = when (type) {
        SquareFragment.FRAGMENT_TYPE_SQUARE -> squareApi
        else -> qaApi
    }

    suspend fun getRemoteArticle(page: Int) = api().getArticles(page)
}