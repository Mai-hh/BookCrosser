package com.huaihao.bookcrosser.viewmodel.main

import com.huaihao.bookcrosser.ui.common.BaseViewModel


sealed interface RequestsEvent {
    data object AddBook : RequestsEvent
}

data class RequestsUiState(
    val searchingText: String = "",
    val isSearching: Boolean = false,
)

class RequestsViewModel : BaseViewModel<RequestsUiState, RequestsEvent>() {
    override fun onEvent(event: RequestsEvent) {
        TODO("Not yet implemented")
    }

    override fun defaultState(): RequestsUiState = RequestsUiState()
}