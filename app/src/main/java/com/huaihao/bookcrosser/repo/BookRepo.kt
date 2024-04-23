package com.huaihao.bookcrosser.repo

import com.huaihao.bookcrosser.model.RequestBody
import com.huaihao.bookcrosser.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface BookRepo {

    suspend fun loadBooks(): Flow<ApiResult>
    suspend fun shelfABook(book: RequestBody.Book): Flow<ApiResult>
    suspend fun requestABook(bookId: Long): Flow<ApiResult>
    suspend fun search(title: String?, author: String?, matchComplete: Boolean): Flow<ApiResult>
    suspend fun searchByIsbn(isbn: String): Flow<ApiResult>
    suspend fun loadDriftingRequests(): Flow<ApiResult>
    suspend fun drift(driftingRequestId: Long): Flow<ApiResult>
    suspend fun rejectDriftingRequest(driftingRequestId: Long): Flow<ApiResult>
    suspend fun driftingFinish(bookId: Long): Flow<ApiResult>

}