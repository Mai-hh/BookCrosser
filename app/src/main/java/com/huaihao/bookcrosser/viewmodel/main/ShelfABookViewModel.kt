package com.huaihao.bookcrosser.viewmodel.main

import androidx.lifecycle.viewModelScope
import com.huaihao.bookcrosser.model.Book
import com.huaihao.bookcrosser.network.ApiResult
import com.huaihao.bookcrosser.repo.DriftingRepo
import com.huaihao.bookcrosser.ui.common.BaseViewModel
import com.huaihao.bookcrosser.ui.common.UiEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface ShelfABookEvent {
    data class CoverUrlChange(val coverUrl: String) : ShelfABookEvent
    data class TitleChange(val title: String) : ShelfABookEvent
    data class AuthorChange(val author: String) : ShelfABookEvent
    data class IsbnChange(val isbn: String) : ShelfABookEvent
    data object UploadCover : ShelfABookEvent
    data class DescriptionChange(val description: String) : ShelfABookEvent
    data object ShelfBook : ShelfABookEvent
    data object NavBack : ShelfABookEvent
}

data class ShelfABookUiState(
    var coverUrl: String = "",
    var title: String = "",
    var author: String = "",
    var isbn: String = "",
    var description: String = "",
    var isLoading: Boolean = false
)

class ShelfABookViewModel(private val driftingRepo: DriftingRepo) :
    BaseViewModel<ShelfABookUiState, ShelfABookEvent>() {
    override fun onEvent(event: ShelfABookEvent) {
        when (event) {
            is ShelfABookEvent.CoverUrlChange -> onCoverUrlChange(coverUrl = event.coverUrl)
            is ShelfABookEvent.TitleChange -> onTitleChange(title = event.title)
            is ShelfABookEvent.AuthorChange -> onAuthorChange(author = event.author)
            is ShelfABookEvent.IsbnChange -> onIsbnChange(isbn = event.isbn)
            is ShelfABookEvent.DescriptionChange -> onDescriptionChange(description = event.description)
            ShelfABookEvent.ShelfBook -> onShelfBook()
            ShelfABookEvent.NavBack -> sendEvent(UiEvent.NavBack)
            ShelfABookEvent.UploadCover -> sendEvent(UiEvent.Toast("封面已上传"))
        }
    }

    override fun defaultState(): ShelfABookUiState = ShelfABookUiState()

    private fun onCoverUrlChange(coverUrl: String) {
        state = state.copy(
            coverUrl = coverUrl,
        )
    }

    private fun onTitleChange(title: String) {
        state = state.copy(
            title = title,
        )
    }

    private fun onAuthorChange(author: String) {
        state = state.copy(
            author = author,
        )
    }

    private fun onIsbnChange(isbn: String) {
        state = state.copy(
            isbn = isbn
        )
    }

    private fun onDescriptionChange(description: String) {
        state = state.copy(
            description = description
        )
    }

    private fun onShelfBook() {
        val book = Book(
            coverUrl = state.coverUrl,
            title = state.title,
            author = state.author,
            isbn = state.isbn,
            description = state.description
        )


        state = state.copy(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {
            driftingRepo.shelfABook(book).collect { result ->
                when (result) {
                    is ApiResult.Success<*> -> {
                        state = state.copy(isLoading = false)
                        sendEvent(UiEvent.Toast("上架成功"))
                        sendEvent(UiEvent.NavBack)
                    }

                    is ApiResult.Error -> {
                        state = state.copy(isLoading = false)
                        sendEvent(UiEvent.Toast("上架失败\n ${result.errorMessage}"))
                    }

                    is ApiResult.Loading -> {}
                }
            }
        }
    }

}