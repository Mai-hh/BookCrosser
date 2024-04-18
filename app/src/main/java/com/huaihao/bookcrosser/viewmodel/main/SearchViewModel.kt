package com.huaihao.bookcrosser.viewmodel.main

import com.huaihao.bookcrosser.ui.common.BaseViewModel


sealed interface SearchEvent {
    data class SearchingTextChange(val text: String) : SearchEvent
    data class Search(val type: SearchType) : SearchEvent

    data object BasicSelected : SearchEvent

    data object ISBNSelected : SearchEvent

    data object BCIDSelected : SearchEvent

    data class ToggleSearch(val active: Boolean) : SearchEvent
}

sealed class SearchType(val value: Int) {
    data class Basic(val title: String, val author: String) : SearchType(1)
    data class ISBN(val isbn: String) : SearchType(2)
    data class BCID(val bcid: String) : SearchType(3)
}

data class SearchUiState(
    val isSearching: Boolean = false,
)

sealed class SearchStateWithType(labels: List<String>) {
    data class Basic(
        val title: String,
        val author: String,
    ) : SearchStateWithType(labels = listOf("书名", "作者"))
    data class ISBN(
        val isbn: String,
    ) : SearchStateWithType(labels = listOf("ISBN"))
    data class BCID(
        val bcid: String
    ) : SearchStateWithType(labels = listOf("BCID"))
}

class SearchViewModel : BaseViewModel<SearchUiState, SearchEvent>() {
    override fun onEvent(event: SearchEvent) = when (event) {
        SearchEvent.BCIDSelected -> TODO()
        SearchEvent.BasicSelected -> TODO()
        SearchEvent.ISBNSelected -> TODO()
        is SearchEvent.Search -> TODO()
        is SearchEvent.SearchingTextChange -> TODO()
        is SearchEvent.ToggleSearch -> TODO()
    }

    override fun defaultState(): SearchUiState = SearchUiState()

    private fun onSearchingTextChange(text: String) {
        state = state.copy(

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