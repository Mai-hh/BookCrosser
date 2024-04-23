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
        val response = api.shelfABook(
            title = book.title,
            author = book.author,
            isbn = book.isbn,
            coverUrl = book.coverUrl,
            description = book.description,
            latitude = book.latitude,
            longitude = book.longitude
        )
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch { it.printStackTrace() }

    override suspend fun requestABook(bookId: Long): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.request(bookId)
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch { it.printStackTrace() }

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

    override suspend fun loadDriftingRequests(): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.selectDriftingToMe()
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch {
        emit(
            ApiResult.Error(
                code = 1,
                errorMessage = "未知错误: ${it.message?.substring(0, 30)}",
            )
        )
        it.printStackTrace()
    }

    override suspend fun drift(bookId: Long, requesterId: Long): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.drift(bookId, requesterId)
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch {
        emit(
            ApiResult.Error(
                code = 1,
                errorMessage = "未知错误: ${it.message?.substring(0, 30)}",
            )
        )
        it.printStackTrace()
    }

    override suspend fun driftingFinish(bookId: Long): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.driftingFinish(bookId)
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch {
        emit(
            ApiResult.Error(
                code = 1,
                errorMessage = "未知错误: ${it.message?.substring(0, 30)}",
            )
        )
        it.printStackTrace()
    }
}