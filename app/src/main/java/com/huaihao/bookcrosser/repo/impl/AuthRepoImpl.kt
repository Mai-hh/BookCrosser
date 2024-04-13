package com.huaihao.bookcrosser.repo.impl

import com.huaihao.bookcrosser.model.User
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.network.BookCrosserApi
import com.huaihao.bookcrosser.repo.AuthRepo

class AuthRepoImpl : AuthRepo {
    private val api = BookCrosserApi.bookCrosserApiService
    override suspend fun login(usernameOrEmail: String, password: String): ApiResult<Unit> {
        val response = api.login(
            User(
                username = usernameOrEmail,
                email = usernameOrEmail,
                password = password
            )
        )
        return if (response.isSuccessful) {
            ApiResult.Success(response.body()!!)
        } else {
            ApiResult.Error(response.errorBody()?.string() ?: "Unknown error")
        }
    }

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): ApiResult<User> {
        TODO("Not yet implemented")
    }

    override suspend fun sendResetCode(email: String): ApiResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(
        email: String,
        code: String,
        newPassword: String
    ): ApiResult<Boolean> {
        TODO("Not yet implemented")
    }
}