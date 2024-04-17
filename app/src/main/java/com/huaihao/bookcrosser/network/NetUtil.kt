package com.huaihao.bookcrosser.network

import kotlinx.coroutines.flow.FlowCollector
import org.json.JSONObject
import retrofit2.Response

object NetUtil {
    suspend fun <T> checkResponse(
        response: Response<T>?,
        flow: FlowCollector<ApiResult>
    ) {
        if (response?.isSuccessful == true) {
            flow.emit(ApiResult.Success(data = response.body()))
        } else {
            val json = response?.errorBody()?.string()
            val jsonObject = json?.let { JSONObject(it) }
            val returnCondition = jsonObject?.getString("message")
            val errorCode = jsonObject?.getString("code")
            flow.emit(
                ApiResult.Error(
                    code = errorCode?.toInt() ?: response?.code() ?: 0,
                    errorMessage = returnCondition
                )
            )
            response?.errorBody()?.close()
        }
    }

}

