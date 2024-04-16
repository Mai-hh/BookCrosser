package com.huaihao.bookcrosser.repo.impl

import com.huaihao.bookcrosser.model.User
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.network.BookCrosserApi
import com.huaihao.bookcrosser.network.NetUtil
import com.huaihao.bookcrosser.repo.AuthRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepoImpl : AuthRepo {

    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val api = BookCrosserApi.bookCrosserApiService
    override suspend fun loginByEmail(email: String, password: String): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.login(
            User(
                email = email,
                password = password
            )
        )
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher)

    override suspend fun loginByUsername(username: String, password: String): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.login(
            User(
                username = username,
                password = password
            )
        )
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher)

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.register(
            User(
                email = email,
                username = username,
                password = password
            )
        )
        NetUtil.checkResponse(response, this)

    }.flowOn(dispatcher)

    override suspend fun sendResetCode(email: String): Flow<ApiResult> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(
        email: String,
        code: String,
        newPassword: String
    ): Flow<ApiResult> {
        TODO("Not yet implemented")
    }

}