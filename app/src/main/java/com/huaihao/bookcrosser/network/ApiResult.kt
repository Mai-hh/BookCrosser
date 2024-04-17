package com.huaihao.bookcrosser.network

import com.squareup.moshi.Json

enum class ApiStatus {
    SUCCESSFUL,
    LOADING,
    ERROR
}

sealed class ApiResult {
    data class Success<T>(
        val status: ApiStatus = ApiStatus.SUCCESSFUL,
        var data: T? = null
    ) : ApiResult()

    data class Error(
        val status: ApiStatus = ApiStatus.ERROR,
        val code: Int,
        val errorMessage: String? = null
    ) : ApiResult()

    data class Loading(
        val status: ApiStatus = ApiStatus.LOADING
    ) : ApiResult()
}

data class TokenResponse(
    val code: Int,
    @Json(name = "token") val token: String,
)