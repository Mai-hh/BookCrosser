package com.huaihao.bookcrosser.viewmodel

import com.huaihao.bookcrosser.ui.common.BaseViewModel


sealed interface SearchEvent {
    data class SearchingTextChange(val text: String) : SearchEvent
    data class Search(val query: String): SearchEvent

    data class ToggleSearch(val active: Boolean): SearchEvent
}

data class SearchUiState(
    val searchingText: String = "",
    val isSearching: Boolean = false,
)

class SearchViewModel: BaseViewModel<SearchUiState, SearchEvent>() {
    override fun onEvent(event: SearchEvent) = when (event) {
        is SearchEvent.Search -> onSearch(query = event.query)
        is SearchEvent.SearchingTextChange -> onSearchingTextChange(text = event.text)
        is SearchEvent.ToggleSearch -> onToggleSearch(active = event.active)
    }

    override fun defaultState(): SearchUiState = SearchUiState()

    private fun onSearchingTextChange(text: String) {
        state = state.copy(
            searchingText = text
        )
    }

    private fun onSearch(query: String) {

    }

    fun onToggleSearch(active: Boolean) {
        state = state.copy(
            isSearching = active
        )
    }

}