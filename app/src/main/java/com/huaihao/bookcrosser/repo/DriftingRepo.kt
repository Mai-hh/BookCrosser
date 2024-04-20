package com.huaihao.bookcrosser.repo

import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.model.Drifting
import com.huaihao.bookcrosser.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface DriftingRepo {
    suspend fun shelfABook(book: Book): Flow<ApiResult>
    suspend fun requestABook(drifting: Drifting): Flow<ApiResult>

}