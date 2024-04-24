package com.huaihao.bookcrosser.repo

import com.huaihao.bookcrosser.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface CommentRepo {

    suspend fun loadAllComments(): Flow<ApiResult>

    suspend fun loadMyComments(): Flow<ApiResult>

    suspend fun updateComment(commentId: Long, content: String): Flow<ApiResult>

    suspend fun deleteComment(commentId: Long): Flow<ApiResult>

}