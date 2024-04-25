package com.huaihao.bookcrosser.viewmodel.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.huaihao.bookcrosser.model.BookRequest
import com.huaihao.bookcrosser.model.DriftingRequest
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.BookRepo
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import com.huaihao.bookcrosser.ui.main.Destinations.MAP_ROUTE
import com.huaihao.bookcrosser.ui.main.map.Type
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface RequestDriftingEvent {
    data object LoadDriftingRequests : RequestDriftingEvent
    data object NavBack : RequestDriftingEvent
    data class Locate(val driftingRequest: DriftingRequest) : RequestDriftingEvent
    data class Drift(val driftingRequest: DriftingRequest) : RequestDriftingEvent
    data class RejectDriftingRequest(val driftingRequest: DriftingRequest) : RequestDriftingEvent
}

data class RequestDriftingUiState(
    val request: List<DriftingRequest> = emptyList(),
    var isLoading: Boolean = false
)

class RequestDriftingViewModel(private val bookRepo: BookRepo) :
    BaseViewModel<RequestDriftingUiState, RequestDriftingEvent>() {
    override fun onEvent(event: RequestDriftingEvent) {
        when (event) {
            RequestDriftingEvent.LoadDriftingRequests -> {
                onLoadDriftingRequests()
            }

            RequestDriftingEvent.NavBack -> {
                sendEvent(UiEvent.NavBack)
            }

            is RequestDriftingEvent.Drift -> {
                onDrift(event.driftingRequest)
            }

            is RequestDriftingEvent.RejectDriftingRequest -> {
                onRejectDriftingRequest(event.driftingRequest)
            }

            is RequestDriftingEvent.Locate -> {
                if (event.driftingRequest.requester.latitude == null || event.driftingRequest.requester.longitude == null) {
                    sendEvent(UiEvent.SnackbarToast("用户未确定位置，谨慎起漂"))
                    return
                }

                sendEvent(
                    UiEvent.Navigate(
                        MAP_ROUTE +
                                "/" +
                                "${event.driftingRequest.requester.latitude}" +
                                "/" +
                                "${event.driftingRequest.requester.longitude}" +
                                "/" +
                                "${event.driftingRequest.requester.username}" +
                                "/" +
                                "${event.driftingRequest.requester.bio}" + "/" +
                                Type.USER
                    )
                )
            }
        }
    }

    private fun onRejectDriftingRequest(driftingRequest: DriftingRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            bookRepo.rejectDriftingRequest(driftingRequest.id).collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        sendEvent(UiEvent.SnackbarToast("拒绝 ${driftingRequest.book.title} 成功"))
                        Log.d(TAG, "onRejectDriftingRequest: Success")
                        onLoadDriftingRequests()
                    }

                    is ApiResult.Error -> {
                        sendEvent(UiEvent.SnackbarToast("拒绝 ${driftingRequest.book.title} 失败\n原因: ${result.errorMessage}"))
                        Log.d(TAG, "onRejectDriftingRequest: Error")
                        onLoadDriftingRequests()
                    }

                    is ApiResult.Loading -> {
                        Log.d(TAG, "onRejectDriftingRequest: Loading")
                    }
                }
            }
        }
    }

    private fun onDrift(driftingRequest: DriftingRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            bookRepo.drift(driftingRequest.id).collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        sendEvent(UiEvent.SnackbarToast("起漂 ${driftingRequest.book.title} 成功"))
                        Log.d(TAG, "onDrift: Success")
                        onLoadDriftingRequests()
                    }

                    is ApiResult.Error -> {
                        sendEvent(UiEvent.SnackbarToast("起漂 ${driftingRequest.book.title} 失败"))
                        Log.d(TAG, "onDrift: Error")
                        onLoadDriftingRequests()
                    }

                    is ApiResult.Loading -> {
                        Log.d(TAG, "onDrift: Loading")
                    }
                }
            }
        }
    }

    private fun onLoadDriftingRequests() {
        state = state.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            bookRepo.loadDriftingRequests().collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        val requests = (result.data as List<DriftingRequest>)
                        Log.d(TAG, "onLoadDriftingRequests: $requests")
                        state = state.copy(request = requests, isLoading = false)
                    }

                    is ApiResult.Error -> {
                        state = state.copy(isLoading = false)
                    }

                    is ApiResult.Loading -> {
                        Log.d(TAG, "onLoadDriftingRequests: Loading")
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "RequestDriftingViewModel"
    }

    override fun defaultState(): RequestDriftingUiState = RequestDriftingUiState()
}