package com.huaihao.bookcrosser.ui.requests

import com.huaihao.bookcrosser.ui.common.BaseViewModel


sealed interface RequestsEvent {
    data class SearchingTextChange(val text: String) : RequestsEvent
    data class Search(val query: String): RequestsEvent

    data class ToggleSearch(val active: Boolean): RequestsEvent
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