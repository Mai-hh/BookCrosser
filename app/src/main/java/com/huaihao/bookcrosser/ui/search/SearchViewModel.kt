package com.huaihao.bookcrosser.ui.search

import androidx.lifecycle.ViewModel


data class SearchEvent(
    val searchingText: String = "",
    val isSearching: Boolean = false,
)

class SearchViewModel: ViewModel() {



}