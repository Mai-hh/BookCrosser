package com.huaihao.bookcrosser.repo.impl

import com.huaihao.bookcrosser.model.Drifting
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.DriftingRepo
import kotlinx.coroutines.flow.Flow

class DriftingRepoImpl : DriftingRepo {
    override suspend fun shelfABook(drifting: Drifting): Flow<ApiResult> {
        TODO("Not yet implemented")
    }

    override suspend fun requestABook(drifting: Drifting): Flow<ApiResult> {
        TODO("Not yet implemented")
    }
}