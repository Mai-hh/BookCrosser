package com.huaihao.bookcrosser.repo.impl

import android.util.Log
import com.huaihao.bookcrosser.model.Drifting
import com.huaihao.bookcrosser.model.RequestBody
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.network.BookCrosserApi
import com.huaihao.bookcrosser.network.NetUtil
import com.huaihao.bookcrosser.repo.BookRepo
import com.huaihao.bookcrosser.viewmodel.main.MapViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class BookRepoImpl : BookRepo {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val api = BookCrosserApi.bookCrosserApiService

    companion object {
        private const val TAG = "BookRepoImpl"
    }
    override suspend fun loadBooks(): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.selectAllBooks()
        Log.d(TAG, "loadBooks: ${response.body()}")
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch { it.printStackTrace() }

    override suspend fun shelfABook(book: RequestBody.Book): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.shelfABook(book)
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch { it.printStackTrace() }

    override suspend fun requestABook(drifting: Drifting): Flow<ApiResult> {
        TODO("Not yet implemented")
    }

    override suspend fun search(
        title: String?,
        author: String?,
        matchComplete: Boolean
    ): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.search(title, author, matchComplete)
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch { it.printStackTrace() }

    override suspend fun searchByIsbn(isbn: String): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.searchByIsbn(isbn)
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch { it.printStackTrace() }
}