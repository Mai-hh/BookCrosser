package com.huaihao.bookcrosser.repo

import com.huaihao.bookcrosser.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface ReviewRepo {

    suspend fun loadAllReviews(): Flow<ApiResult>

}