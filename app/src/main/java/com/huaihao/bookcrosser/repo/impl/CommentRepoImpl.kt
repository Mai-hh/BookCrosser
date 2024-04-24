package com.huaihao.bookcrosser.repo.impl

import android.util.Log
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.network.BookCrosserApi
import com.huaihao.bookcrosser.network.NetUtil
import com.huaihao.bookcrosser.repo.CommentRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CommentRepoImpl : CommentRepo {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val api = BookCrosserApi.bookCrosserApiService

    companion object {
        private const val TAG = "CommentRepoImpl"
    }
    override suspend fun loadAllComments(): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.selectAllComments()
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch {
        Log.e(TAG, "loadAllComments: ${it.message}")
        it.printStackTrace()
    }

    override suspend fun loadMyComments(): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.selectMyComments()
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch {
        Log.e(TAG, "loadMyComments: ${it.message}")
        it.printStackTrace()
    }

    override suspend fun updateComment(commentId: Long, content: String): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.updateComment(commentId, content)
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch {
        Log.e(TAG, "updateComment: ${it.message}")
        it.printStackTrace()
    }

    override suspend fun deleteComment(commentId: Long): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.deleteComment(commentId)
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch {
        Log.e(TAG, "deleteComment: ${it.message}")
        it.printStackTrace()
    }
}