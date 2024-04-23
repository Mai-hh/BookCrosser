package com.huaihao.bookcrosser.repo.impl

import android.util.Log
import com.huaihao.bookcrosser.model.User
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.network.BookCrosserApi
import com.huaihao.bookcrosser.network.NetUtil
import com.huaihao.bookcrosser.repo.AuthRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepoImpl : AuthRepo {

    companion object {
        const val TAG = "AuthRepoImpl"
    }

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
        Log.d(TAG, "loginByEmail: $response")
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch { it.printStackTrace() }

    override suspend fun loginByUsername(username: String, password: String): Flow<ApiResult> =
        flow {
            emit(ApiResult.Loading())
            val response = api.login(
                User(
                    username = username,
                    password = password
                )
            )
            NetUtil.checkResponse(response, this)
        }.flowOn(dispatcher).catch { it.printStackTrace() }

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

    }.flowOn(dispatcher).catch {
        Log.d(TAG, "register: ${it.message}")
        it.printStackTrace()
    }

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

    override suspend fun checkLogin(token: String): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.checkLogin()
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch { it.printStackTrace() }

    override suspend fun loadUserProfile(): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.loadUserProfile()
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch {
        Log.e(TAG, "loadUserProfile: ${it.message}")
        it.printStackTrace()
    }

    override suspend fun updateProfile(
        username: String,
        bio: String?,
        latitude: Double?,
        longitude: Double?
    ): Flow<ApiResult> {
        return flow {
            emit(ApiResult.Loading())
            val response = api.update(
                username = username,
                bio = bio ?: "",
                latitude = latitude,
                longitude = longitude
            )
            NetUtil.checkResponse(response, this)
        }.flowOn(dispatcher).catch {
            it.printStackTrace()
        }
    }

    override suspend fun loadAllUsers(): Flow<ApiResult> = flow {
        emit(ApiResult.Loading())
        val response = api.selectAll()
        NetUtil.checkResponse(response, this)
    }.flowOn(dispatcher).catch {
        Log.e(TAG, "loadAllUsers: ${it.message}")
        it.printStackTrace()
    }

}