package com.huaihao.bookcrosser.repo

import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.model.Drifting
import com.huaihao.bookcrosser.model.RequestBody
import com.huaihao.bookcrosser.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface BookRepo {

    suspend fun loadBooks(): Flow<ApiResult>

    suspend fun shelfABook(book: RequestBody.Book): Flow<ApiResult>
    suspend fun requestABook(drifting: Drifting): Flow<ApiResult>

}