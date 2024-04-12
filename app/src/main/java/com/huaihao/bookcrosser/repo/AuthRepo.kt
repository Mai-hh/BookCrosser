package com.huaihao.bookcrosser.repo

import com.huaihao.bookcrosser.model.User
import com.huaihao.bookcrosser.network.ApiResult

interface AuthRepo {
    suspend fun login(usernameOrEmail: String, password: String): ApiResult<User>
    suspend fun register(username: String, email: String, password: String): ApiResult<User>
    suspend fun sendResetCode(email: String): ApiResult<Boolean>
    suspend fun resetPassword(email: String, code: String, newPassword: String): ApiResult<Boolean>
}