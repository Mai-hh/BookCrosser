package com.huaihao.bookcrosser.viewmodel.main

import androidx.lifecycle.viewModelScope
import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.BookRepo
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import com.huaihao.bookcrosser.ui.main.Destinations.MAP_ROUTE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


sealed interface SearchEvent {
    data class SearchingTextChange(val text: String) : SearchEvent
    data class Search(val type: SearchType) : SearchEvent
    data object ShowSearchResults : SearchEvent
    data object ResetShouldExpandBottomSheet : SearchEvent

    data class NavToBookMarker(val book: Book) : SearchEvent
}

sealed class SearchType(val value: Int) {
    data class Basic(val title: String, val author: String, val matchComplete: Boolean) :
        SearchType(1)

    data class ISBN(val isbn: String) : SearchType(2)
    data class BCID(val bcid: String) : SearchType(3)


}

data class SearchUiState(
    val isSearching: Boolean = false,
    val books: List<Book> = mutableListOf(),
    val shouldExpandBottomSheet: Boolean = false
)

sealed class SearchStateWithType(labels: List<String>) {
    data class Basic(
        val title: String,
        val author: String,
        val matchComplete: Boolean
    ) : SearchStateWithType(labels = listOf("书名", "作者"))

    data class ISBN(
        val isbn: String,
    ) : SearchStateWithType(labels = listOf("ISBN"))

    data class BCID(
        val bcid: String
    ) : SearchStateWithType(labels = listOf("BCID"))
}

class SearchViewModel(private val bookRepo: BookRepo) :
    BaseViewModel<SearchUiState, SearchEvent>() {
    override fun onEvent(event: SearchEvent) = when (event) {
        is SearchEvent.Search -> {
            when (event.type) {
                is SearchType.Basic -> {
                    onBasicSearch(
                        event.type.title,
                        event.type.author,
                        event.type.matchComplete
                    )
                }

                is SearchType.ISBN -> {
                    onISBNSearch(event.type.isbn)
                }

                is SearchType.BCID -> TODO()

            }
        }

        is SearchEvent.SearchingTextChange -> TODO()
        is SearchEvent.ShowSearchResults -> {
            state = state.copy(
                isSearching = false
            )
        }

        is SearchEvent.ResetShouldExpandBottomSheet -> {
            state = state.copy(shouldExpandBottomSheet = false)
        }

        is SearchEvent.NavToBookMarker -> {
            sendEvent(UiEvent.Navigate("$MAP_ROUTE/${event.book.latitude}/${event.book.longitude}"))
        }
    }

    override fun defaultState(): SearchUiState = SearchUiState()

    private fun onBasicSearch(title: String?, author: String?, matchComplete: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            bookRepo.search(title, author, matchComplete).collect {
                when (it) {
                    is ApiResult.Success<*> -> {
                        state = state.copy(
                            books = (it.data as List<Book>),
                            isSearching = false,
                            shouldExpandBottomSheet = true
                        )
                    }

                    is ApiResult.Error -> {
                        sendEvent(UiEvent.Toast("搜索失败\n原因: ${it.errorMessage}"))
                        state = state.copy(
                            isSearching = false
                        )
                    }

                    is ApiResult.Loading -> {
                        state = state.copy(
                            isSearching = true
                        )
                    }
                }
            }
        }
    }

    private fun onISBNSearch(isbn: String) {
        viewModelScope.launch(Dispatchers.IO) {
            bookRepo.searchByIsbn(isbn).collect {
                when (it) {
                    is ApiResult.Success<*> -> {
                        state = state.copy(
                            books = (it.data as List<Book>),
                            isSearching = false,
                            shouldExpandBottomSheet = true
                        )
                    }

                    is ApiResult.Error -> {
                        sendEvent(UiEvent.Toast("搜索失败\n原因: ${it.errorMessage}"))
                        state = state.copy(
                            isSearching = false
                        )
                    }

                    is ApiResult.Loading -> {
                        state = state.copy(
                            isSearching = true
                        )
                    }
                }
            }
        }
    }
}