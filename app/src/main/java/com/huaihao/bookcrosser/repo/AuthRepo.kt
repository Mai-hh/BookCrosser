package com.huaihao.bookcrosser.repo

import com.huaihao.bookcrosser.network.ApiResult
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    suspend fun loginByEmail(email: String, password: String): Flow<ApiResult>
    suspend fun loginByUsername(username: String, password: String): Flow<ApiResult>
    suspend fun register(username: String, email: String, password: String): Flow<ApiResult>
    suspend fun sendResetCode(email: String): Flow<ApiResult>
    suspend fun resetPassword(email: String, code: String, newPassword: String): Flow<ApiResult>
    suspend fun checkLogin(token: String): Flow<ApiResult>
    suspend fun loadUserProfile(): Flow<ApiResult>
    suspend fun updateProfile(username: String, bio: String?, latitude: Double?, longitude: Double?): Flow<ApiResult>
    suspend fun loadAllUsers(): Flow<ApiResult>
}