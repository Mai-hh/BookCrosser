package com.huaihao.bookcrosser.repo.impl

import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.model.Drifting
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.network.BookCrosserApi
import com.huaihao.bookcrosser.network.NetUtil
import com.huaihao.bookcrosser.repo.DriftingRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DriftingRepoImpl : DriftingRepo {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val api = BookCrosserApi.bookCrosserApiService
    override suspend fun shelfABook(book: Book): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.shelfABook(book)
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch { it.printStackTrace() }

    override suspend fun requestABook(drifting: Drifting): Flow<ApiResult> {
        TODO("Not yet implemented")
    }
}