package com.huaihao.bookcrosser.repo.impl

import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.ReviewRepo
import kotlinx.coroutines.flow.Flow

class ReviewRepoImpl : ReviewRepo {
    override suspend fun loadAllReviews(): Flow<ApiResult> {
        TODO("Not yet implemented")
    }
}